package com.delacrixmorgan.squark.deprecrated.network;

import com.delacrixmorgan.squark.deprecrated.wrapper.APIWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Delacrix Morgan on 05/09/2017.
 */

public interface InterfaceAPI {
    @GET("latest")
    Call<APIWrapper> updateRates(
            @Query("base") String baseCurrency
    );
}
