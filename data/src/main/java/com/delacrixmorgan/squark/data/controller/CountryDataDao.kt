package com.delacrixmorgan.squark.data.controller

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.delacrixmorgan.squark.data.model.Country

/**
 * CountryDataDao
 * squark-android
 *
 * Created by Delacrix Morgan on 21/06/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

@Dao
interface CountryDataDao {
    @Query("SELECT * from Country")
    fun getCountries(): List<Country>

    @Insert(onConflict = REPLACE)
    fun insertCountry(country: Country)

    @Query("DELETE from Country")
    fun deleteCountries()
}