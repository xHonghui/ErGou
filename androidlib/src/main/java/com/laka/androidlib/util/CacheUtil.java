package com.laka.androidlib.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;


/**
 * @Author:summer
 * @Date:2019/1/12
 * @Description: 缓存工具类
 */
public class CacheUtil {

    public static String getTotalCacheSize(Context context) {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        //return FileUtils.getFormatSize(cacheSize); //格式化为K、M、G、T
        return FileUtils.getFormatSizeForMB(cacheSize); //统一格式化为MB为单位
    }

    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size += getFolderSize(fileList[i]);
                } else {
                    size += fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static void clearAllCache(Context context) {
        clearFolderFile(context.getCacheDir().getPath(), true);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            clearFolderFile(context.getExternalCacheDir().getPath(),true);
        }
    }


    /**
     * @param filePath       缓存目录
     * @param isDeleteFolder 是否删除文件夹（空目录）
     */
    public static void clearFolderFile(String filePath, boolean isDeleteFolder) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        clearFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (!file.isDirectory()) {
                    file.delete();
                } else {// 目录
                    if (isDeleteFolder) {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
