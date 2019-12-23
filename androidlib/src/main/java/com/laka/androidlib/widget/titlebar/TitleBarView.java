package com.laka.androidlib.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.androidlib.R;
import com.laka.androidlib.interfaces.ICustomViewInit;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.ResourceUtils;
import com.laka.androidlib.util.SoftKeyBoardUtil;

import java.lang.reflect.TypeVariable;

/**
 * @Author:Rayman
 * @Date:2019/1/8
 * @Description: 通用的标题栏
 */
public class TitleBarView<T> extends RelativeLayout implements ICustomViewInit<T>, ITitleBarView {

    /**
     * description:UI设置
     **/
    private RelativeLayout mRlContainer;
    private TextView mTvTitle;
    private LinearLayout mLlTitleLeft;
    private ImageView mLeftIconIv;
    private TextView mTvBarLeft;

    private LinearLayout mLlTitleRight;
    private ImageView mRightIconIv;
    private TextView mTvBarRight;

    private View mDivider;

    /**
     * description:控件设置
     * 左侧Icon在文字的位置，右侧Icon在文字的位置
     **/
    private int leftIconPosition;
    private int rightIconPosition;
    private boolean leftIconVisible, rightIconVisible, dividerVisible;
    private String leftText, title, rightText;
    private int leftTextColor, titleTextColor, rightTextColor, backgroundColor;
    private int defaultColorWhite = ResourceUtils.getColor(R.color.white);
    private int leftTextSize, titleTextSize, rightTextSize;
    private int defaultTextSize, defaultTextColor;

    public TitleBarView(Context context) {
        super(context);
        initViews(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    @Override
    public void initViews(final Context context, @Nullable AttributeSet attrs) {

        if (isInEditMode()) {
            return;
        }

        LayoutInflater.from(context).inflate(R.layout.layout_title_bar_view, this);

        mRlContainer = findViewById(R.id.rl_title_container);
        mTvTitle = findViewById(R.id.tv_title);
        mLlTitleLeft = findViewById(R.id.ll_title_bar_left);
        mLeftIconIv = findViewById(R.id.iv_left_icon);
        mTvBarLeft = findViewById(R.id.tv_title_bar_lef);
        mLlTitleRight = findViewById(R.id.ll_title_bar_right);
        mRightIconIv = findViewById(R.id.iv_right_icon);
        mTvBarRight = findViewById(R.id.tv_title_bar_right);
        mDivider = findViewById(R.id.divider_line);

        mLlTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context != null && context instanceof Activity) {
                    SoftKeyBoardUtil.hideInputMethod(((Activity) context), mTvTitle);
                    ((Activity) context).finish();
                }
            }
        });

        initProperty(context, attrs);
    }

    @Override
    public void initProperty(final Context context, AttributeSet attrs) {
        defaultTextSize = ResourceUtils.getDimen(R.dimen.normal_font_size);
        defaultTextColor = ResourceUtils.getColor(R.color.color_font);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);
            leftText = typedArray.getString(R.styleable.TitleBarView_left_text);
            title = typedArray.getString(R.styleable.TitleBarView_title_text);
            rightText = typedArray.getString(R.styleable.TitleBarView_right_text);

            leftTextSize = typedArray.getDimensionPixelOffset(R.styleable.TitleBarView_left_text_size, defaultTextSize);
            titleTextSize = typedArray.getDimensionPixelOffset(R.styleable.TitleBarView_title_text_size, defaultTextSize);
            rightTextSize = typedArray.getDimensionPixelOffset(R.styleable.TitleBarView_right_text_size, defaultTextSize);

            backgroundColor = typedArray.getColor(R.styleable.TitleBarView_back_ground_color, defaultColorWhite);
            leftTextColor = typedArray.getColor(R.styleable.TitleBarView_left_text_color, defaultTextColor);
            titleTextColor = typedArray.getColor(R.styleable.TitleBarView_title_text_color, defaultTextColor);
            rightTextColor = typedArray.getColor(R.styleable.TitleBarView_right_text_color, defaultTextColor);

            leftIconVisible = typedArray.getBoolean(R.styleable.TitleBarView_left_icon_visibility, true);
            rightIconVisible = typedArray.getBoolean(R.styleable.TitleBarView_right_icon_visibility, false);
            dividerVisible = typedArray.getBoolean(R.styleable.TitleBarView_bottom_divider_visibility, true);


            if (!TextUtils.isEmpty(title)) {
                mTvTitle.setText(title);
            } else {
                mTvTitle.setText("");
            }

            mTvTitle.setTextColor(titleTextColor);
            setBackground(new ColorDrawable(backgroundColor));
