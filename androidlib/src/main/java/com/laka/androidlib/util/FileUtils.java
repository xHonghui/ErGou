package com.laka.androidlib.util;

import android.Manifest;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.laka.androidlib.listener.IPermissionQuestCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.NumberFormat;

/**
 * @ClassName: FileUtils
 * @Description: 文件操作工具类
 * @Author: chuan
 * @Date: 10/01/2018
 */

/**
 * Android的文件管理系统分为三类：内部存储，app私有存储和公有存储。
 * <p>
 * 内部存储文件夹较小，大致文件目录为/data/data/。。。/app名/
 * 默认为files和cache两个文件夹，非root状态下不可见。
 * app卸载时会被删除。故。若sd卡存在的情况下，不建议使用。
 * 4.4后不需要文件读写权限
 * <p>
 * app私有存储文件夹，在sd卡上，为。。。/Android/data/app名/
 * 默认为files和cache两个文件夹
 * app卸载时会被删除，推荐使用本文件夹作为app私有文件的存储
 * 4.4后不需要文件读写权限
 * <p>
 * 公有存储文件夹，在sd卡上，为Android手机公有。任何app或用户可见。
 * app卸载时不会被删除
 * 需要文件读写权限。6.0后需要动态申请权限
 */
public final class FileUtils {
    public static final int SDCARD_NOT_MOUNT = -4;  //sd卡未挂载
    public static final int FILE_NO_PERMISSION = -3;  //无权限
    public static final int FILE_NOT_EXIT = -2;  //文件不存在
    public static final int FILE_HANDLE_FAIL = -1;  //操作失败
    public static final int FILE_HANDLE_SUCCESS = 0;  //操作成功

