package it.italiancoders.mybudget.utils

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import it.italiancoders.mybudget.R

/**
 * @author fattazzo
 *         <p/>
 *         date: 25/01/18
 */
object FragmentUtils {

    enum class AnimationType { NONE, SLIDE, FADE_IN, FADE_OUT }

    fun replace(activity: Activity?, fragment: Fragment, containerResId: Int = R.id.container, animationType: AnimationType = AnimationType.NONE) {
        val transaction = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
        setReplaceAnimation(transaction, animationType)
        transaction.replace(containerResId, fragment, fragment.javaClass.simpleName).addToBackStack(null).commit()
        activity.supportFragmentManager.executePendingTransactions()
    }

    fun add(activity: Activity?, fragment: Fragment, containerResId: Int = R.id.container, animationType: AnimationType = AnimationType.NONE) {
        val transaction = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
        setAddAnimation(transaction, animationType)
        transaction.add(containerResId, fragment, fragment.javaClass.simpleName).addToBackStack(null).commit()
        activity.supportFragmentManager.executePendingTransactions()
    }

    private fun setAddAnimation(transaction: FragmentTransaction, animationType: AnimationType) {
        when (animationType) {
            AnimationType.SLIDE -> transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            AnimationType.FADE_IN -> transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
            AnimationType.FADE_OUT -> transaction.setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in)
            else -> {
                // No animation
            }
        }
    }

    private fun setReplaceAnimation(transaction: FragmentTransaction, animationType: AnimationType) {
        when (animationType) {
            AnimationType.SLIDE -> transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            AnimationType.FADE_IN -> transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            AnimationType.FADE_OUT -> transaction.setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in)
            else -> {
                // No animation
            }
        }
    }
}