@file:Suppress("unused")

package com.alexflex.timetableassistant.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.PowerManager
import android.view.Display
import androidx.annotation.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import java.util.regex.Pattern

/**
 * This function allows you to get a system service of the given type [T].
 *
 * @return the service of type [T] or null if the service is not supported by the system.
 */
inline fun <reified T> Context.getSystemServiceCompat(): T? {
    return ContextCompat.getSystemService(this, T::class.java)
}

/**
 * This function allows you to get a system service of the given type [T].
 *
 * @return the service of type [T] or an exception will be thrown if the service
 * is not supported by the system.
 */
inline fun <reified T> Context.getSystemServiceNotNull(): T {
    return try {
        getSystemServiceCompat()!!
    } catch (e: NullPointerException) {
        throw IllegalArgumentException("The service of class ${T::class.simpleName} is not supported!")
    }
}

/**
 * Gets a string by the given resource ID and turns it into [Pattern].
 */
fun Context.getStringAsPattern(@StringRes res: Int, flags: Int = 0): Pattern =
    getString(res).toPattern(flags)

/**
 * Gets the given string (referred to by [resId] ID) formatted appropriately for
 * the given [count].
 */
fun Context.plurals(@PluralsRes resId: Int, count: Int) =
    resources.getQuantityString(resId, count, count)

/**
 * Gets [Drawable] by the given [id] with help of [ResourcesCompat].
 */
fun Context.getDrawableCompat(@DrawableRes id: Int, theme: Resources.Theme? = null): Drawable? =
    ResourcesCompat.getDrawable(resources, id, theme)

/**
 * Gets color by the given [id] with help of [ResourcesCompat].
 */
@ColorInt
fun Context.getColorCompat(@ColorRes id: Int, theme: Resources.Theme? = null): Int =
    ResourcesCompat.getColor(resources, id, theme)

/**
 * Checks if the screen is on. This function handles Samsung's Always-On display thing.
 *
 * @return true if the screen is on and false otherwise.
 */
@Suppress("ObsoleteSdkInt", "deprecation")
fun Context.isScreenOn(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
        // no display service means the device has no displays and this the screen
        // can't be turned on
        val displayService = getSystemServiceCompat<DisplayManager>() ?: return false
        for (display in displayService.displays) {
            if (display.state == Display.STATE_ON) {
                return true
            }
        }
        false
    } else {
        val pm = getSystemServiceCompat<PowerManager>()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            pm?.isScreenOn == true
        else pm?.isInteractive == true
    }
}

/**
 * Turns vector image into a [Bitmap].
 *
 * @param drawableId is a drawable resource ID that needs to be resolved and turned into [Bitmap].
 */
fun Context.vectorToBitmap(@DrawableRes drawableId: Int): Bitmap? {
    val drawable = ContextCompat.getDrawable(this, drawableId) ?: return null
    val bitmap = createBitmap(
        drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

/**
 * Copies the given piece of text in the clipboard.
 */
fun Context.copyText(text: CharSequence, appName: String) {
    val clip = ClipData.newPlainText("Copied from $appName", text)
    getSystemServiceCompat<ClipboardManager>()?.setPrimaryClip(clip)
}

/**
 * Checks whether all the permissions are granted by the user.
 *
 * @param permissions are the permissions to check.
 */
fun Context.allGranted(vararg permissions: String) = permissions.all {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

/**
 * Checks whether at least one the permissions is granted by the user.
 *
 * @param permissions are the permissions to check.
 */
fun Context.isSomeGranted(vararg permissions: String) = permissions.any {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

/**
 * Checks whether any of the permissions needs to be explained to the user.
 *
 * @param permissions are the permissions to check.
 */
fun Activity.isNeedRationale(vararg permissions: String) = permissions.any {
    ActivityCompat.shouldShowRequestPermissionRationale(this, it)
}
