package com.laka.androidlib.net.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.Map;
import java.util.TreeMap;


/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Date: 09/02/2018
 */

public class Util {
    /**
     * Compose the params to url
     *
     * @param url      request url
     * @param paramMap request params
     * @return A url with params.
     */
    public static String composeParams(@NonNull String url, ArrayMap<String, Object> paramMap) {

        // URL is null, throw a RuntimeException.
        if (url == null) {
            throw new RuntimeException("url can't be null");
        }

        // If the paramsMap is null or empty, return the url simply.
        if (paramMap == null || paramMap.size() == 0) {
            return url;
        }

        StringBuilder paramsUrl = new StringBuilder().append(url);

        if (!url.contains("?")) {
            paramsUrl.append("?");
        }

        int paramMapSize = 0;

        for (String key : paramMap.keySet()) {

            if (paramMap.get(key) == null) {
                continue; // the value is null, leave it.
            }

            paramsUrl.append(key).append("=").append(paramMap.get(key));

            ++paramMapSize;

            if (paramMapSize != paramMap.size()) {
                paramsUrl.append("&");
            }

        }

        return paramsUrl.toString();
    }

    /**

     * @param paramMap request params
     * @return A url with params.
     */
    public static String composeParams(ArrayMap<String, Object> paramMap) {

        // If the paramsMap is null or empty, return the url simply.
        if (paramMap == null || paramMap.size() == 0) {
            return "";
        }

        StringBuilder paramsUrl = new StringBuilder();

        int paramMapSize = 0;

        for (String key : paramMap.keySet()) {

            if (paramMap.get(key) == null) {
                continue; // the value is null, leave it.
            }

            paramsUrl.append(key).append("=").append(paramMap.get(key));

            ++paramMapSize;

            if (paramMapSize != paramMap.size()) {
                paramsUrl.append("&");
            }

        }

        return paramsUrl.toString();
    }

    /**
     * 先对key排序，再拼拼参数。
     */
    public static String sortMapByKeyAndComposeParams(ArrayMap<String, Object> paramMap) {

        if (paramMap == null || paramMap.size() == 0) {
            return "";
        }

        // TreeMap会对Key进行升序排序，所以，只要put进去就行。
        Map<String, Object> treeMap = new TreeMap<>();

        treeMap.putAll(paramMap);

        StringBuilder paramsUrl = new StringBuilder();

        int paramMapSize = 0;

        for (String key : treeMap.keySet()) {

            if (treeMap.get(key) == null) {
                continue; // the value is null, leave it.
            }

            paramsUrl.append(key).append("=").append(treeMap.get(key));
            ++paramMapSize;

            if (paramMapSize != treeMap.size()) {
                paramsUrl.append("&");
            }
        }

        return paramsUrl.toString();
    }


    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        try {
            Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                if (idx == -1) {
                    return null;
                }
                result = cursor.getString(idx);
                cursor.close();
            }
        } catch (Exception e) {
            return "";
        }
        return result;
    }


}
