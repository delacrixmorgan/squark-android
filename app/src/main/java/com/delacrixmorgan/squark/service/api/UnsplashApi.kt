package com.delacrixmorgan.squark.service.api

import com.delacrixmorgan.squark.model.photo.PhotoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {
    @GET("search/photos")
    suspend fun getPhotos(
        @Query("query") query: String
    ): Response<PhotoDto>
}