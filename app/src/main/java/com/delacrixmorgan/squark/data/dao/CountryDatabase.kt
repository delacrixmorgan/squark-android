package com.delacrixmorgan.squark.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.delacrixmorgan.squark.data.model.Country

@Database(entities = [(Country::class)], version = 1, exportSchema = false)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDataDao(): CountryDataDao

    companion object {
        private var INSTANCE: CountryDatabase? = null

        fun getInstance(context: Context): CountryDatabase? {
            if (INSTANCE == null) {
                synchronized(CountryDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            CountryDatabase::class.java,
                            "country.db"
                        )
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