package com.second_year.finalproject.Api.Interfaces

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("RegisterUser.php")
    fun createUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("UserLogin.php")
    fun userLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("UpdatePassword.php")
    fun updatePassword(
        @Field("username") username: String,
        @Field("newPassword") newPassword: String
    ): Call<ResponseBody>
}
