package com.laka.ergou.mvp.share.view.activity

import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.laka.androidlib.base.activity.BaseActivity
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.StatusBarUtil
import com.laka.ergou.R
import com.laka.ergou.mvp.share.constant.ShareConstant
import kotlinx.android.synthetic.main.activity_see_bigimage.*

/**
 * @Author:summer
 * @Date:2019/5/29
 * @Description:查看大图
 */
class SeeBigImageActivity : BaseActivity() {

    private var mImageList: ArrayList<String> = ArrayList()
    private var mPosition: Int = 0

    override fun setContentView(): Int {
        return R.layout.activity_see_bigimage
    }

    override fun setStatusBarColor(color: Int) {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, color), 0)
    }

    override fun initIntent() {
        mImageList = intent.getSerializableExtra(ShareConstant.KEY_SEE_BIGIMAGE_LIST) as ArrayList<String>
        mPosition = intent.getIntExtra(ShareConstant.KEY_SEE_BIGIMAGE_POSITION, 0)
    }

    override fun initViews() {
        tv_count.text = "${mPosition + 1}/${mImageList.size}"
    }

    override fun initData() {
        view_pager_bigimage.adapter = SeeBigImageViewPagerAdapter(mImageList)
        view_pager_bigimage.currentItem = mPosition
    }

    override fun initEvent() {
        view_pager_bigimage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                tv_count.text = "${position + 1}/${mImageList.size}"
            }
        })
        cl_back1.setOnClickListener { finish() }
    }

    //非静态内部类，如果没有任何修饰符，则默认是静态内部类
    inner class SeeBigImageViewPagerAdapter : PagerAdapter {

        private var imageList: ArrayList<String> = ArrayList()

        constructor(list: ArrayList<String>) : super() {
            this.imageList = list
        }

        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return if (imageList == null) 0 else imageList.size
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            var itemView = LayoutInflater.from(this@SeeBigImageActivity).inflate(R.layout.item_see_bigimage, null)
            val photoView = itemView.findViewById<ImageView>(R.id.iv_img)
            GlideUtil.loadImageNoCrop(this@SeeBigImageActivity, imageList[position], R.drawable.default_img, photoView)
            container?.addView(itemView)
            return itemView
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container?.removeView(`object` as View?)
        }
    }

}