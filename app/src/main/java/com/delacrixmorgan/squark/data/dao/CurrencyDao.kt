package com.delacrixmorgan.squark.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.delacrixmorgan.squark.models.Country
import com.delacrixmorgan.squark.models.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT * from Currency")
    fun getCurrencies(): Flow<List<Country>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(currency: Currency)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateCurrency(currency: Currency)

    @Query("DELETE from Currency")
    fun deleteCurrencies()
}