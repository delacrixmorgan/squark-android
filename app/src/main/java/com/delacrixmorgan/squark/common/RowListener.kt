package com.delacrixmorgan.squark.common

interface RowListener {
    fun onRowClicked(position: Int)
    fun onSwipeLeft(multiplier: Double)
    fun onSwipeRight(multiplier: Double)
}