/*
 * Project: myBudget-mobile-android
 * File: RetrofitClient.kt
 *
 * Created by fattazzo
 * Copyright © 2018 Gianluca Fattarsi. All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package it.italiancoders.mybudget.rest

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import it.italiancoders.mybudget.Config
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * @author fattazzo
 *         <p/>
 *         date: 28/03/18
 */
object RetrofitClient {

    private var retrofit: Retrofit? = null

    private val converterFactory: Converter.Factory by lazy {
        val jacksonMapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        JacksonConverterFactory.create(jacksonMapper)
    }

    private val httpClientBuilder: OkHttpClient.Builder
        get() = OkHttpClient.Builder().connectTimeout(50, TimeUnit.SECONDS).writeTimeout(50, TimeUnit.SECONDS).readTimeout(50, TimeUnit.SECONDS)

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder().client(httpClientBuilder.build()).baseUrl(baseUrl).addConverterFactory(converterFactory).build()
        }
        return retrofit!!
    }

    fun getSecureClient(baseUrl: String): Retrofit {

        val httpClient = httpClientBuilder
        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                    .addHeader("x-auth-token", Config.accessToken.orEmpty())
                    .addHeader("Accept-Language",Config.locale.language)
                    .build()
            chain.proceed(request)
        }

        return Retrofit.Builder().client(httpClient.build()).baseUrl(baseUrl).addConverterFactory(converterFactory).build()
    }
}