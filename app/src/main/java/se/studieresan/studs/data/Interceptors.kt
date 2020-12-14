package se.studieresan.studs.data

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

private const val AUTHORIZATION = "Authorization"
private const val CONTENT_TYPE = "Content-Type"
private const val CONTENT_TYPE_VALUE = "application/graphql"

class AddJwtInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain
                .request()
                .newBuilder()
        val token = StudsPreferences
                .getJwtToken(context)
        builder.run {
            if (token.isNotEmpty()) {
                addHeader(AUTHORIZATION, token)
            }
            addHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
        }
        return chain.proceed(builder.build())
    }
}

class ReceivedJwtInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val token = response.header(AUTHORIZATION) ?: return response
        StudsPreferences.setJwtToken(context, token)
        return response
    }
}
