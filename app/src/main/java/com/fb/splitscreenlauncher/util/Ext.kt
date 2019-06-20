/*
 * Copyright (c) Francisco Barroso
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.fb.splitscreenlauncher.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import com.fb.splitscreenlauncher.ServiceAccessibility


// context

@Suppress("UNUSED_PARAMETER")
fun Context.toast(text: String? = null, res: Int = 0, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, text ?: getString(res), Toast.LENGTH_SHORT).show()
}

fun Context.launchUrl(url: String? = null, res: Int = 0) {

    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url ?: getString(res))))

}

fun Context.versionName(): String = appPackageInfo()?.versionName ?: ""

fun Context.appPackageInfo(): PackageInfo? {
    return try { packageManager.getPackageInfo(packageName, 0) ?: null }
    catch (ex: Exception) { null }
}

fun Context.isAccessibilityEnabled(
    pkgName: String = this.packageName,
    serviceName: String = ServiceAccessibility::class.java.name,
    serviceNameSimple: String = ServiceAccessibility::class.java.simpleName): Boolean {

    return try {

        val accessibilityPath = "$pkgName/$serviceName"
        val accessibilitySimplePath = "$pkgName/.$serviceNameSimple"

        Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES).let {
            it.contains(accessibilityPath) || it.contains(accessibilitySimplePath)
        }

    } catch (ignored: Exception) {

        false
    }
}

fun Context.getThemeColor(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}


// Drawable

fun Drawable.asBitmap(): Bitmap {
    return when {
        this is BitmapDrawable -> bitmap
        else -> {

            val width = Math.max(if (bounds.isEmpty) intrinsicWidth else bounds.width(), 1)
            val height = Math.max(if (bounds.isEmpty) intrinsicHeight else bounds.height(), 1)

            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also {
                val canvas = Canvas(it)
                setBounds(0, 0, canvas.width, canvas.height)
                draw(canvas)
            }
        }
    }
}


// Bitmap

fun Bitmap.scale(maxWidthAndHeight: Int): Bitmap {

    val newWidth: Int
    val newHeight: Int

    if (this.width >= this.height) {

        val ratio:Float = this.width.toFloat() / this.height.toFloat()
        newWidth = maxWidthAndHeight
        newHeight = Math.max(Math.round(maxWidthAndHeight / ratio), 1)

    } else {

        val ratio:Float = this.height.toFloat() / this.width.toFloat()
        newWidth = Math.round(maxWidthAndHeight / ratio)
        newHeight = Math.max(maxWidthAndHeight, 1)
    }

    return Bitmap.createScaledBitmap(this, newWidth, newHeight, false)
}

fun Bitmap.asDrawable(res: Resources): BitmapDrawable = BitmapDrawable(res,this)


// string

fun String.ld(tag: String = "debug") { Log.d(tag, this) }


// dpi

val density: Float = Math.max(Resources.getSystem().displayMetrics.density, 1f)

val Int.dpiToPx: Int
    get() = Math.round(this * density)

val Double.pixel: Double
    get() = Math.round(this * density).toDouble()

val Float.pixel: Float
    get() = Math.round(this * density).toFloat()