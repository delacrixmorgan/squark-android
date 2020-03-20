package com.delacrixmorgan.squark.data.dao

import androidx.room.*
import com.delacrixmorgan.squark.data.model.Country

@Dao
interface CountryDataDao {
    @Query("SELECT * from Country")
    suspend fun getCountries(): List<Country>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: Country)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateCountry(country: Country)

    @Query("DELETE from Country")
    suspend fun deleteCountries()
}