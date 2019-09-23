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

package com.fb.splitscreenlauncher.util.icon

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.fb.splitscreenlauncher.util.misc.toPx


class IconUtil {

    companion object {

        const val STACKED = 1
        const val SPLIT = 2

        fun getIcon(style: Int = STACKED, iconFirst: Drawable?, iconSecond: Drawable?): Bitmap? {
            return when (style) {
                SPLIT -> null
                else -> Bitmap.createBitmap(56.toPx, 56.toPx, Bitmap.Config.ARGB_8888).also {

                    val canvas = Canvas(it)

                    iconFirst?.apply {
                        val tempBounds = copyBounds()
                        bounds = Rect(0, 0, 36.toPx, 36.toPx)
                        draw(canvas)
                        bounds = tempBounds
                    }

                    iconSecond?.mutate()?.apply {
                        val tempBounds = copyBounds()
                        bounds = Rect(0, 0, 36.toPx, 36.toPx).apply { offset(20.toPx, 20.toPx) }
                        draw(canvas)
                        bounds = tempBounds
                    }
                }
            }
        }

    }

}