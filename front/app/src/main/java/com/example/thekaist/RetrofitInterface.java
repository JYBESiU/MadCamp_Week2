package com.example.thekaist;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

    @POST("/online")
    Call<List<UserInfo>> executeOnline();

    @POST("/changeplay")
    Call<Void> executeChangePlay(@Body HashMap<String, String> map);

    @POST("/userinfo")
    Call<UserInfo> executeUserinfo(@Body HashMap<String, String> map);

    @POST("/change")
    Call<Void> executeChange(@Body HashMap<String, String> map);

    @POST("/logout")
    Call<Void> executeLogout(@Body HashMap<String, String> map);

    @POST("/rank")
    Call<List<UserInfo>> executeRank();

    @POST("/signupkakao")
    Call<Void> executeKakaosignup(@Body HashMap<String, String> map);

    @POST("/getbattle")
    Call<List<battleinfo>> executeHistory(@Body HashMap<String, String> map);

    @POST("/winLose")
    Call<Void> executeWinLose(@Body HashMap<String, String> map);
}
