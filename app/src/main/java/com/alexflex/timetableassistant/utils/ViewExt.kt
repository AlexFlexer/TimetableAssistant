@file:Suppress("unused")

package com.alexflex.timetableassistant.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/**
 * Makes the given view visible.
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * @return true if the given view is visible and false otherwise.
 */
fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

/**
 * Makes the given view not visible, but able to listen
 * for clicks and touches.
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * Fully removes the given view from the layout.
 * However, it doesn't become null and still accessible
 * through the code.
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * The view is visible if [visible] is true, and gone otherwise.
 */
fun View.visibleIf(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

/**
 * The view is visible if [invisible] is true, and invisible otherwise.
 */
fun View.invisibleIf(invisible: Boolean) {
    this.visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

/**
 * The view is gone if [isGone] is true, and visible otherwise.
 */
fun View.goneIf(isGone: Boolean) {
    this.visibility = if (isGone) View.GONE else View.VISIBLE
}

/**
 * The views visibility is set with help of alpha.
 */
inline var View.isVisibleAlpha: Boolean
    get() = alpha > 0f
    set(value) {
        alpha = if (value) 1f else 0f
    }

/**
 * Converts pixels to density-independent pixels.
 */
fun Int.toDp(context: Context): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()

/**
 * Converts text size to scaled pixels.
 */
fun Int.toSp(context: Context): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        context.resources.displayMetrics
    )

/**
 * Converts density-independent pixels to pixels.
 */
val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/**
 * Sets the given [listener] to be called when the given view is clicked.
 */
inline fun View.onClick(crossinline listener: () -> Unit) {
    setOnClickListener { listener() }
}

fun View.onClick() = setOnClickListener(null)

/**
 * Sets the given [listener] to be called when the given view is long clicked.
 *
 * @param listener is a listener to be assigned.
 * @param isViewLongClickable is a value to be returned from the lambda function
 *          of the listener.
 */
inline fun View.onLongClick(
    isViewLongClickable: Boolean = true,
    crossinline listener: (View) -> Unit,
) {
    setOnLongClickListener {
        listener(this)
        isViewLongClickable
    }
}

/**
 * Sets the given [listener] to be executed when the given view's state is changed
 * (either checked or unchecked).
 */
inline fun CompoundButton.onCheckedStateChanged(crossinline listener: (Boolean) -> Unit) {
    setOnCheckedChangeListener { _, isChecked ->
        listener(isChecked)
    }
}

/**
 * Removes current [CompoundButton.OnCheckedChangeListener] from the given
 * [CompoundButton].
 */
fun CompoundButton.removeCheckedStateListener() = setOnCheckedChangeListener(null)

/**
 * Shows the soft keyboard and focuses it on the given view.
 */
fun View.showKeyboard() {
    if (this.requestFocus()) {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

/**
 * Hides the soft keyboard from the view and the activity at all.
 */
fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.windowToken, 0)
}

/** Gets [LayoutInflater] instance using the [View]'s [Context]. */
fun View.getLayoutInflater(): LayoutInflater = context.getSystemServiceNotNull()

fun View.getColorCompat(@ColorRes id: Int, theme: Resources.Theme? = null) =
    context.getColorCompat(id, theme)

fun View.getDrawableCompat(@DrawableRes id: Int, theme: Resources.Theme? = null) =
    context.getDrawableCompat(id, theme)