//            LogUtils.info("输出当前background的颜色：" + backgroundColor + "\n输出白色颜色：" + defaultColorWhite
//                    + "\n输出title颜色：" + titleTextColor + "\n默认文字颜色：" + defaultTextColor);
            setRightIconVisibility(rightIconVisible ? VISIBLE : GONE);
            setLeftIconVisibility(leftIconVisible ? VISIBLE : GONE);
            mDivider.setVisibility(dividerVisible ? VISIBLE : GONE);
            typedArray.recycle();
        }

        leftIconPosition = POSITION_LEFT;
        rightIconPosition = POSITION_RIGHT;
    }

    @Override
    public void updateData(T data) {

    }

    @Override
    public ITitleBarView setBackGroundColor(int resColor) {
        this.backgroundColor = ResourceUtils.getColor(resColor);
        mRlContainer.setBackgroundColor(backgroundColor);
        return this;
    }

    @Override
    public ITitleBarView setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    @Override
    public ITitleBarView setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize);
        return this;
    }

    @Override
    public ITitleBarView setTitleTextColor(int resColor) {
        this.titleTextColor = ResourceUtils.getColor(resColor);
        mTvTitle.setTextColor(titleTextColor);
        return this;
    }

    @Override
    public ITitleBarView setLeftText(String text) {
        mTvBarLeft.setText(text);
        mTvBarLeft.setVisibility(VISIBLE);
        return this;
    }

    @Override
    public ITitleBarView setLeftTextSize(int textSize) {
        return this;
    }

    @Override
    public ITitleBarView setLeftTextColor(int resColor) {
        return this;
    }

    @Override
    public ITitleBarView setLeftIcon(int resId) {
        mLeftIconIv.setImageResource(resId);
        mLeftIconIv.setVisibility(VISIBLE);
        return this;
    }

    @Override
    public ITitleBarView setLeftIcon(Drawable drawable) {
        mLeftIconIv.setImageDrawable(drawable);
        mLeftIconIv.setVisibility(VISIBLE);
        return this;
    }

    @Override
    public ITitleBarView setLeftIconPosition(int position) {
        leftIconPosition = position;
        mLlTitleLeft.removeAllViews();
        if (leftIconPosition == POSITION_LEFT) {
            mLlTitleLeft.addView(mLeftIconIv);
            mLlTitleLeft.addView(mTvBarLeft);
        } else {
            mLlTitleLeft.addView(mTvBarLeft);
            mLlTitleLeft.addView(mLeftIconIv);
        }
        return this;
    }

    @Override
    public ITitleBarView setLeftCompoundMargin(int margin) {
        if (mTvBarLeft.getVisibility() == VISIBLE && mLeftIconIv.getVisibility() == VISIBLE) {
            MarginLayoutParams params;
            if (leftIconPosition == POSITION_LEFT) {
                params = (MarginLayoutParams) mLeftIconIv.getLayoutParams();
                params.rightMargin = margin;
            } else {
                params = (MarginLayoutParams) mTvBarLeft.getLayoutParams();
                params.rightMargin = margin;
            }
        }
        return this;
    }

    @Override
    public ITitleBarView setLeftMargin(int marginLeft, int marginTop, int marginRight, int marginBottom) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) mLlTitleLeft.getLayoutParams();
        marginLayoutParams.leftMargin = marginLeft;
        marginLayoutParams.topMargin = marginTop;
        marginLayoutParams.rightMargin = marginRight;
        marginLayoutParams.bottomMargin = marginBottom;
        return this;
    }

    @Override
    public void setLeftTextVisibility(int visibility) {
        mTvBarLeft.setVisibility(visibility);
    }

    @Override
    public void setLeftIconVisibility(int visibility) {
        mLeftIconIv.setVisibility(visibility);
    }

    @Override
    public ITitleBarView setOnLeftClickListener(View.OnClickListener onLeftIconClickListener) {
        mLlTitleLeft.setOnClickListener(onLeftIconClickListener);
        mLeftIconIv.setEnabled(false);
        return this;
    }

    @Override
    public ITitleBarView setRightText(String text) {
        mTvBarRight.setText(text);
        mTvBarRight.setVisibility(VISIBLE);
        return this;
    }

    @Override
    public ITitleBarView setRightTextSize(int textSize) {
        this.rightTextSize = textSize;
        mTvBarRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, rightTextSize);
        return this;
    }

    @Override
    public ITitleBarView setRightTextColor(int resColor) {
        this.rightTextColor = ResourceUtils.getColor(resColor);
        mTvBarRight.setTextColor(rightTextColor);
        return this;
    }

    @Override
    public ITitleBarView setRightIcon(int resId) {
        mRightIconIv.setImageResource(resId);
        mRightIconIv.setVisibility(VISIBLE);
        return this;
    }

    @Override
    public ITitleBarView setRightIcon(Drawable drawable) {
        mRightIconIv.setImageDrawable(drawable);
        mRightIconIv.setVisibility(VISIBLE);
        return this;
    }

    @Override
    public ITitleBarView setRightIconPosition(int position) {
        rightIconPosition = position;
        mLlTitleRight.removeAllViews();
        if (rightIconPosition == POSITION_LEFT) {
            mLlTitleRight.addView(mRightIconIv);
            mLlTitleRight.addView(mTvBarRight);
        } else {
            mLlTitleRight.addView(mTvBarRight);
            mLlTitleRight.addView(mRightIconIv);
        }
        return this;
    }

    @Override
    public ITitleBarView setRightCompoundMargin(int margin) {
        if (mTvBarRight.getVisibility() == VISIBLE && mRightIconIv.getVisibility() == VISIBLE) {
            MarginLayoutParams params;
            if (rightIconPosition == POSITION_LEFT) {
                params = (MarginLayoutParams) mRightIconIv.getLayoutParams();
                params.rightMargin = margin;
            } else {
                params = (MarginLayoutParams) mTvBarRight.getLayoutParams();
                params.rightMargin = margin;
            }
        }
        return this;
    }

    @Override
    public ITitleBarView setRightMargin(int marginLeft, int marginTop, int marginRight, int marginBottom) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) mLlTitleRight.getLayoutParams();
        marginLayoutParams.leftMargin = marginLeft;
        marginLayoutParams.topMargin = marginTop;
        marginLayoutParams.rightMargin = marginRight;
        marginLayoutParams.bottomMargin = marginBottom;
        return this;
    }

    @Override
    public void setRightTextVisibility(int visibility) {
        mTvBarRight.setVisibility(visibility);
    }

    @Override
    public void setRightIconVisibility(int visibility) {
        mRightIconIv.setVisibility(visibility);
    }

    @Override
    public ITitleBarView setOnRightClickListener(View.OnClickListener onRightClickListener) {
        mLlTitleRight.setOnClickListener(onRightClickListener);
        mRightIconIv.setEnabled(false);
        return this;
    }

    @Override
    public ITitleBarView showDivider(boolean isShow) {
        dividerVisible = isShow;
        mDivider.setVisibility(dividerVisible ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public View getLeftView() {
        return mLlTitleLeft;
    }

    @Override
    public View getRightView() {
        return mLlTitleRight;
    }
}
