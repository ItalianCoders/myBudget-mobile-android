package it.italiancoders.mybudget.fragment.login

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Align.CENTER
import android.graphics.Paint.Style.FILL
import android.graphics.Path
import android.graphics.Rect
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent.ACTION_UP
import android.view.View
import it.italiancoders.mybudget.R
import java.lang.Math.*

class LoginButton : View {
    private val loginButtonPath = Path()
    private val signUpButtonPath = Path()
    private val r = Rect()
    private var widthButton: Int = 0
    private var heightButton: Int = 0
    private var buttonTop: Int = 0
    private var buttonBottom: Int = 0
    private var loginButtonPaint: Paint? = null
    private var signUpButtonPaint: Paint? = null
    private var startRight: Int = 0
    private var currentY: Float = 0.toFloat()
    private var buttonCenter: Int = 0
    private var currentX: Float = 0.toFloat()
    private var currentRight: Float = 0.toFloat()
    private var currentBottomY: Float = 0.toFloat()
    private var currentBottomX: Float = 0.toFloat()
    private var currentArcX: Float = 0.toFloat()

    private var loginPaint: Paint? = null
    private var orPaint: Paint? = null
    private var signUpPaint: Paint? = null

    private var currentLoginX: Float = 0.toFloat()
    private var currentSignUpTextX: Float = 0.toFloat()
    private var largeTextSize: Float = 0.toFloat()
    private var smallTextSize: Float = 0.toFloat()
    private var currentLoginY: Float = 0.toFloat()
    private var currentLeft: Float = 0.toFloat()
    private var signUpOrX: Float = 0.toFloat()
    private var isLogin = false
    private var currentSignUpTextY: Float = 0.toFloat()
    private var currentSignUpX: Float = 0.toFloat()
    private var currentBottomSignUpX: Float = 0.toFloat()
    private var startLeft: Int = 0

    private var callback: OnButtonSwitchedListener? = null

    private var startSignUpTextX: Float = 0.toFloat()
    private var startSignUpTextY: Float = 0.toFloat()
    private var startLoginX: Float = 0.toFloat()
    private var startLoginY: Float = 0.toFloat()
    private var loginOrX: Float = 0.toFloat()
    private var loginButtonOutline: Rect? = null
    private var signUpButtonOutline: Rect? = null

    private var onSignUpListener: OnSignUpListener? = null
    private var onLoginListener: OnLoginListener? = null
    private var loginTextOutline: Rect? = null
    private var signUpTextOutline: Rect? = null

    private val buttonHeight: Int
        get() = resources.getDimensionPixelOffset(R.dimen.login_bottom_height)

    private val bottomMargin: Int
        get() = resources.getDimensionPixelOffset(R.dimen.login_bottom_margin)

    private val buttonActionBottonDistance: Int
        get() = resources.getInteger(R.integer.login_action_bottom_distance)

    private val startButtonRight: Float
        get() = resources.getDimensionPixelOffset(R.dimen.login_bottom_width).toFloat()


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        loginButtonPaint = Paint()
        loginButtonPaint!!.color = ContextCompat.getColor(context, R.color.welcome_login)
        loginButtonPaint!!.style = FILL

        signUpButtonPaint = Paint()
        signUpButtonPaint!!.color = ContextCompat.getColor(context, R.color.welcome_signup)
        signUpButtonPaint!!.style = FILL

        val paint2 = Paint()
        paint2.color = ContextCompat.getColor(context, android.R.color.black)
        paint2.style = FILL

        loginPaint = Paint()
        loginPaint!!.color = ContextCompat.getColor(context, android.R.color.black)
        loginPaint!!.textAlign = CENTER
        loginPaint!!.textSize = dpToPixels(44)

        orPaint = Paint()
        orPaint!!.color = ContextCompat.getColor(context, android.R.color.black)
        orPaint!!.textSize = dpToPixels(16)

        signUpPaint = Paint()
        signUpPaint!!.color = ContextCompat.getColor(context, android.R.color.black)
        signUpPaint!!.textSize = dpToPixels(16)
        signUpPaint!!.textAlign = CENTER
        //        signUpPaint.setAlpha(255);
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthButton = w
        heightButton = h
        buttonTop = heightButton - bottomMargin - buttonHeight
        buttonBottom = heightButton - bottomMargin
        startRight = startButtonRight.toInt()

        buttonCenter = (buttonBottom - buttonTop) / 2 + buttonTop

        currentSignUpX = widthButton.toFloat()
        currentBottomSignUpX = widthButton.toFloat()

        loginOrX = dpToPixels(32)

        currentY = buttonCenter.toFloat()
        currentBottomY = buttonBottom.toFloat()
        currentRight = startRight.toFloat()
        currentLeft = (widthButton - startRight).toFloat()
        startLeft = widthButton - startRight

