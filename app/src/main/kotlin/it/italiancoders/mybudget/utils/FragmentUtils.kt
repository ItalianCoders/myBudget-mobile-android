/*
 * Project: myBudget-mobile-android
 * File: FragmentUtils.kt
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

    fun replace(activity: Activity?, fragment: Fragment, containerResId: Int = R.id.container, animationType: AnimationType = AnimationType.SLIDE) {
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