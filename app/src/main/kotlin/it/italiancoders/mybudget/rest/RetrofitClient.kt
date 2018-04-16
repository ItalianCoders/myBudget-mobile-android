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
            val request = chain.request().newBuilder().addHeader("x-auth-token", Config.accessToken.orEmpty()).build()
            chain.proceed(request)
        }

        return Retrofit.Builder().client(httpClient.build()).baseUrl(baseUrl).addConverterFactory(converterFactory).build()
    }
}