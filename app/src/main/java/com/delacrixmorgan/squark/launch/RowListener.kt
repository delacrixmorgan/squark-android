package com.delacrixmorgan.squark.launch

/**
 * RowListener
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

interface RowListener {
    fun onClick(position: Int)
    fun onSwipeLeft(position: Int)
    fun onSwipeRight(position: Int)
    fun onSwipingLeft(position: Int)
    fun onSwipingRight(position: Int)
}