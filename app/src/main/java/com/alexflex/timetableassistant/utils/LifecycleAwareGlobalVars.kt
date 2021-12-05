@file:Suppress("unused", "deprecation")
package com.alexflex.timetableassistant.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * This is a delegate for [Fragment] to get your component of type [T] when Fragment's
 * view is available.
 *
 * @param componentInitializer is a function that returns desired instance of [T].
 * @param onViewDestroyedCallback is a callback to be invoked when the fragment's view is
 * about to be destroyed and your [T] instance is about to become unavailable (i.e. null).
 * When this callback is invoked, [T] instance is not null yet, you can dispose it if needed.
 */
fun <T> Fragment.autoDestroyViewComponent(
    componentInitializer: () -> T,
    onViewDestroyedCallback: ((T) -> Unit)? = null
): ReadOnlyProperty<Fragment, T> {
    return AutoDestroyOnViewDestroyedComponent(this, componentInitializer, onViewDestroyedCallback)
}

/**
 * This is a delegate for [Fragment] to get your component of type [T] when the Fragment is
 * created and not destroyed yet.
 *
 * @param componentInitializer is a function that returns desired instance of [T].
 * @param onDestroyCallback is a callback to be invoked when the fragment is
 * about to be destroyed and your [T] instance is about to become unavailable (i.e. null).
 * When this callback is invoked, [T] instance is not null yet, you can dispose it if needed.
 */
fun <T> Fragment.autoDestroyLifecycleComponent(
    componentInitializer: () -> T,
    onDestroyCallback: ((T) -> Unit)? = null
): ReadOnlyProperty<Fragment, T> {
    return AutoDestroyComponent(lifecycle, componentInitializer, onDestroyCallback)
}

/**
 * This is a delegate for [AppCompatActivity] to get your component of type [T] when the Activity
 * is created and not destroyed yet.
 *
 * @param componentInitializer is a function that returns desired instance of [T].
 * @param onDestroyCallback is a callback to be invoked when the activity is about to be
 * destroyed and your [T] instance is about to become unavailable (i.e. null).
 * When this callback is invoked, [T] instance is not null yet, you can dispose it if needed.
 */
fun <T> AppCompatActivity.autoDestroyLifecycleComponent(
    componentInitializer: () -> T,
    onDestroyCallback: ((T) -> Unit)? = null
): ReadOnlyProperty<Fragment, T> {
    return AutoDestroyComponent(lifecycle, componentInitializer, onDestroyCallback)
}

private class AutoDestroyOnViewDestroyedComponent<I, T>(
    private val mFragment: Fragment,
    private val mComponentInitializer: () -> T,
    private val onViewDestroyedCallback: ((T) -> Unit)?
) : ReadOnlyProperty<I, T>, LifecycleObserver {

    init {
        mFragment.lifecycle.addObserver(this)
    }

    private var mComponentCreator: AutoDestroyComponent<I, T>? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @Suppress("unused")
    fun onFragmentDestroyed() {
        // keep in mind that this method is called twice:
        // the first call happens when fragment's view is destroyed and the second one happens
        // when the fragment is destroyed itself
        mComponentCreator = null
    }

    override fun getValue(thisRef: I, property: KProperty<*>): T {
        val state = mFragment.lifecycle.currentState
        require(state.isAtLeast(Lifecycle.State.CREATED) && state != Lifecycle.State.DESTROYED) {
            "Accessing lifecycle-aware component beyond lifecycle bounds"
        }
        // looks like we can safely access viewLifecycleOwner of mFragment here
        if (mComponentCreator == null) {
            val viewLifecycle = mFragment.viewLifecycleOwner.lifecycle
            // by doing so, we avoid leaking mComponentCreator instance,
            // so when the fragment's view is destroyed, mComponentCreator is destroyed too
            viewLifecycle.addObserver(this)
            // no need to explicit subscribe mComponentCreator instance to fragment's view
            // lifecycle: it's done under the hood
            mComponentCreator = AutoDestroyComponent(
                viewLifecycle,
                mComponentInitializer,
                onViewDestroyedCallback,
                mLifecycleStateChecker = {
                    // checking INITIALIZED state is needed here since it is totally okay
                    // to access fragment's view and viewLifecycleOwner in onViewCreated callback,
                    // but fragment's view state is INITIALIZED there, not "created"
                    it.isAtLeast(Lifecycle.State.INITIALIZED) && it != Lifecycle.State.DESTROYED
                }
            )
        }
        // don't worry about !!: at this line of the code, it is guaranteed that
        // mComponentCreator is not null, even though Kotlin compiler can't perform
        // smart cast here
        return mComponentCreator!!.getValue(thisRef, property)
    }
}

private class AutoDestroyComponent<I, T>(
    private val mLifecycle: Lifecycle,
    private val mComponentInitializer: () -> T,
    private val onViewDestroyedCallback: ((T) -> Unit)?,
    private val mLifecycleStateChecker: (Lifecycle.State) -> Boolean = {
        it.isAtLeast(Lifecycle.State.CREATED) && it != Lifecycle.State.DESTROYED
    }
) : ReadOnlyProperty<I, T>, LifecycleObserver {

    private var mComponent: T? = null

    init {
        mLifecycle.addObserver(this)
    }

    override fun getValue(thisRef: I, property: KProperty<*>): T {
        return getComponentOrThrow()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @Suppress("unused")
    fun onDestroy() {
        onViewDestroyedCallback?.invoke(mComponent!!)
        mComponent = null
    }

    private fun getComponentOrThrow(): T {
        require(mLifecycleStateChecker(mLifecycle.currentState)) {
            "Accessing view-dependent component before it is created or after it was destroyed"
        }
        if (mComponent == null) {
            mComponent = mComponentInitializer()
        }
        return mComponent!!
    }
}