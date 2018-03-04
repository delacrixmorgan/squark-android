package com.delacrixmorgan.squark.launch

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by Delacrix Morgan on 04/03/2017.
 **/

open class OnSwipeTouch(context: Context) : View.OnTouchListener {
    private val mGestureDetector: GestureDetector
    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100

    private var isScrolling = false
    private var scrollDirection: ScrollDirection = ScrollDirection.LEFT

    enum class ScrollDirection {
        LEFT, RIGHT
    }

    init {
        mGestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        v.performClick()

        if (mGestureDetector.onTouchEvent(event)) {
            return true
        }

        if (event.action == MotionEvent.ACTION_UP) {
            if (isScrolling) {
                isScrolling = false

                when (scrollDirection) {
                    ScrollDirection.LEFT -> onSwipeLeft()
                    ScrollDirection.RIGHT -> onSwipeRight()
                }
            }
        }

        return false
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            onSingleTap()
            return true
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x
            var result = false

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight()
                    } else {
                        onSwipeLeft()
                    }
                    result = true
                }
            } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeBottom()
                } else {
                    onSwipeTop()
                }
                result = true
            }

            return result
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            isScrolling = true

            if (e1.x > e2.x) {
                scrollDirection = ScrollDirection.LEFT
                onSwipingLeft()
            }

            if (e1.x < e2.x) {
                scrollDirection = ScrollDirection.RIGHT
                onSwipingRight()
            }
            return true
        }
    }

    open fun onSingleTap() {}

    open fun onSwipeRight() {}

    open fun onSwipeLeft() {}

    open fun onSwipeTop() {}

    open fun onSwipeBottom() {}

    open fun onSwipingLeft() {}

    open fun onSwipingRight() {}
}