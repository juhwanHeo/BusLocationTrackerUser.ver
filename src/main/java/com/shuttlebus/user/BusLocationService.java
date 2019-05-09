package com.shuttlebus.user;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BusLocationService {
//    @FormUrlEncoded
//    @POST("/get/getBusInfo.php")
//    List<Bus> getPostLocationData(@Field("name") String name);

    @FormUrlEncoded
    @POST("/get/getBusInfo.php")
    Call<RetrofitRepo> getPostLocationData(@Field("arrived") String arrived,@Field("LatLon") String LatLon);


}
