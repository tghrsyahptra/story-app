package com.tghrsyahptra.storyapp.data.api

import com.tghrsyahptra.storyapp.data.response.AddStoryResponse
import com.tghrsyahptra.storyapp.data.response.LoginResponse
import com.tghrsyahptra.storyapp.data.response.RegisterResponse
import com.tghrsyahptra.storyapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    /**
     * Mendapatkan daftar cerita dari server.
     *
     * @param bearer Token otentikasi dalam format Bearer.
     * @return Objek [Call] yang mengembalikan [StoryResponse].
     */
    @GET("stories")
    fun getStories(
        @Header("Authorization") bearerToken: String?
    ): Call<StoryResponse>

    /**
     * Mengunggah cerita baru ke server.
     *
     * @param bearerToken Token otentikasi dalam format Bearer.
     * @param file Berkas gambar yang akan diunggah.
     * @param description Deskripsi cerita.
     * @return Objek [Call] yang mengembalikan [AddStoryResponse].
     */
    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") bearerToken: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody?
    ): Call<AddStoryResponse>

    /**
     * Mendaftarkan pengguna baru ke aplikasi.
     *
     * @param name Nama pengguna.
     * @param email Alamat email pengguna.
     * @param password Kata sandi pengguna.
     * @return Objek [Call] yang mengembalikan [RegisterResponse].
     */
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<RegisterResponse>

    /**
     * Melakukan proses login pengguna.
     *
     * @param email Alamat email pengguna.
     * @param password Kata sandi pengguna.
     * @return Objek [Call] yang mengembalikan [LoginResponse].
     */
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<LoginResponse>
}