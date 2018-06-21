package com.delacrixmorgan.squark.data.controller

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.delacrixmorgan.squark.data.model.Country

/**
 * CountryDatabase
 * squark-android
 *
 * Created by Delacrix Morgan on 21/06/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

@Database(entities = [(Country::class)], version = 1)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDataDao(): CountryDataDao

    companion object {
        private var INSTANCE: CountryDatabase? = null

        fun getInstance(context: Context): CountryDatabase? {
            if (INSTANCE == null) {
                synchronized(CountryDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            CountryDatabase::class.java,
                            "country.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}