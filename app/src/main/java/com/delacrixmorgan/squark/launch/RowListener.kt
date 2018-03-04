package com.delacrixmorgan.squark.launch

/**
 * Created by Delacrix Morgan on 04/03/2018.
 **/

interface RowListener {
    fun onClick(position: Int)
    fun onSwipeLeft(position: Int)
    fun onSwipeRight(position: Int)
    fun onSwipingLeft(position: Int)
    fun onSwipingRight(position: Int)
}