@file:Suppress("unused")

package com.alexflex.timetableassistant.utils

import android.app.Activity
import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Retrieves the given extra from [Activity.getIntent].getExtras() associated with the [key].
 * If [key] is null, property's name will be used as the key. This extra must not be null,
 * or an exception will be thrown.
 *
 * @throws IllegalStateException if an extra associated with the given [key] is not found or
 * is not type of [T].
 */
fun <T : Any> extra(key: String? = null): ReadWriteProperty<Activity, T> =
    ActivityExtraDelegate(key)

/**
 * Retrieves the given extra from [Activity.getIntent].getExtras() associated with the [key].
 * If [key] is null, property's name will be used as the key. The extra can be null.
 */
fun <T : Any> extraNullable(key: String? = null): ReadWriteProperty<Activity, T?> =
    ActivityNullableExtraDelegate(key)

/**
 * Retrieves the given enum extra from [Activity.getIntent].getExtras() associated with the [key].
 * If [key] is null, property's name will be used as the key. This enum extra must not be null,
 * or an exception will be thrown.
 *
 * @throws IllegalStateException if an enum extra associated with the given [key] is not found or
 * is not type of [T].
 */
inline fun <reified T : Enum<T>> extraEnum(key: String? = null): ReadWriteProperty<Activity, T> =
    activityExtraEnum(key)

/**
 * Retrieves the given enum extra from [Activity.getIntent].getExtras() associated with the [key].
 * If [key] is null, property's name will be used as the key. The enum extra can be null.
 */
inline fun <reified T : Enum<T>> extraEnumNullable(key: String? = null): ReadWriteProperty<Activity, T?> =
    activityExtraEnumNullable(key)


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Internal things ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline fun <reified T : Enum<T>> activityExtraEnum(key: String?): ReadWriteProperty<Activity, T> {
    return object : ReadWriteProperty<Activity, T> {
        override fun getValue(thisRef: Activity, property: KProperty<*>): T {
            val argKey = key ?: property.name
            val enumValName = thisRef.intent?.extras
                ?.getString(argKey)
                ?: throw IllegalArgumentException("Enum ${property.name} could not be read")
            return enumValueOf(enumValName)
        }

        override fun setValue(thisRef: Activity, property: KProperty<*>, value: T) {
            val extraKey = key ?: property.name
            val args = thisRef.intent.extras
                ?: Bundle().also { thisRef.intent.putExtras(it) }
            args.put(extraKey, value.name)
        }

    }
}

inline fun <reified T : Enum<T>> activityExtraEnumNullable(key: String?): ReadWriteProperty<Activity, T?> {
    return object : ReadWriteProperty<Activity, T?> {
        override fun getValue(thisRef: Activity, property: KProperty<*>): T? {
            val argKey = key ?: property.name
            val enumValName = thisRef.intent?.extras
                ?.getString(argKey)
            return if (enumValName == null) null else enumValueOf<T>(enumValName)
        }

        override fun setValue(thisRef: Activity, property: KProperty<*>, value: T?) {
            val extraKey = key ?: property.name
            val args = thisRef.intent.extras
                ?: Bundle().also { thisRef.intent.putExtras(it) }
            args.put(extraKey, value?.name ?: return)
        }

    }
}

class ActivityExtraDelegate<T : Any>(private val key: String?) :
    ReadWriteProperty<Activity, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(
        thisRef: Activity,
        property: KProperty<*>,
    ): T {
        val argKey = key ?: property.name
        return thisRef.intent?.extras
            ?.get(argKey) as? T
            ?: throw IllegalArgumentException("Property ${property.name} could not be read")
    }

    override fun setValue(
        thisRef: Activity,
        property: KProperty<*>, value: T,
    ) {
        val args = thisRef.intent.extras
            ?: Bundle().also { thisRef.intent.putExtras(it) }
        val argKey = key ?: property.name
        args.put(argKey, value)
    }
}

class ActivityNullableExtraDelegate<T : Any?>(private val key: String?) :
    ReadWriteProperty<Activity, T?> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(
        thisRef: Activity,
        property: KProperty<*>,
    ): T? {
        val argKey = key ?: property.name
        return thisRef.intent?.extras?.get(argKey) as? T
    }

    override fun setValue(
        thisRef: Activity,
        property: KProperty<*>, value: T?,
    ) {
        val args = thisRef.intent?.extras
            ?: Bundle().also { thisRef.intent.putExtras(it) }
        val argKey = key ?: property.name
        value?.let { args.put(argKey, it) } ?: args.remove(argKey)
    }
}