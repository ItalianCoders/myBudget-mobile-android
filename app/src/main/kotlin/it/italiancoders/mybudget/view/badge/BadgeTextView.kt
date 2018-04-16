package it.italiancoders.mybudget.view.badge

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import it.italiancoders.mybudget.R


class BadgeTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var backgroundColor: Int = 0
    private var borderColor: Int = 0
    private var borderWidth: Float = 0.toFloat()
    private var borderAlpha: Float = 0.toFloat()
    private var ctType: Int = 0
    private var density: Float = 0.toFloat()
    private var mShadowRadius: Int = 0
    private var shadowYOffset: Int = 0
    private var shadowXOffset: Int = 0

    private var basePadding: Int = 0
    private var diffWH: Int = 0

    private var isHighLightMode: Boolean = false

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        gravity = Gravity.CENTER
        density = getContext().resources.displayMetrics.density
        mShadowRadius = (density * SHADOW_RADIUS).toInt()
        shadowYOffset = (density * Y_OFFSET).toInt()
        shadowXOffset = (density * X_OFFSET).toInt()
        basePadding = mShadowRadius * 2
        val textHeight = textSize
        val textWidth = textHeight / 4
        diffWH = (Math.abs(textHeight - textWidth) / 2).toInt()
        val horizontalPadding = basePadding + diffWH
        setPadding(horizontalPadding, basePadding, horizontalPadding, basePadding)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BadgeTextView)
        backgroundColor = typedArray.getColor(R.styleable.BadgeTextView_android_background, Color.WHITE)
        borderColor = typedArray.getColor(R.styleable.BadgeTextView_btv_border_color, Color.TRANSPARENT)
        borderWidth = typedArray.getDimension(R.styleable.BadgeTextView_btv_border_width, 0f)
        borderAlpha = typedArray.getFloat(R.styleable.BadgeTextView_btv_border_alpha, 1f)
        ctType = typedArray.getInt(R.styleable.BadgeTextView_btv_type, DEFAULT_FILL_TYPE)
        typedArray.recycle()
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        val strText = text?.toString()?.trim { it <= ' ' } ?: ""
        if (isHighLightMode && "" != strText) {
            val lp = layoutParams
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
            layoutParams = lp
            isHighLightMode = false
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        refreshBackgroundDrawable(w, h)
    }

    private fun refreshBackgroundDrawable(targetWidth: Int, targetHeight: Int) {
        if (targetWidth <= 0 || targetHeight <= 0) {
            return
        }
        val text = text ?: return
        if (text.length == 1) {
            val max = Math.max(targetWidth, targetHeight)
            val circle: ShapeDrawable
            val diameter = max - 2 * mShadowRadius
            val oval = OvalShadow(mShadowRadius, diameter)
            circle = ShapeDrawable(oval)
            ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, circle.paint)
            circle.paint.setShadowLayer(mShadowRadius.toFloat(), shadowXOffset.toFloat(), shadowYOffset.toFloat(), KEY_SHADOW_COLOR)
            circle.paint.color = backgroundColor
            background = circle
        } else if (text.length > 1) {
            val sr = SemiCircleRectDrawable()
            ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, sr.paint)
            sr.paint.setShadowLayer(mShadowRadius.toFloat(), shadowXOffset.toFloat(), shadowYOffset.toFloat(), KEY_SHADOW_COLOR)
            sr.paint.color = backgroundColor
            background = sr
        }

    }

    @JvmOverloads
    fun setBadgeCount(count: String, goneWhenZero: Boolean = false) {
        var temp = -1
        try {
            temp = Integer.parseInt(count)
        } catch (e: Exception) {

        }

        if (temp != -1) {
            setBadgeCount(temp, goneWhenZero)
        }
    }

    @JvmOverloads
    fun setBadgeCount(count: Int, goneWhenZero: Boolean = true) {
        when {
            count in 1..99 -> {
                text = count.toString()
                visibility = View.VISIBLE
            }
            count > 99 -> {
                text = "99+"
                visibility = View.VISIBLE
            }
            count <= 0 -> {
                text = "0"
                visibility = if (goneWhenZero) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }
    }

    fun clearHighLightMode() {
        isHighLightMode = false
        setBadgeCount(0)
    }

    /**
     * @param isDisplayInToolbarMenu
     */
    @JvmOverloads
    fun setHighLightMode(isDisplayInToolbarMenu: Boolean = false) {
        isHighLightMode = true
        val params = layoutParams
        params.width = dp2px(context, 8f)
        params.height = params.width
        if (isDisplayInToolbarMenu && params is FrameLayout.LayoutParams) {
            params.topMargin = dp2px(context, 8f)
            params.rightMargin = dp2px(context, 8f)
        }
        layoutParams = params
        val drawable = ShapeDrawable(OvalShape())
        ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, drawable.paint)
        drawable.paint.color = backgroundColor
        drawable.paint.isAntiAlias = true
        background = drawable
        text = ""
        visibility = View.VISIBLE
    }

    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
        refreshBackgroundDrawable(width, height)
    }

    private inner class OvalShadow internal constructor(shadowRadius: Int, private val mCircleDiameter: Int) : OvalShape() {
        private val mRadialGradient: RadialGradient
        private val mShadowPaint: Paint = Paint()

        init {
            mShadowRadius = shadowRadius
            mRadialGradient = RadialGradient((mCircleDiameter / 2).toFloat(), (mCircleDiameter / 2).toFloat(),
                    mShadowRadius.toFloat(), intArrayOf(FILL_SHADOW_COLOR, Color.TRANSPARENT), null, Shader.TileMode.CLAMP)
            mShadowPaint.shader = mRadialGradient
        }

        override fun draw(canvas: Canvas, paint: Paint) {
            val viewWidth = this@BadgeTextView.width
            val viewHeight = this@BadgeTextView.height
            canvas.drawCircle((viewWidth / 2).toFloat(), (viewHeight / 2).toFloat(), (mCircleDiameter / 2 + mShadowRadius).toFloat(), mShadowPaint)
            canvas.drawCircle((viewWidth / 2).toFloat(), (viewHeight / 2).toFloat(), (mCircleDiameter / 2).toFloat(), paint)
        }
    }

    internal inner class SemiCircleRectDrawable : Drawable() {
        val paint: Paint = Paint()
        private var rectF: RectF? = null

        init {
            paint.isAntiAlias = true
        }

        override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
            super.setBounds(left, top, right, bottom)
            if (rectF == null) {
                rectF = RectF((left + diffWH).toFloat(), (top + mShadowRadius + 4).toFloat(), (right - diffWH).toFloat(), (bottom - mShadowRadius - 4).toFloat())
            } else {
                rectF!!.set((left + diffWH).toFloat(), (top + mShadowRadius + 4).toFloat(), (right - diffWH).toFloat(), (bottom - mShadowRadius - 4).toFloat())
            }
        }

        override fun draw(canvas: Canvas) {
            var dr = (rectF!!.bottom * 0.4).toFloat()
            if (rectF!!.right < rectF!!.bottom) {
                dr = (rectF!!.right * 0.4).toFloat()
            }
            canvas.drawRoundRect(rectF!!, dr, dr, paint)
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }

        override fun getOpacity(): Int {
            return PixelFormat.TRANSPARENT
        }
    }

    companion object {

        private const val DEFAULT_FILL_TYPE = 0
        private const val SHADOW_RADIUS = 3.5f
        private const val FILL_SHADOW_COLOR = 0x55000000
        private const val KEY_SHADOW_COLOR = 0x55000000
        private const val X_OFFSET = 0f
        private const val Y_OFFSET = 1.75f

        fun dp2px(context: Context, dpValue: Float): Int {
            return try {
                val scale = context.resources.displayMetrics.density
                (dpValue * scale + 0.5f).toInt()
            } catch (e: Exception) {
                (dpValue + 0.5f).toInt()
            }

        }
    }

}
