package com.example.nutrigrow.api

import com.example.nutrigrow.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // --- IMPORTANT SECURITY NOTE ---
    // Using a hardcoded IP is fine for local development.
    // For production, this MUST be a proper domain name (e.g., "https://api.nutrigrow.com/").
    // The "http://" protocol is insecure. Production apps must use "https://" (HTTPS).
    const val BASE_URL = "http://192.168.18.42:8888/"

    // You'll need a way to get the auth token, e.g., from SharedPreferences.
    // For now, this is a placeholder.
    private var AUTH_TOKEN: String? = null

    fun setAuthToken(token: String?) {
        AUTH_TOKEN = token
    }
    // ------------------------------------


    // 1. Create a logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Only log the body of requests in debug builds, not in production.
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    // 2. Create an authentication interceptor
    private val authInterceptor = okhttp3.Interceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        AUTH_TOKEN?.let { token ->
            // Add the "Bearer" token to the header if it exists
            requestBuilder.header("Authorization", "Bearer $token")
        }

        chain.proceed(requestBuilder.build())
    }

    // 3. Build the OkHttpClient, adding the interceptors
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
        .readTimeout(30, TimeUnit.SECONDS)    // Set read timeout
        .writeTimeout(30, TimeUnit.SECONDS)   // Set write timeout
        .build()

    // 4. Build the Retrofit instance using the custom OkHttpClient
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // <-- Use our custom client
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}