        signUpPaint!!.getTextBounds(getString(R.string.sign_up), 0, 7, r)

        currentLoginX = dpToPixels(92)
        val signUpWidth = r.right
        currentSignUpTextX = widthButton.toFloat() - (signUpWidth / 2).toFloat() - dpToPixels(32)

        signUpPaint!!.getTextBounds(getString(R.string.login), 0, 5, r)

        loginTextOutline = Rect()
        signUpTextOutline = Rect()
        loginPaint!!.getTextBounds(getString(R.string.login), 0, 5, loginTextOutline)
        loginPaint!!.getTextBounds(getString(R.string.sign_up), 0, 7, signUpTextOutline)

        loginTextOutline!!.offset(widthButton / 2 - (loginTextOutline!!.right + loginTextOutline!!.left) / 2, buttonTop - buttonActionBottonDistance)
        signUpTextOutline!!.offset(widthButton / 2 - (signUpTextOutline!!.right + signUpTextOutline!!.left) / 2, buttonTop - buttonActionBottonDistance)

        val loginWidth = r.right
        val or = getString(R.string.or).toUpperCase()
        orPaint!!.getTextBounds(or, 0, or.length, r)
        val margin = currentLoginX - loginWidth / 2 - dpToPixels(32) - r.right.toFloat()
        signUpOrX = widthButton.toFloat() - signUpWidth.toFloat() - dpToPixels(32) - r.right.toFloat() - margin

        currentLoginY = buttonCenter + dpToPixels(8)
        currentSignUpTextY = buttonCenter + dpToPixels(8)
        largeTextSize = dpToPixels(44)
        smallTextSize = dpToPixels(16)

        startLoginX = currentLoginX
        startLoginY = currentLoginY
        startSignUpTextX = currentSignUpTextX
        startSignUpTextY = currentSignUpTextY

        loginButtonPath.moveTo(0f, buttonBottom.toFloat())
        loginButtonPath.lineTo(currentRight, buttonBottom.toFloat())
        loginButtonPath.lineTo(currentRight, buttonTop.toFloat())
        loginButtonPath.lineTo(0f, buttonTop.toFloat())
        loginButtonPath.close()

        signUpButtonPath.moveTo(widthButton.toFloat(), buttonBottom.toFloat())
        signUpButtonPath.lineTo(currentLeft, buttonBottom.toFloat())
        signUpButtonPath.lineTo(currentLeft, buttonTop.toFloat())
        signUpButtonPath.lineTo(widthButton.toFloat(), buttonTop.toFloat())
        signUpButtonPath.close()

        loginButtonOutline = Rect(
                0,
                buttonTop,
                currentRight.toInt() + buttonHeight / 2,
                buttonBottom)

        signUpButtonOutline = Rect(
                (widthButton.toFloat() - currentRight - (buttonHeight / 2).toFloat()).toInt(),
                buttonTop,
                widthButton,
                buttonBottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isLogin) {
            canvas.drawText(getString(R.string.sign_up), (widthButton / 2).toFloat(), (buttonTop - buttonActionBottonDistance).toFloat(), signUpPaint!!)
        } else {
            canvas.drawText(getString(R.string.login), (widthButton / 2).toFloat(), (buttonTop - buttonActionBottonDistance).toFloat(), loginPaint!!)
        }

