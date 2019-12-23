package com.laka.ergou.mvp.shopping.center.helper

import android.content.Context

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.FileNotFoundException
import java.io.IOException
import com.facebook.common.file.FileUtils.mkdirs
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory
import android.os.Environment.MEDIA_MOUNTED
import android.util.Log
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.image.BitmapUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import java.io.File
import java.io.FileOutputStream


class SaveImageHelper(private val context: Context) {
    private var bitmap: Bitmap? = null

    fun savaBitmap(imgName: String, bytes: ByteArray, img_path: String) {
        var imgName = imgName
        var filePath = ""
        var fos: FileOutputStream? = null
        try {
            filePath = context.cacheDir.absolutePath
            val imgDir = File(filePath)
            if (!imgDir.exists()) {
                imgDir.mkdirs()
            }
            imgName = "$filePath/$imgName"
            fos = FileOutputStream(imgName)
            fos?.write(bytes)
            SPHelper.putString(ShoppingCenterConstant.SP_KEY_ADVERT_PATH, img_path)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (fos != null) {
                    fos!!.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun getImage(filename: String): Bitmap? {
        val imgPath = "${context.cacheDir.absolutePath}/$filename"
        try {
//            bitmap = BitmapFactory.decodeFile(ImgPath)
            bitmap = BitmapUtils.decodeBitmap(imgPath, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

}
