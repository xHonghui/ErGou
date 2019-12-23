package com.laka.ergou.mvp.main.model.event

import android.net.Uri
import android.util.SparseArray
import android.widget.ImageView
import com.laka.ergou.mvp.circle.model.bean.CircleArticle

data class ImageDataEvent(
        var imageView: ImageView,
        var imageGroupList: SparseArray<ImageView>,
        var urlList: List<Uri>,
        val mCircleArticle: CircleArticle
)