    private static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private FileUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    /**
     * 判断sd卡是否存在
     *
     * @return true，sd卡存在；false，sd卡不存在
     */
    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 创建项目私有文件夹
     * 4.4之后不需要文件读写权限
     *
     * @param dirName  子文件夹名
     * @param type     文件类型
     *                 {@link android.os.Environment#DIRECTORY_MUSIC},
     *                 {@link android.os.Environment#DIRECTORY_PODCASTS},
     *                 {@link android.os.Environment#DIRECTORY_RINGTONES},
     *                 {@link android.os.Environment#DIRECTORY_ALARMS},
     *                 {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *                 {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *                 {@link android.os.Environment#DIRECTORY_MOVIES}.
     * @param isCreate 不存在时是否创建
     * @return 创建的文件夹
     */
    public static File getAppFileDir(String dirName,
                                     @Nullable String type, boolean isCreate) {
        if (StringUtils.isTrimEmpty(dirName)) {
            return null;
        }

        File parent = getAppFileDir(type);
        if (parent == null) {
            return null;
        }

        File file = new File(parent, dirName);

        boolean isExist = file.exists() && file.isDirectory();
        if (isCreate && !isExist) {
            isExist = file.mkdirs();

        }
        return isExist ? file : null;
    }

    /**
     * 创建项目私有文件
     * 4.4之后不需要文件读写权限
     *
     * @param fileName 子文件名
     * @param type     文件类型
     *                 {@link android.os.Environment#DIRECTORY_MUSIC},
     *                 {@link android.os.Environment#DIRECTORY_PODCASTS},
     *                 {@link android.os.Environment#DIRECTORY_RINGTONES},
     *                 {@link android.os.Environment#DIRECTORY_ALARMS},
     *                 {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *                 {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *                 {@link android.os.Environment#DIRECTORY_MOVIES}.
     * @param isCreate 不存在时是否创建
     * @return 创建的文件
     */
    public static File getAppFile(String fileName,
                                  @Nullable String type, boolean isCreate) {
        if (StringUtils.isTrimEmpty(fileName)) {
            return null;
        }

        File parent = getAppFileDir(type);
        if (parent == null) {
            return null;
        }

        File file = new File(parent, fileName);

        boolean isExist = file.exists() && !file.isDirectory();
        if (isCreate && !isExist) {
            try {
                isExist = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isExist ? file : null;
    }

    /**
     * 删除项目私有文件
     * 4.4之后不需要文件读写权限
     *
     * @param fileName 文件名称
     * @param type     文件类型，可为空
     * @return 是否删除成功
     * {@link FileUtils#FILE_NOT_EXIT}
     * {@link FileUtils#FILE_HANDLE_SUCCESS}
     * {@link FileUtils#FILE_HANDLE_FAIL}
     */
    public static int deleteAppFile(String fileName, @Nullable String type) {
        if (StringUtils.isTrimEmpty(fileName)) {
            return FILE_NOT_EXIT;
        }

        File parent = getAppFileDir(type);
        if (parent == null) {
            return FILE_NOT_EXIT;
        }
        return deleteFile(new File(parent, fileName));
    }

    /**
     * 判断项目文件是否存在
     *
     * @param fileName 文件名称
     * @param type     文件类型，可为空
     * @param isFile   是否是文件
     * @return true ，存在；false ，不存在
     */
    public static boolean isAppFileExit(String fileName, @Nullable String type, boolean isFile) {
        return isExist(getAppFileDir(type), fileName, isFile);
    }

    /**
     * 创建项目私有缓存文件夹
     * 4.4之后不需要文件读写权限
     *
     * @param dirName  子文件夹名
     * @param isCreate 不存在时是否创建
     * @return 创建的文件夹
     */
    public static File getCacheFileDir(String dirName,
                                       boolean isCreate) {
        if (StringUtils.isTrimEmpty(dirName)) {
            return null;
        }

        File parent = getCacheFileDir();
        if (parent == null) {
            return null;
        }

        File file = new File(parent, dirName);

        boolean isExist = file.exists() && file.isDirectory();
        if (isCreate && !isExist) {
            isExist = file.mkdirs();
        }
        return isExist ? file : null;
    }

    /**
     * 创建项目私有缓存文件
     * 4.4之后不需要文件读写权限
     *
     * @param fileName 子文件名
     * @return 创建的文件
     */
    public static File getCacheFile(String fileName, boolean isCreate) {
        if (StringUtils.isTrimEmpty(fileName)) {
            return null;
        }

        File parent = getCacheFileDir();
        if (parent == null) {
            return null;
        }

        File file = new File(parent, fileName);

        boolean isExist = file.exists() && !file.isDirectory();
        if (isCreate && !isExist) {
            try {
                isExist = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isExist ? file : null;
    }

    /**
     * 删除缓存文件
     *
     * @param fileName 文件名
     * @return 是否删除成功
     * {@link FileUtils#FILE_NOT_EXIT}
     * {@link FileUtils#FILE_HANDLE_SUCCESS}
     * {@link FileUtils#FILE_HANDLE_FAIL}
     */
    public static int deleteCacheFile(String fileName) {
        if (StringUtils.isTrimEmpty(fileName)) {
            return FILE_NOT_EXIT;
        }

        File parent = getCacheFileDir();
        if (parent == null) {
            return FILE_NOT_EXIT;
        }
        return deleteFile(new File(parent, fileName));
    }

    /**
     * 清空缓存
     */
    public static void clearCache() {
        deleteFile(ApplicationUtils.getApplication().getCacheDir());
        if (isSDCardMounted()) {
            deleteFile(ApplicationUtils.getApplication().getExternalCacheDir());
        }
    }

    /**
     * 判断项目缓存文件是否存在
     *
     * @param fileName 文件名称
     * @param isFile   是否是文件
     * @return true ，存在；false ，不存在
     */
    public static boolean isCacheFileExit(String fileName, boolean isFile) {
        return isExist(getCacheFileDir(), fileName, isFile);
    }

    /**
     * 获取app所有缓存的大小
     *
     * @return 缓存大小
     */
    public static long getTotalCacheSize() {
        long size = getFileSize(ApplicationUtils.getApplication().getCacheDir());

        if (isSDCardMounted()) {
            size = size + getFileSize(ApplicationUtils.getApplication().getExternalCacheDir());
        }

        return size;
    }

    /**
     * 得到sd卡的根目录
     *
     * @return sd卡根目录路径
     */
    public static String getSDCardPath() {
        if (!isSDCardMounted()) {
            return null;
        }

        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 创建sd卡上的公有文件夹，传入的路径为完整路径
     *
     * @param dirPath  文件路径。完整路径如：/storage/。。。。。
     * @param isCreate 不存在时是否创建
     * @param callback 需要申请权限的回调
     * @return null，如果sd卡没有挂载或没有权限；sd卡根目录路径
     */
    public static File getSDCardDir(String dirPath, boolean isCreate,
                                    IPermissionQuestCallback callback) {
        if (StringUtils.isTrimEmpty(dirPath) || !isSDCardMounted()) {
            return null;
        }

        if (!checkReadAndWritePermission()) {
            if (callback != null) {
                callback.questPermissions(PERMISSIONS);
            }
            return null;
        }

        File file = new File(dirPath);
        boolean isExist = file.exists() && file.isDirectory();
        if (isCreate && !isExist) {
            isExist = file.mkdirs();
        }

        return isExist ? file : null;
    }

    /**
     * 创建sd卡上的公有文件，传入路径为完整路径
     *
     * @param dirPath  文件完整路径
     * @param isCreate 不存在时是否创建
     * @param callback 需要申请权限的回调
     * @return null，如果sd卡没有挂载或没有权限；sd卡上创建的文件
     */
    public File getSDCardFile(String dirPath, boolean isCreate,
                              IPermissionQuestCallback callback) {
        if (StringUtils.isTrimEmpty(dirPath) || !isSDCardMounted()) {
            return null;
        }

        if (!checkReadAndWritePermission()) {
            if (callback != null) {
                callback.questPermissions(PERMISSIONS);
            }
            return null;
        }

        File file = new File(dirPath);
        boolean isExist = file.exists() && !file.isDirectory();
        if (isCreate && !isExist) {
            try {
                isExist = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isExist ? file : null;
    }

    /**
     * 删除sd卡上的文件
     *
     * @param filePath 文件完整路径
     * @param callback 需要申请权限的回调
     * @return {@link FileUtils#FILE_NOT_EXIT}
     * {@link FileUtils#FILE_HANDLE_SUCCESS}
     * {@link FileUtils#FILE_HANDLE_FAIL}
     * {@link FileUtils#SDCARD_NOT_MOUNT}
     * {@link FileUtils#FILE_NO_PERMISSION}
     */
    public static int deleteSDCardFile(String filePath,
                                       IPermissionQuestCallback callback) {
        if (StringUtils.isTrimEmpty(filePath)) {
            return FILE_NOT_EXIT;
        }

        if (!isSDCardMounted()) {
            return SDCARD_NOT_MOUNT;
        }

        if (!checkReadAndWritePermission()) {
            if (callback != null) {
                callback.questPermissions(PERMISSIONS);
            }
            return FILE_NO_PERMISSION;
        }

        return deleteFile(new File(filePath));
    }

    /**
     * 判断sd卡上文件是否存在
     *
     * @param filePath 文件完整路径
     * @param isFile   是否是文件
     * @return true ，存在；false ，不存在
     */
    public static boolean isSDCardFileExit(String filePath, boolean isFile,
                                           IPermissionQuestCallback callback) {
        if (StringUtils.isTrimEmpty(filePath)) {
            return false;
        }

        if (checkReadAndWritePermission()) {
            if (callback != null) {
                callback.questPermissions(PERMISSIONS);
            }

            return false;
        }

        File file = new File(filePath);

        return file.exists() &&
                ((isFile && !file.isDirectory()
                        || (!isFile && file.isDirectory())));
    }

    /**
     * 计算文件大小
     *
     * @param file 文件名
     * @return 文件大小
     */
    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }

        long size = 0;

        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                size = size + getFileSize(f);
            } else {
                size = size + f.length();
            }
        }
        return size;
    }

    /**
     * 格式化文件大小的单位
     *
     * @param size 文件大小
     * @return 带单位的文件大小
     */
    public static String getFormatSize(long size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 格式化文件大小的单位（MB）
     *
     * @param size 文件大小
     * @return 带单位的文件大小
     */
    public static String getFormatSizeForMB(long size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0M";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1 && megaByte > 0.10) {
            return numberFormat(megaByte)+"M";
        } else if (megaByte <= 0.1) {
            return "0M";
        } else {
            return numberFormat(megaByte)+"M";
        }
    }

    /**
     * 格式化 double 数据类型
     */
    private static String numberFormat(double arg) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        BigDecimal bigDecimal = new BigDecimal(arg);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(bigDecimal);
    }


    /**
     * 根据文件路径获取文件名
     *
     * @param filePath      文件路径
     * @param withExtension 是否包括文件扩展名
     * @return null，文件不存在；文件名
     */
    public static String getFileNameFromPath(String filePath, boolean withExtension) {
        if (StringUtils.isTrimEmpty(filePath)) {
            return null;
        }

        int splitIndex = filePath.lastIndexOf(File.separator);

        if (splitIndex >= 0 || splitIndex < filePath.length()) {
            String fileName = filePath.substring(splitIndex + 1);

            if (withExtension) {
                return fileName;
            } else {
                int dotIndex = fileName.lastIndexOf(".");

                if (dotIndex <= 0 || dotIndex >= fileName.length() - 1) {
                    return fileName;
                }

                return fileName.substring(0, dotIndex);
            }
        }

        return filePath;
    }

    /**
     * 根据文件路径获取文件类型后缀名
     *
     * @param filePath 文件路径
     * @return null，文件类型后缀名；文件名
     */
    public static String getFileFormatFromPath(String filePath) {
        if (StringUtils.isTrimEmpty(filePath)) {
            return null;
        }

        int splitIndex = filePath.lastIndexOf(".");

        if (splitIndex >= 0 || splitIndex < filePath.length()) {
            return filePath.substring(splitIndex + 1);
        }

        return filePath;
    }

    /**
     * 拷贝文件
     * 采用FileChannel的方式实现，效率更高
     *
     * @param source 源文件
     * @param target 目标文件
     */
    public static void copyFile(File source, File target) {
        if (source == null || !source.exists() || source.isDirectory()
                || target == null || !target.exists() || target.isDirectory()) {
            return;
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel ifc = null;
        FileChannel ofc = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            ifc = fis.getChannel();
            ofc = fos.getChannel();

            ifc.transferTo(0, ifc.size(), ofc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (fos != null) {
                    fos.close();
                }

                if (ifc != null) {
                    ifc.close();
                }

                if (ofc != null) {
                    ofc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*********************私有方法开始*********************/

    /**
     * 获取项目私有文件夹，若已经挂载sd卡，则返回sd卡上的app私有目录，否则返回app 的内部存储目录
     * 4.4之后不需要文件读写权限
     *
     * @param type 文件类型
     *             {@link android.os.Environment#DIRECTORY_MUSIC},
     *             {@link android.os.Environment#DIRECTORY_PODCASTS},
     *             {@link android.os.Environment#DIRECTORY_RINGTONES},
     *             {@link android.os.Environment#DIRECTORY_ALARMS},
     *             {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *             {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *             {@link android.os.Environment#DIRECTORY_MOVIES}.
     * @return 文件夹
     */
    private static File getAppFileDir(@Nullable String type) {
        File file;
        if (isSDCardMounted()) {
            file = ApplicationUtils.getApplication().getExternalFilesDir(type);
        } else {
            file = ApplicationUtils.getApplication().getFilesDir();
        }

        return file != null && file.exists() ? file : null;
    }

    /**
     * 获取项目缓存文件夹，若已经挂载sd卡，则返回sd卡上的app私有缓存目录，否则返回app 的内部缓存存储目录
     * 4.4之后不需要文件读写权限
     *
     * @return 缓存文件夹
     */
    private static File getCacheFileDir() {
        File file;
        if (isSDCardMounted()) {
            file = ApplicationUtils.getApplication().getExternalCacheDir();
        } else {
            file = ApplicationUtils.getApplication().getCacheDir();
        }

        return file != null && file.exists() ? file : null;
    }

    /**
     * 删除文件夹
     *
     * @param file 要删除的文件夹
     * @return 文件夹是否删除成功
     * {@link FileUtils#FILE_NOT_EXIT}
     * {@link FileUtils#FILE_HANDLE_SUCCESS}
     * {@link FileUtils#FILE_HANDLE_FAIL}
     */
    private static int deleteFile(File file) {
        if (file == null || !file.exists()) {
            return FILE_NOT_EXIT;
        }

        if (file.isDirectory()) {
            File[] childs = file.listFiles();
            for (File child : childs) {
                deleteFile(child);
            }
        }

        return file.delete() ? FILE_HANDLE_SUCCESS : FILE_HANDLE_FAIL;
    }


    /**
     * 检查读写文件权限
     *
     * @return true，有权限；false，无权限
     */
    private static boolean checkReadAndWritePermission() {
        return PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 判断文件或文件夹是否存在
     *
     * @param parent   父文件夹
     * @param fileName 文件名
     * @param isFile   是否是文件
     * @return true ，存在 ； false ， 不存在
     */
    private static boolean isExist(File parent, String fileName, boolean isFile) {
        if (parent == null || !parent.exists() || !parent.isDirectory()) {
            return false;
        }

        if (StringUtils.isTrimEmpty(fileName)) {
            return false;
        }

        File file = new File(parent, fileName);
        return file.exists() &&
                ((isFile && !file.isDirectory()
                        || (!isFile && file.isDirectory())));
    }
}
