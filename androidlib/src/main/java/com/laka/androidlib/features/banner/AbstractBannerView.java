package com.laka.androidlib.features.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.androidlib.util.ListUtils;
import com.laka.androidlib.util.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: AbstractBannerView
 * @Description:
 * @Author: chuan
 * @Date: 14/03/2018
 */

public abstract class AbstractBannerView<T> extends FrameLayout implements
        ViewPager.OnPageChangeListener {
    protected final static String TAG = AbstractBannerView.class.getSimpleName();

    private static final int MSG = 1;
    private final static long AUTO_PLAY_DELAY_TIME = 5000L; //banner自动翻页的时间

    protected ViewPager mVpPager;
    protected LinearLayout mLlDots;

    private boolean mCancelled = true;
    private int mCurrentItem;

    private _Adapter mAdapter = new _Adapter();
    private _Handler mHandler = new _Handler(this);  //处理banner的自动翻页

    private List<T> mBannerList = new ArrayList<>();
    private List<SimpleDraweeView> mDraweeViewList = new ArrayList<>();
    private List<View> mDotsList = new ArrayList<>();

    public AbstractBannerView(@NonNull Context context) {
        this(context, null);
    }

    public AbstractBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public AbstractBannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    /**
     * 处理banner的点击事件
     *
     * @param banner 被点击的banner
     */
    protected abstract void handleOnBannerClick(@NonNull T banner);

    /**
     * 初始化布局文件。
     * 在这个函数中，需要给mVpPager和mLlDots赋值。
     */
    protected abstract void initLayout();

    /**
     * 初始化dot
     *
     * @param dot     要处理的dot
     * @param params  LinearLayout.LayoutParams
     * @param isFirst 是否是第一个dot
     */
    protected abstract void initDot(View dot, LinearLayout.LayoutParams params, boolean isFirst);

    /**
     * 初始化SimpleDraweeView
     *
     * @param draweeView 要处理的SimpleDraweeView
     * @param params     LayoutParams
     */
    protected abstract void initDraweeView(SimpleDraweeView draweeView, LayoutParams params);

    /**
     * 显示banner
     *
     * @param draweeView 需要显示banner的SimpleDraweeView
     * @param banner     需要被显示的banner
     */
    protected abstract void showBanner(SimpleDraweeView draweeView, T banner);

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        cancel();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentItem = position;
        selectDot(mCurrentItem % mBannerList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                start();
                break;
            default:
                cancel();
                break;
        }
    }

    /**
     * 初始化
     */
    private void init() {
        initLayout();

        if (mVpPager == null) {
            LogUtils.error(TAG, "mVpPager == null");
            throw new IllegalStateException("mVpPager should not be null");
        }

        if (mLlDots == null) {
            LogUtils.error(TAG, "mLlDots == null");
            throw new IllegalStateException("mLlDots should not be null");
        }

        mVpPager.setAdapter(mAdapter);
        mVpPager.addOnPageChangeListener(this);
    }

    /**
     * 更新dot
     *
     * @param size 新的banner数量
     */
    private void updateDots(int size) {
        while (mDotsList.size() < size) {
            View dot = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            initDot(dot, params, mDotsList.isEmpty());
            dot.setLayoutParams(params);
            mDotsList.add(dot);
        }

        while (mDotsList.size() > size) {
            mDotsList.remove(mDotsList.size() - 1);
        }

        int count = mLlDots.getChildCount();
        int addSize = size - count;
        if (addSize > 0) {
            //说明dot数量不够，需要多加几个
            for (int i = 0; i < addSize; i++) {
                mLlDots.addView(mDotsList.get(count + i));
            }
        } else if (addSize < 0) {
            //说明dot数量太多，需要删除几个
            int removeSize = -addSize;
            mLlDots.removeViews(count - removeSize, removeSize);
        }
    }

    /**
     * 更新DraweeView
     *
     * @param size 新的banner数量
     */
    private void updateDraweeView(int size) {
        size = (size == 2 ? 4 : size);

        while (mDraweeViewList.size() < size) {
            SimpleDraweeView draweeView = new SimpleDraweeView(getContext());
            draweeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LayoutParams params = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            draweeView.setLayoutParams(params);

            initDraweeView(draweeView, params);

            mDraweeViewList.add(draweeView);
        }

        while (mDraweeViewList.size() > size) {
            mDraweeViewList.remove(mDraweeViewList.size() - 1);
        }

    }

    /**
     * 设置被选中的dot
     *
     * @param position 当前被选中position
     */
    private void selectDot(int position) {
        for (int i = 0; i < mDotsList.size(); i++) {
            if (i == position) {
                mDotsList.get(i).setSelected(true);
            } else {
                mDotsList.get(i).setSelected(false);
            }
        }
    }

    /**
     * 取消自动循环
     */
    private synchronized void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    /**
     * 开始自动循环
     */
    private synchronized void start() {
        if (mCancelled) {
            mCancelled = false;
            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), AUTO_PLAY_DELAY_TIME);
        }
    }

    /**
     * 更新banner数据
     *
     * @param list banner数据
     */
    public void updateData(List<T> list) {
        LogUtils.debug(TAG, "updateData START :" + System.currentTimeMillis());
        if (ListUtils.isEmpty(list)) {
            return;
        }

        cancel();

        mBannerList.clear();
        mBannerList.addAll(list);
        mAdapter.notifyDataSetChanged();

        updateDots(list.size());
        updateDraweeView(list.size());

        if (mCurrentItem <= 0) {
            mCurrentItem = mBannerList.size() * 400;
        }
        mVpPager.setCurrentItem(mCurrentItem, false);
        selectDot(mCurrentItem % mBannerList.size());

        start();

        LogUtils.debug(TAG, "updateData END :" + System.currentTimeMillis());
    }

    private class _Adapter extends PagerAdapter implements OnClickListener {
        @Override
        public int getCount() {
            if (ListUtils.isEmpty(mBannerList)) {
                return 0;
            } else if (mBannerList.size() == 1) {
                return 1;
            } else {
                return Integer.MAX_VALUE;
            }
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LogUtils.debug(TAG, "instant start :" + System.currentTimeMillis());

            //根据当前的position得到实际数据中的index
            int bannerPos = position % mBannerList.size();
            int viewPos = position % mDraweeViewList.size();

            //取出view进行复用
            SimpleDraweeView draweeView = mDraweeViewList.get(viewPos);
            T banner = mBannerList.get(bannerPos);
            if (banner != null) {
                showBanner(draweeView, banner);
                draweeView.setTag(banner);
                draweeView.setOnClickListener(this);
            }

            container.removeView(draweeView);
            container.addView(draweeView);

            LogUtils.debug(TAG, "instant end :" + System.currentTimeMillis());

            return draweeView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //这个函数中不能销毁view，因为view是要被复用的
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onClick(View v) {
            T bannerBean = (T) v.getTag();
            if (bannerBean != null) {
                handleOnBannerClick(bannerBean);
            }
        }
    }

    /**
     * banner的轮播。采用软链接是为了防止内存泄露
     */
    private static class _Handler extends Handler {
        private WeakReference<AbstractBannerView> mBannerRef;

        _Handler(AbstractBannerView mBannerView) {
            mBannerRef = new WeakReference<>(mBannerView);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mBannerRef.get() == null) {
                return;
            }

            AbstractBannerView bannerView = mBannerRef.get();
            if (bannerView.mCancelled) {
                return;
            }

            if (!bannerView.mBannerList.isEmpty()) {
                bannerView.mVpPager.setCurrentItem(bannerView.mCurrentItem++);
            } else {
                bannerView.cancel();
                return;
            }

            removeMessages(MSG);
            sendMessageDelayed(obtainMessage(MSG), AUTO_PLAY_DELAY_TIME);
        }
    }

}