        if (isLogin) {
            canvas.drawPath(loginButtonPath, loginButtonPaint!!)
            canvas.drawArc(
                    currentRight - buttonHeight / 2 + currentArcX,
                    buttonTop.toFloat(),
                    currentRight + buttonHeight / 2 - currentArcX,
                    buttonBottom.toFloat(),
                    0f,
                    360f,
                    false,
                    loginButtonPaint!!)

            canvas.drawText(getString(R.string.or), loginOrX, buttonCenter + dpToPixels(8), orPaint!!)
            canvas.drawText(getString(R.string.login), currentLoginX, currentLoginY, loginPaint!!)
        } else {
            canvas.drawPath(signUpButtonPath, signUpButtonPaint!!)
            canvas.drawArc(
                    currentLeft - buttonHeight / 2 + currentArcX,
                    buttonTop.toFloat(),
                    currentLeft + buttonHeight / 2 - currentArcX,
                    buttonBottom.toFloat(),
                    0f,
                    360f,
                    false,
                    signUpButtonPaint!!)

            canvas.drawText(getString(R.string.or), signUpOrX, buttonCenter + dpToPixels(8), orPaint!!)

            canvas.drawText(getString(R.string.sign_up), currentSignUpTextX, currentSignUpTextY, signUpPaint!!)
        }
    }

    fun startAnimation() {
        val start = startButtonRight
        val animator = ObjectAnimator.ofFloat(0f, 1f)
        //        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener { animation ->
            val fraction = animation.animatedValue as Float
            val currentAngle = fraction * (PI.toFloat() / 2) // in radians

            val gone = (widthButton - start) * fraction
            currentRight = start + gone
            currentLeft = startLeft - gone

            // fade out sign up text to 0
            if (isLogin) {
                signUpPaint!!.alpha = (255 - 255 * fraction).toInt() // fade out sign up large text
            } else {
                loginPaint!!.alpha = (255 - 255 * fraction).toInt() // fade out login large text
            }

            if (orPaint!!.alpha != 0) {
                orPaint!!.alpha = 0
            }

            // move login text to center and scale
            if (isLogin) {
                currentLoginX = startLoginX + (widthButton / 2 - startLoginX) * fraction
                currentLoginY = startLoginY - (startLoginY - buttonTop.toFloat() - buttonActionBottonDistance.toFloat()) * fraction
                loginPaint!!.textSize = smallTextSize + (largeTextSize - smallTextSize) * fraction
            } else {
                currentSignUpTextX = startSignUpTextX - (startSignUpTextX - widthButton / 2) * fraction
                currentSignUpTextY = startSignUpTextY - (startSignUpTextY - buttonTop.toFloat() - buttonActionBottonDistance.toFloat()) * fraction
                signUpPaint!!.textSize = smallTextSize + (largeTextSize - smallTextSize) * fraction
            }

            currentArcX = (fraction * dpToPixels(37)).toInt().toFloat() // just hardcoded value

            val y = tan(currentAngle.toDouble()) * currentRight // goes from ~ 0 to 4451
            val realY = (buttonTop - y).toFloat() // goes ~ from 1234 to -1243
            currentY = max(0f, realY) // goes ~ from 1234 to 0


            val realBottomY = (buttonBottom + y).toFloat()
            currentBottomY = min(heightButton.toFloat(), realBottomY)


            if (currentY == 0f) { // if reached top, start moving to the right
                val cot = 1.0f / tan(currentAngle.toDouble())
                currentX = ((y - buttonTop) * cot).toFloat()
                currentSignUpX = widthButton - currentX
            }

            if (currentBottomY == heightButton.toFloat()) {
                val cot = 1.0f / tan(currentAngle.toDouble())
                currentBottomX = ((y - bottomMargin) * cot).toFloat()
                currentBottomSignUpX = widthButton - currentBottomX
            }

            if (currentAngle == PI.toFloat() / 2) {
                currentX = currentRight
                currentBottomX = currentRight
                currentY = 0f
                currentBottomY = heightButton.toFloat()
            }

            if (isLogin) {
                loginButtonPath.reset()
                loginButtonPath.moveTo(0f, buttonBottom.toFloat())
                loginButtonPath.lineTo(currentRight, buttonBottom.toFloat())
                loginButtonPath.lineTo(currentRight, buttonTop.toFloat())

                loginButtonPath.lineTo(currentX, currentY)
                loginButtonPath.lineTo(0f, currentY)

                // bottom reveal
                loginButtonPath.lineTo(0f, currentBottomY)
                loginButtonPath.lineTo(currentBottomX, currentBottomY)
                loginButtonPath.lineTo(currentRight, buttonBottom.toFloat())
            } else {
                signUpButtonPath.reset()
                signUpButtonPath.moveTo(widthButton.toFloat(), buttonBottom.toFloat())
                signUpButtonPath.lineTo(currentLeft, buttonBottom.toFloat())
                signUpButtonPath.lineTo(currentLeft, buttonTop.toFloat())

                signUpButtonPath.lineTo(currentSignUpX, currentY)
                signUpButtonPath.lineTo(widthButton.toFloat(), currentY)

                // bopttom reveal
                signUpButtonPath.lineTo(widthButton.toFloat(), currentBottomY)
                signUpButtonPath.lineTo(currentBottomSignUpX, currentBottomY)
                signUpButtonPath.lineTo(currentLeft, buttonBottom.toFloat())
            }

            currentX = 0f
            currentSignUpX = widthButton.toFloat()
            currentBottomX = 0f
            currentBottomSignUpX = widthButton.toFloat()
            invalidate()
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                orPaint!!.alpha = 125
                signUpPaint!!.alpha = 255
                signUpPaint!!.textSize = dpToPixels(16)
                currentArcX = 0f

                currentRight = startButtonRight.toInt().toFloat()
                currentLeft = (widthButton - startButtonRight.toInt()).toFloat()

                isLogin = !isLogin

                if (isLogin) {
                    currentLoginX = startLoginX
                    currentLoginY = startLoginY
                    loginPaint!!.alpha = 255
                    loginPaint!!.textSize = dpToPixels(16)
                    signUpPaint!!.alpha = 255
                    signUpPaint!!.textSize = dpToPixels(44)
                }

                currentSignUpTextX = startSignUpTextX
                currentSignUpTextY = startSignUpTextY

                val hideButton = startRight + buttonHeight / 2
                if (!isLogin) {
                    currentLeft += hideButton.toFloat()
                } else {
                    currentRight -= hideButton.toFloat()
                }


                //move texts
                if (!isLogin) {
                    signUpOrX += hideButton.toFloat()
                    currentSignUpTextX += hideButton.toFloat()
                } else {
                    loginOrX -= hideButton.toFloat()
                    currentLoginX -= hideButton.toFloat()
                }
                val hiddenButtonLeft = currentLeft
                val hiddenButtonRight = currentRight
                val endSignUpOrX = signUpOrX
                val endSignUpTextX = currentSignUpTextX
                val endLoginOrX = loginOrX
                val endLoginTextX = currentLoginX

                // reset paths
                signUpButtonPath.reset()
                signUpButtonPath.moveTo(widthButton.toFloat(), buttonBottom.toFloat())
                signUpButtonPath.lineTo(currentLeft, buttonBottom.toFloat())
                signUpButtonPath.lineTo(currentLeft, buttonTop.toFloat())
                signUpButtonPath.lineTo(widthButton.toFloat(), buttonTop.toFloat())
                signUpButtonPath.close()

                loginButtonPath.reset()
                loginButtonPath.moveTo(0f, buttonBottom.toFloat())
                loginButtonPath.lineTo(currentRight, buttonBottom.toFloat())
                loginButtonPath.lineTo(currentRight, buttonTop.toFloat())
                loginButtonPath.lineTo(0f, buttonTop.toFloat())
                loginButtonPath.close()

                callback!!.onButtonSwitched(isLogin)

                val buttonBounce = ObjectAnimator.ofInt(0, hideButton).setDuration(500)
                buttonBounce.startDelay = 300
                buttonBounce.interpolator = MyBounceInterpolator(.2, 7.0)
                buttonBounce.addUpdateListener { a ->
                    val v = a.animatedValue as Int

                    if (!isLogin) {
                        currentLeft = hiddenButtonLeft - v

                        signUpOrX = endSignUpOrX - v
                        currentSignUpTextX = endSignUpTextX - v

                        signUpButtonPath.reset()
                        signUpButtonPath.moveTo(widthButton.toFloat(), buttonBottom.toFloat())
                        signUpButtonPath.lineTo(currentLeft, buttonBottom.toFloat())
                        signUpButtonPath.lineTo(currentLeft, buttonTop.toFloat())
                        signUpButtonPath.lineTo(widthButton.toFloat(), buttonTop.toFloat())
                        signUpButtonPath.close()
                    } else {
                        currentRight = hiddenButtonRight + v

                        loginOrX = endLoginOrX + v
                        currentLoginX = endLoginTextX + v

                        loginButtonPath.reset()
                        loginButtonPath.moveTo(0f, buttonBottom.toFloat())
                        loginButtonPath.lineTo(currentRight, buttonBottom.toFloat())
                        loginButtonPath.lineTo(currentRight, buttonTop.toFloat())
                        loginButtonPath.lineTo(0f, buttonTop.toFloat())
                        loginButtonPath.close()
                    }
                    invalidate()
                }
                buttonBounce.start()

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })

        animator.start()
    }

    private fun dpToPixels(dp: Int): Float {
        return resources.displayMetrics.density * dp
    }

    fun setOnButtonSwitched(callback: OnButtonSwitchedListener) {
        this.callback = callback
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        setOnTouchListener { v, event ->
            val x = event.x.toInt()
            val y = event.y.toInt()
            if (isLogin && loginButtonOutline!!.contains(x, y)) {
                if (event.action == ACTION_UP) {
                    l!!.onClick(v)
                }
                return@setOnTouchListener true
            } else if (!isLogin && loginTextOutline!!.contains(x, y)) {
                if (event.action == ACTION_UP) {
                    onLoginListener!!.login()
                }
                return@setOnTouchListener true
            } else if (isLogin && signUpTextOutline!!.contains(x, y)) {
                if (event.action == ACTION_UP) {
                    onSignUpListener!!.signUp()
                }
                return@setOnTouchListener true
            } else {
                if (!isLogin && signUpButtonOutline!!.contains(x, y)) {
                    if (event.action == ACTION_UP) {
                        l!!.onClick(v)
                    }
                    return@setOnTouchListener true
                } else {
                    return@setOnTouchListener false
                }
            }
        }
    }

    fun setOnSignUpListener(listener: OnSignUpListener) {
        onSignUpListener = listener
    }

    fun setOnLoginListener(listener: OnLoginListener) {
        onLoginListener = listener
    }

    private fun getString(@StringRes stringId: Int): String {
        return context.getString(stringId).toUpperCase()
    }

    fun getOnLoginListener() : OnLoginListener? = onLoginListener
}
