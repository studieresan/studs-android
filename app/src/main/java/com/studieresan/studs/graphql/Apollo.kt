package com.studieresan.studs.graphql

import android.content.Context
import android.os.Looper
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import com.studieresan.studs.data.StudsPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import type.CustomType
import java.text.ParseException
import java.time.LocalDateTime
import kotlin.reflect.typeOf

private var instance: ApolloClient? = null

fun apolloClient(context: Context): ApolloClient {

    /*check(Looper.myLooper() == Looper.getMainLooper()) {
        "Only the main thread can get the apolloClient instance"
    }*/

    if (instance != null) {
        return instance!!
    }

    val log = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    instance = ApolloClient.builder()
            .serverUrl("https://studs-overlord.herokuapp.com/graphql")
            .okHttpClient(OkHttpClient.Builder()
                    .addInterceptor(log)
                    .addInterceptor(AuthorizationInterceptor(context))
                    .build()
            )
            .addCustomTypeAdapter(CustomType.DATETIME, DateTimeCustomTypeAdapter)
            .build()

    return instance!!
}

val DateTimeCustomTypeAdapter = object : CustomTypeAdapter<LocalDateTime> {
    override fun decode(value: CustomTypeValue<*>): LocalDateTime {
        return try {
            LocalDateTime.parse(value.value.toString())
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }
    }

    override fun encode(value: LocalDateTime): CustomTypeValue<*> {
        return CustomTypeValue.GraphQLString(value.toString())
    }
}

private class AuthorizationInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
                .addHeader("Authorization", StudsPreferences.getJwtToken(context) ?: "")
                .build()

        return chain.proceed(request)
    }
}
