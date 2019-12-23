package com.laka.androidlib.net.utils.parse.adapter;

import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.laka.androidlib.net.utils.parse.GsonUtil;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.NumberUtils;

import java.io.IOException;

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:Gson默认Integer转换类，假若后台的数字字段返回了null,"",boolean .
 * 通过当前的解析器转换为默认值0
 */

public class IntegerDefaultFormatAdapter extends TypeAdapter<Integer> {

    @Override
    public void write(JsonWriter out, Integer value) throws IOException {
        if (value == null) {
            value = 0;
        }
        out.value(value);
    }

    @Override
    public Integer read(JsonReader in) throws IOException {
        Integer value;
        try {
            if (in.peek() == JsonToken.NULL) {
                // 假若返回Null，默认为0
                in.nextNull();
                value = 0;
            } else if (in.peek() == JsonToken.BOOLEAN) {
                in.nextBoolean();
                value = 0;
            } else if (in.peek() == JsonToken.STRING) {
                String jsonStr = in.nextString();
                value = NumberUtils.isNumber(jsonStr) ? Integer.parseInt(jsonStr) : 0;
            } else {
                value = in.nextInt();
            }
            // LogUtils.info(GsonUtil.TAG, "进入Integer格式转换类：" + value);
        } catch (Exception e) {
            LogUtils.error(e);
            value = 0;
        }
        return value;
    }
}
