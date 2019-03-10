package com.example.myapplication.Requests;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GarbageDetail {
    @GET("garbage_detail.php")
    Call<com.example.myapplication.Responses.GarbageDetail> call(@Query("garbage_name") String garbage_name,@Query("garbage_location") String garbage_lcation);
}
