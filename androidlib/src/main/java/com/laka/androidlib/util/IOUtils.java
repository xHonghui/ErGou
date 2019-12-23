package com.laka.androidlib.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: IOUtils
 * @Description: 文件io操作工具类
 * @Author: chuan
 * @Date: 19/01/2018
 */

/**
 * IOUtils,对io流进行操作。
 * 采用NIO和IO的接口封装。
 * NIO接口读写更加迅速，但对行，字符的处理不方便。
 * IO可以对字节，字符，等一系列进行处理。
 */

public final class IOUtils {

    private IOUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    /**
     * 读取文件
     *
     * @param is 要读取的文件流
     * @return 文件内容byte数组或null
     */
    public static byte[] readToByte(InputStream is) {
        if (is == null) {
            return null;
        }

        byte[] res = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            byte[] bytes = new byte[1024 * 4];
            int readNum;
            if ((readNum = is.read(bytes)) > 0) {
                baos.write(bytes, 0, readNum);
            }

            res = baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    /**
     * 读取文件
     *
     * @param file 要读取的文件
     * @return 文件内容byte数组或null
     */
    public static byte[] readToByte(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }

        byte[] res = null;
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            is = new FileInputStream(file);

            byte[] bytes = new byte[1024 * 4];
            int readNum;
            if ((readNum = is.read(bytes)) > 0) {
                baos.write(bytes, 0, readNum);
            }

            res = baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }

                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    /**
     * 读取文件
     *
     * @param file 要读取的文件
     * @return 文件内容字符串或null
     */
    public static String readToString(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }

        List<String> strList = readToList(file);
        if (strList == null || strList.size() == 0) {
            return null;
        }

        StringBuilder sBuilder = new StringBuilder();

        for (String s : strList) {
            sBuilder.append(s).append("\n");
        }

        return sBuilder.toString();
    }

    /**
     * 读取文件
     *
     * @param file 要读取的文件
     * @return 文件内容列表或null
     */
    public static List<String> readToList(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader bReader = null;

        List<String> strList = new ArrayList<>();

        try {
            is = new FileInputStream(file);
            isr = new InputStreamReader(is);
            bReader = new BufferedReader(isr);

            String line;
            while ((line = bReader.readLine()) != null) {
                strList.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }

                if (isr != null) {
                    isr.close();
                }

                if (bReader != null) {
                    bReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strList;
    }

    /**
     * 读取文件
     *
     * @param file 要读取的文件
     * @return 文件内容Object列表或null
     */
    public static List<Object> readToObject(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }

        List<Object> objectList = new ArrayList<>();
        InputStream is = null;
        ObjectInputStream ois = null;

        try {
            is = new FileInputStream(file);
            ois = new ObjectInputStream(is);

            Object o;
            while ((o = ois.readObject()) != null) {
                objectList.add(o);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }

                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return objectList;
    }

    /**
     * 向文件写入数据
     *
     * @param file 文件名
     * @param str  要写入的String数据
     * @return 写入结果
     */
    public static boolean writeFile(File file, String str) {
        return StringUtils.isEmpty(str) && writeFile(file, str.getBytes());
    }

    /**
     * 向文件写入数据
     *
     * @param file  文件名
     * @param bytes 要写入的byte[]数据
     * @return 写入结果
     */
    public static boolean writeFile(File file, byte[] bytes) {
        if (file == null || !file.exists() || file.isDirectory()
                || bytes == null || bytes.length == 0) {
            return false;
        }

        boolean result;

        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.clear();
        buffer.put(bytes);
        buffer.flip();

        FileOutputStream fos = null;
        FileChannel fc = null;

        try {
            fos = new FileOutputStream(file);
            fc = fos.getChannel();

            while (buffer.hasRemaining()) {
                fc.write(buffer);
            }

            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }

                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 向文件写入数据
     *
     * @param file   文件名
     * @param object 要写入的Serializable[]数据
     * @return 写入结果
     */
    public static boolean writeFile(File file, Serializable[] object) {
        if (file == null || !file.exists() || file.isDirectory()
                || object == null || object.length == 0) {
            return false;
        }

        OutputStream os = null;
        ObjectOutputStream oos = null;

        boolean result;
        try {
            os = new FileOutputStream(file);
            oos = new ObjectOutputStream(os);

            for (Serializable o : object) {
                oos.writeObject(o);
            }

            oos.flush();

            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }

                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
