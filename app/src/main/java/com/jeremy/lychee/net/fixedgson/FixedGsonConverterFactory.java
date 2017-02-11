package com.jeremy.lychee.net.fixedgson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * Created by zhangying-pd on 2016/3/23.
 * 修复了解析失败直接结束的问题,某个字段解析失败后会自动跳过去解析下一个字段
 */
public class FixedGsonConverterFactory extends Converter.Factory  {

    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static FixedGsonConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static FixedGsonConverterFactory create(Gson gson) {
        return new FixedGsonConverterFactory(gson);
    }

    private final Gson gson;

    private FixedGsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        FixedReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new FixedReflectiveTypeAdapterFactory(
                new ConstructorConstructor(Collections.<Type, InstanceCreator<?>>emptyMap()), FieldNamingPolicy.IDENTITY, Excluder.DEFAULT);
        TypeToken token = TypeToken.get(type);
        TypeAdapter<?> adapter =reflectiveTypeAdapterFactory.create(gson, token);
        return new FixedGsonResponseBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {

        FixedReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new FixedReflectiveTypeAdapterFactory(
                new ConstructorConstructor(Collections.<Type, InstanceCreator<?>>emptyMap()), FieldNamingPolicy.IDENTITY, Excluder.DEFAULT);
        TypeToken token = TypeToken.get(type);
        TypeAdapter<?> adapter =reflectiveTypeAdapterFactory.create(gson, token);
        return new FixedGsonResponseBodyConverter2<>(gson, adapter);
    }

}
