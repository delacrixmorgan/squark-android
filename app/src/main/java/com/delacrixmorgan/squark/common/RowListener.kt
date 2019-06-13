package com.delacrixmorgan.squark.common

/**
 * RowListener
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

interface RowListener {
    fun onClick(position: Int)
    fun onSwipeLeft(multiplier: Double)
    fun onSwipeRight(multiplier: Double)
}