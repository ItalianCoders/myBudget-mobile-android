package it.italiancoders.mybudget.utils

import android.content.Context
import android.graphics.drawable.BitmapDrawable
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

    fun loadImage(context: Context, imageView: ImageView, imageUrl: String?, defaultImageResId: Int, defaultTintColorResId: Int) {
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
                        val imageBitmap = (imageView.getDrawable() as BitmapDrawable).bitmap
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
}