package com.delacrixmorgan.squark.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.delacrixmorgan.squark.models.Country

@Database(
    entities = [(Country::class)],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countryDataDao(): CountryDataDao

    companion object {
        fun getDatabase(
            appContext: Context,
            databaseName: String
        ): AppDatabase = Room.databaseBuilder(appContext, AppDatabase::class.java, databaseName)
            .fallbackToDestructiveMigration()
            .build()
    }
}