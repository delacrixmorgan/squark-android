package com.delacrixmorgan.squark.network;

import com.delacrixmorgan.squark.wrapper.RestWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Delacrix Morgan on 05/09/2017.
 */

public interface InterfaceAPI {
    @GET("latest")
    Call<RestWrapper> updateRates(
            @Query("base") String baseCurrency
    );
}
