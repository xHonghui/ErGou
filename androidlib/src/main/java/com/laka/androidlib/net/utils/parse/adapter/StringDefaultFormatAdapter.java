package com.laka.androidlib.net.utils.parse.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:Gson默认String转换类，假若后台原本需要返回String字段 .
 * 但是最后返回了一个null。通过转换类，默认转换为空字符串
 */

public class StringDefaultFormatAdapter extends TypeAdapter<String> {

    @Override
    public String read(JsonReader reader) throws IOException {
        String value;
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            value = "";
        } else if (reader.peek() == JsonToken.BOOLEAN) {
            reader.nextBoolean();
            value = "";
        } else {
            value = reader.nextString();
        }
        // LogUtils.info(GsonUtil.TAG, "进入String格式转换类：" + value);
        return value;
    }

    @Override
    public void write(JsonWriter writer, String value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value);
    }
}
