package com.laka.androidlib.ext

import android.graphics.Typeface
import android.widget.TextView

fun TextView.setNumTypeface(){
    val type = Typeface.createFromAsset(context.getAssets(), "FF_DIN_BOLD-2.ttf")
    setTypeface(type)
}