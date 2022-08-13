package com.delacrixmorgan.squark.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.delacrixmorgan.squark.model.currency.Currency

@Database(
    entities = [(Currency::class)],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    companion object {
        fun getDatabase(
            appContext: Context,
            databaseName: String
        ): AppDatabase = Room.databaseBuilder(appContext, AppDatabase::class.java, databaseName)
            .fallbackToDestructiveMigration()
            .build()
    }
}