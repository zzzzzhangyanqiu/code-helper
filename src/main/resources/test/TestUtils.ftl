package com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

public class TestUtils {
    public static JSONObject getTestArg(String path) {
        String s = new JSONReader(
                new BufferedReader(new InputStreamReader(TestUtils.class.getResourceAsStream(path))))
                .readString();
        return JSONObject.parseObject(s);
    }
}