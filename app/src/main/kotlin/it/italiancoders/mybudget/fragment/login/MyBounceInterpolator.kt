package it.italiancoders.mybudget.fragment.login

internal class MyBounceInterpolator(amp: Double, freq: Double) : android.view.animation.Interpolator {
    private var amplitude = 1.0
    private var frequency = 10.0

    init {
        amplitude = amp
        frequency = freq
    }

    override fun getInterpolation(time: Float): Float {
        return (-1.0 * Math.pow(Math.E, -time / amplitude) * Math.cos(frequency * time) + 1).toFloat()
    }
}