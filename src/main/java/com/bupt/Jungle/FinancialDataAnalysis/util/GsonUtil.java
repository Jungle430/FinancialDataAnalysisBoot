package com.bupt.Jungle.FinancialDataAnalysis.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;


public final class GsonUtil {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private GsonUtil() {
    }

    public static <T> T jsonToBean(String jsonString, Class<T> classOfT, String dateFormat) throws JsonSyntaxException {
        Gson gson = new GsonBuilder().setDateFormat(Optional.ofNullable(dateFormat).orElse(DEFAULT_DATE_FORMAT)).create();
        return gson.fromJson(jsonString, classOfT);
    }

    public static <T> T jsonToBean(String jsonString, Class<T> classOfT) throws JsonSyntaxException {
        return jsonToBean(jsonString, classOfT, null);
    }

    public static <T> String beanToJson(T bean, String dateFormat) {
        Gson gson = new GsonBuilder().setDateFormat(Optional.ofNullable(dateFormat).orElse(DEFAULT_DATE_FORMAT)).create();
        return gson.toJson(bean);
    }

    public static <T> String beanToJson(T bean) {
        return beanToJson(bean, null);
    }

    public static <T> List<T> jsonToList(String jsonString, Class<T> classOfT, String dateFormat) throws JsonSyntaxException, IllegalArgumentException {
        Gson gson = new GsonBuilder().setDateFormat(Optional.ofNullable(dateFormat).orElse(DEFAULT_DATE_FORMAT)).create();
        Type type = TypeToken.getParameterized(List.class, classOfT).getType();
        return gson.fromJson(jsonString, type);
    }

    public static <T> List<T> jsonToList(String jsonString, Class<T> classOfT) throws JsonSyntaxException, IllegalArgumentException {
        return jsonToList(jsonString, classOfT, null);
    }
}