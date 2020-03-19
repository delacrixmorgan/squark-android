package com.delacrixmorgan.squark.data.dao

import androidx.room.*
import com.delacrixmorgan.squark.data.model.Country

@Dao
interface CountryDataDao {
    @Query("SELECT * from Country")
    fun getCountries(): List<Country>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountry(country: Country)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateCountry(country: Country)

    @Query("DELETE from Country")
    fun deleteCountries()
}