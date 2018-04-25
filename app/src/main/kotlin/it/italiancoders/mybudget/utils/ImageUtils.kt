/*
 * Project: myBudget-mobile-android
 * File: ImageUtils.kt
 *
 * Created by fattazzo
 * Copyright © 2018 Gianluca Fattarsi. All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package it.italiancoders.mybudget.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception


/**
 * @author fattazzo
 *         <p/>
 *         date: 12/04/18
 */
object ImageUtils {

    fun loadImage(context: Context, imageView: ImageView, imageUrl: String?, defaultImageResId: Int, defaultTintColorResId: Int = android.R.color.darker_gray) {
        if (imageUrl.orEmpty().isBlank()) {
            applyDefaultUserImage(context, imageView, defaultImageResId, defaultTintColorResId)
            return
        }

        Picasso.get().load(imageUrl).fetch(object : Callback {
            override fun onSuccess() {
                imageView.imageTintList = null
                imageView.alpha = 0f
                Picasso.get().load(imageUrl).into(imageView, object : Callback {
                    override fun onSuccess() {
                        val imageBitmap = (imageView.drawable as BitmapDrawable).bitmap
                        val imageDrawable = RoundedBitmapDrawableFactory.create(context.resources, imageBitmap)
                        imageDrawable.isCircular = true
                        imageDrawable.cornerRadius = Math.max(imageBitmap.width, imageBitmap.height) / 2.0f
                        imageView.setImageDrawable(imageDrawable)
                    }

                    override fun onError(e: Exception?) = applyDefaultUserImage(context, imageView, defaultImageResId, defaultTintColorResId)
                })
                imageView.animate().setDuration(300).alpha(1f).start()
            }

            override fun onError(e: Exception?) = applyDefaultUserImage(context, imageView, defaultImageResId, defaultTintColorResId)
        })
    }

    private fun applyDefaultUserImage(context: Context, imageView: ImageView, defaultImageResId: Int, defaultTintColorResId: Int) {
        imageView.setImageResource(defaultImageResId)
        imageView.imageTintList = ContextCompat.getColorStateList(context, defaultTintColorResId)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(400,400, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, 400, 400)
        vectorDrawable.draw(canvas)
        return bitmap
    }
}