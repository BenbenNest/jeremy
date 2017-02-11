package com.jeremy.lychee.utils;

import java.lang.reflect.Type;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonUtils {

	public final static Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	private static Gson mUriGson = new GsonBuilder().registerTypeAdapter(
			Uri.class, new UriInOut()).create();

	public static String toJson(Object src) {

		return mGson.toJson(src);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {

		return mGson.fromJson(json, clazz);
	}

	public static <T> T fromJson(String json, Type type) {

		return mGson.fromJson(json, type);
	}

	public static class UriInOut implements JsonSerializer<Uri>,
			JsonDeserializer<Uri> {
		@Override
		public JsonElement serialize(Uri src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}

		@Override
		public Uri deserialize(final JsonElement src, final Type srcType,
				final JsonDeserializationContext context)
				throws JsonParseException {
			return Uri.parse(src.getAsString());
		}
	}

	public static <T> T fromUriJson(String json, Type type) {
		return mUriGson.fromJson(json, type);
	}

	public static String toUriJson(Object object) {
		return mUriGson.toJson(object);
	}
}
