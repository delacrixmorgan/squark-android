package com.delacrixmorgan.squark.common

interface RowListener {
    fun onClick(position: Int)
    fun onSwipeLeft(multiplier: Double)
    fun onSwipeRight(multiplier: Double)
}