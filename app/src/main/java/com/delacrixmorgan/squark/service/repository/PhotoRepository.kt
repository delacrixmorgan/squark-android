package com.delacrixmorgan.squark.service.repository

import com.delacrixmorgan.squark.model.photo.Photo
import com.delacrixmorgan.squark.model.photo.PhotoDtoToModelMapper
import com.delacrixmorgan.squark.service.api.UnsplashApi
import com.delacrixmorgan.squark.service.network.apiRequest
import com.delacrixmorgan.squark.service.network.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val api: UnsplashApi,
    private val photoDtoToModelMapper: PhotoDtoToModelMapper
) {
//    fun getPhotos(query: String): Flow<Result<List<Photo>, Exception>>{
//        return flow {
//            apiRequest { api.getPhotos(query) }.map {
//
//            }.fold(
//
//            )
//        }.flowOn(Dispatchers.Default)
//    }
}