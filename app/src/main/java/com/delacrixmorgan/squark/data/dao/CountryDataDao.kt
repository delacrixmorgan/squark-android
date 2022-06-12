package com.delacrixmorgan.squark.data.dao

import androidx.room.*
import com.delacrixmorgan.squark.models.Country
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDataDao {
    @Query("SELECT * from Country")
    fun getCountries(): Flow<List<Country>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountry(country: Country)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateCountry(country: Country)

    @Query("DELETE from Country")
    fun deleteCountries()
}