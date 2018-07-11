package com.delacrixmorgan.squark.common

interface SwipeGesture {
    fun onSwipeUp()
    fun onSwipeDown()
    fun onLeftTap() {}
    fun onRightTap() {}
    fun onLongPressed() {}
}