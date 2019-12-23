package com.laka.androidlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.androidlib.R;
import com.laka.androidlib.listener.OnDebounceClickListener;
import com.laka.androidlib.util.StringUtils;
import com.laka.androidlib.util.anim.AnimationHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lyf
 * @CreateTime 2018/1/2
 * @Description 先进先出-垂直跑马灯，直到最后一条停留。
 **/
public class MarqueeView extends RelativeLayout {

    private final static String TAG = MarqueeView.class.getSimpleName();
    public static final int POINT_MAX_THREAD_HOLD = 3;

    // 当前是否正在轮播
    private boolean isRunning = false;
    // 用于轮播的两个View
    private TextView mFirstView;
    private TextView mSecondView;
    // View的停留时间(毫秒)
    private int mInterval = 5000;
    // 动画的执行时间(毫秒)
    private int animDuration = 1000;
    // 当前正在显示的是mFirstView或mSecondView(whichOne % 2 == 0则意味着，当前显示的是mFirstView)
    private int whichOne = 0;
    // 计时器
    private CountDownTimer mCountDownTimer;
    // 轮播池
    private List<String> mAnnouncePools = new ArrayList<>();
    // 当前下标
    private int mIndexInPools;

    // 是否停止轮播
    private boolean isStopRunning = false;

    private boolean isRichHtml = false;

    private Context mContext;

    public MarqueeView(Context context) {
        super(context);
        initView(context);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_marquee, this);
        mFirstView = findViewById(R.id.tv_pre_view);
        mSecondView = findViewById(R.id.tv_next_view);
        mFirstView.setEllipsize(TextUtils.TruncateAt.END);
        mSecondView.setEllipsize(TextUtils.TruncateAt.END);
        mFirstView.setSingleLine(true);
        mSecondView.setSingleLine(true);
    }

    /**
     * 启动轮播
     */
    public void start() {

        if (isRunning || mAnnouncePools.size() == 0) {
            return;
        }
        showNext();
    }

    /**
     * 添加公告进去
     *
     * @param text
     */
    public void addAnnounce(String text) {
        if (text != null) {
            mAnnouncePools.add(text);
            start();
        }
    }

    /**
     * 添加公告进去
     *
     * @param text
     */
    public void addAnnounce(String text, boolean isRichHtml) {
        if (text != null) {
            mAnnouncePools.add(text);
            this.isRichHtml = isRichHtml;
            start();
        }
    }

    /**
     * 批量添加公告进去
     *
     * @param text
     */
    public void addAnnounces(List<String> text) {
        addAnnounces(text, false);
    }

    /**
     * 批量添加公告进去
     *
     * @param text
     */
    public void addAnnounces(List<String> text, boolean isRichHtml) {
        if (text != null) {
            this.isRichHtml = isRichHtml;
            mAnnouncePools.addAll(text);
            start();
        }
    }

    public void showNext() {

        if (mAnnouncePools.size() > 0 && !isStopRunning) {

            // 一条时不轮播
//            if (mAnnouncePools.size() == 1) {
//                isStopRunning = true;
//            }

            // 轮播完，重置位置，从0开始。
            if (mIndexInPools >= mAnnouncePools.size()) {
                mIndexInPools = 0;
            }

            String text = mAnnouncePools.get(mIndexInPools++);
            Animation upHide = AnimationHelper.getTranslateUpHide(animDuration);
            Animation upVisible = AnimationHelper.getTranslateUpVisible(animDuration);
            upHide.setFillAfter(true);
            upVisible.setFillAfter(true);
            upHide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (whichOne % 2 == 0) {
                        mSecondView.setText("");
                    } else {
                        mFirstView.setText("");
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            // 开始倒计时
            startCountDownTimer();

            if (whichOne % 2 == 0) {
                setText(mFirstView, text);
                mFirstView.startAnimation(upVisible);
                mSecondView.startAnimation(upHide);
            } else {
                setText(mSecondView, text);
                mFirstView.startAnimation(upHide);
                mSecondView.startAnimation(upVisible);
            }

        }

    }

    //  设置文本
    private void setText(TextView showView, final String text) {
        if (isRichHtml) {
            showView.setText(Html.fromHtml(StringUtils.convertSpanStyleStr2HtmlStr(text)));
        } else {
            showView.setText(text);
        }
        showView.setOnClickListener(new OnDebounceClickListener() {
            @Override
            public void handleClickEvent(View v) {

            }
        });
    }

    // 设置倒计时
    private void startCountDownTimer() {

        if (mCountDownTimer != null) { // 停止倒计时
            mCountDownTimer.cancel();
        }

        isRunning = true;

        mCountDownTimer = new CountDownTimer(mInterval, mInterval / 2) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                isRunning = false;
                ++whichOne;
                showNext();
            }
        };
        mCountDownTimer.start();
    }

    /**
     * 设置停留时间
     *
     * @param mInterval 毫秒
     */
    public void setInterval(int mInterval) {
        this.mInterval = mInterval;
    }

    /**
     * 设置动画时间
     *
     * @param animDuration 毫秒
     */
    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }

    /**
     * 清空重置
     */
    public void reset() {
        if (mCountDownTimer != null) { // 停止倒计时
            try {
                mCountDownTimer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mAnnouncePools.clear();
        isRunning = false;
        mFirstView.setText("");
        mSecondView.setText("");
        whichOne = 0;
    }

    public void updatemAnnounce(List<String> announces) {
        this.mAnnouncePools = announces;
    }

    public int getCurrentPosition() {
        return mIndexInPools;
    }
}
