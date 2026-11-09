package com.example.gymapp.data.remote

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializerOrNull
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class KotlinxSerializationConverterFactory private constructor(
    private val json: Json
) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val serializer = runCatching { serializerOrNull(type) }.getOrNull() ?: return null
        return Converter<ResponseBody, Any> { body ->
            body.use { json.decodeFromString(serializer, it.string()) }
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val serializer = runCatching { serializerOrNull(type) }.getOrNull() ?: return null
        return Converter<Any, RequestBody> { value ->
            json.encodeToString(serializer, value).toRequestBody(JSON_MEDIA_TYPE)
        }
    }

    companion object {
        private val JSON_MEDIA_TYPE: MediaType = "application/json".toMediaType()

        fun create(json: Json): Converter.Factory = KotlinxSerializationConverterFactory(json)
    }
}
