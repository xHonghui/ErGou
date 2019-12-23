package com.laka.androidlib.util;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @Author:Rayman
 * @Date:2019/2/14
 * @Description:Byte操作工具类
 */

public class ByteUtils {

    public static byte[] string2Bytes(String s) {
        if (TextUtils.isEmpty(s)) {
            return new byte[]{};
        }
        return s.getBytes();
    }

    public static byte[] short2Bytes(short s) {
        byte bytes[] = new byte[2];
        bytes[1] = (byte) (s >> 8);
        bytes[0] = (byte) (s >> 0);
        return bytes;
    }

    /**
     * description:Int转Bytes
     **/
    public static byte[] int2Bytes(int i) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) (i >> 24);
        bytes[2] = (byte) (i >> 16);
        bytes[1] = (byte) (i >> 8);
        bytes[0] = (byte) (i >> 0);
        return bytes;
    }

    /**
     * description:Bytes转Int
     **/
    public static int bytes2Int(byte[] bytes) {
        return ((((bytes[3] & 0xff) << 24)
                | ((bytes[2] & 0xff) << 16)
                | ((bytes[1] & 0xff) << 8) |
                ((bytes[0] & 0xff) << 0)));
    }

    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    public static long bytes2Long(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

    /**
     * description:将long转成4个byte的数组（无符号int）
     **/
    public static byte[] long2UnsignedInt(long value) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) ((value >> 24) & 0xff);
        bytes[2] = (byte) ((value >> 16) & 0xff);
        bytes[1] = (byte) ((value >> 8) & 0xff);
        bytes[0] = (byte) ((value >> 0) & 0xff);
        return bytes;
    }

    public static int long2UnsignedInt(byte[] bytes) {
        int value =
                ((bytes[3] >> 24) & 0xff) |
                        ((bytes[2] >> 16) & 0xff) |
                        ((bytes[1] >> 8) & 0xff) |
                        ((bytes[0] >> 0) & 0xff);
        return value;
    }

    public static ByteBuffer allocate(int capacity) {
        return ByteBuffer.allocate(capacity).order(ByteOrder.LITTLE_ENDIAN);
    }

    public static int readInt(BufferedInputStream bIn) throws IOException {

        byte[] bytes = new byte[4];

        readBytes(bIn, bytes);

        ByteBuffer buffer = allocate(4);
        buffer.put(bytes);

        buffer.flip();
        int result = buffer.getInt();
        return result;
    }

    public static void readBytes(BufferedInputStream bIn, byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return;
        }
        int len = data.length;
        int hasRead = 0;
        int result;
        while (hasRead < len) {
            result = bIn.read(data, hasRead, len - hasRead);
            if (result == -1) {
                return;
//                throw new IOException();
            }
            hasRead += result;
        }
    }

    /**
     * 返回当前数组位数
     *
     * @param params
     * @return
     */
    public static int getArrayBitLength(Object... params) {
        int bytesCount = 128;
        if (params != null) {
            for (Object param : params) {
                if (param instanceof String) {
                    byte[] stringBytes = ((String) param).getBytes(Charset.forName("UTF-8"));
                    bytesCount += stringBytes.length;
                } else if (param instanceof Integer) {
                    bytesCount += 4;
                } else if (param instanceof Byte) {
                    bytesCount++;
                } else if (param instanceof Short) {
                    bytesCount += 2;
                } else if (param instanceof Byte[]) {
                    bytesCount += ((Byte[]) param).length;
                }

            }
        }
        return bytesCount;
    }

    /**
     * 添加String到ByteBuffer
     *
     * @param byteBuffer
     * @param string
     */
    public static void appendStringWithLength(ByteBuffer byteBuffer, String string) {
        byte[] stringBytes = string.getBytes(Charset.forName("UTF-8"));
        byteBuffer.putInt(stringBytes.length);
        byteBuffer.put(stringBytes);
    }

    public static String byteArrToHexStr(byte[] bytes) throws Exception {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) {
            result.append(String.format("%02X ", b));
            result.append(" "); // delimiter
        }
        return result.toString();
    }

    public static long getUnsignedInt(int value) {
        return (long) value & 0xFFFFFFFF;
    }
}
