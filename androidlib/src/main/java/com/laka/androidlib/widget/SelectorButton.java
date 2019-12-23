package com.laka.androidlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.laka.androidlib.R;
import com.laka.androidlib.util.SystemUtils;
import com.laka.androidlib.util.screen.ScreenUtils;

/**
 * 按压式Button，自动计算按压背景色或者自行设置
 * Created by Lyf on 2017/3/14.
 */
public class SelectorButton extends android.support.v7.widget.AppCompatTextView {

    private Paint mPaint;// 画笔
    private RectF mRect;// 圆角矩形
    private String mDrawText;// 绘制的文本
    private String pressTextColor;// 自定义按钮的按压文本颜色
    private String backGroundColor; // 自定义按钮的背景颜色(字符串)
    private String pressColor; // 自定义按钮的按压背景颜色(字符串)
    private String mMaskColor; //蒙版颜色（最上层图层）
    private String mBorderColor;  //边框颜色
    private boolean isShowBorder; //是否显示边框

    private Context mContext;

    private float mTxtConterX = 0f;  //绘制的文本的中心位置，用于辅助绘制drawableLeft
    private float mBorderWidth = 0f;  //边框宽度
    private float mRadius = 0f;// 圆角角度
    private float mTopLeftRadius = 0f; // 左上圆角角度
    private float mBottomLeftRadius = 0f; // 左下圆角角度
    private float mTopRightRadius = 0f; // 右上圆角角度
    private float mBottomRightRadius = 0f; // 右下圆角角度
    private int measureWidth;// 按钮的宽度
    private int measureHeight;// 按钮的高度
    private boolean isBold = false; // 是否显示粗体
    private boolean isRadius = true;// 是否显示圆角
    private boolean showShadow = false;//是否显示按压层
    private boolean isStroke; // 是否中空(按钮中间为透明色)
    private boolean onlyStrokeColor;// 按压时是否只改变描边颜色
    private final static String TAG = "SelectorButton";
    private final static int defaultWidth = ScreenUtils.dp2px(40);// 默认的高度
    private final static int defaultHeight = 600;// 默认的宽度
    private final static int defaultBorderWidth = ScreenUtils.dp2px(1);// 默认的宽度
    private final static String defaultText = "确定";// 默认的文本
    private final static String defaultColor = "#ff00ff00"; // 默认的背景颜色
    private final static String defaultTextColor = "#ff000000"; // 默认的背景颜色
    private final static String defaultPressColor = "#45000000"; // 默认的按压层背景颜色
    private final static String defaultMaskColor = "#45000000"; // 默认的蒙版颜色
    private final static String defaultBorderColor = "#45000000"; // 默认的蒙版颜色

    private int leftDrawableRes;
    private Drawable leftDrawable;
    private int backgroundDrawableRes;
    private Drawable backgroundDrawable;
    private boolean hasBackgroundDrawable;
    private int drawablePadding = 0;
    private int mPaddingLeft = ScreenUtils.dp2px(0);
    private int mPaddingTop = ScreenUtils.dp2px(10);
    private int mPaddingRight = ScreenUtils.dp2px(0);
    private int mPaddingBottom = ScreenUtils.dp2px(10);

    private Typeface boldType = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
    private Typeface normalType = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);

    public SelectorButton(Context context) {
        super(context);
        init(context, null);
    }

    public SelectorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SelectorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureWidth = getMeasuredWidth() > 0 ? getMeasuredWidth() : defaultWidth;
        measureHeight = getMeasuredHeight() > 0 ? getMeasuredHeight() : defaultHeight;
        setMeasuredDimension(measureWidth, measureHeight);
        setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }

    // 初始化相关对象
    protected void init(Context context, AttributeSet attrs) {

        this.mContext = context;

        // 初始化相关变量
        mRect = new RectF();
        mPaint = new Paint();

        // 设置背景为透明,避免用户使用android:backGround进行设置背景
        setBackgroundColor(Color.TRANSPARENT);
        // 拿到android:text的文本
        mDrawText = getText().toString();
        // 将android:text清空,不然,Button会再绘一个相同的文本
        setText("");
        // 如果没有设置android:text文本，显示默认的确定文本。
        if (TextUtils.isEmpty(mDrawText)) {
            mDrawText = defaultText;
        }

        // 属性相关
        if (attrs != null) {

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectorButton);
            mRadius = typedArray.getDimension(R.styleable.SelectorButton_bg_radius, 0f);
            mTopLeftRadius = typedArray.getDimension(R.styleable.SelectorButton_topLeftRadius, 0f);
            mTopRightRadius = typedArray.getDimension(R.styleable.SelectorButton_topRightRadius, 0f);
            mBottomLeftRadius = typedArray.getDimension(R.styleable.SelectorButton_bottomLeftRadius, 0f);
            mBottomRightRadius = typedArray.getDimension(R.styleable.SelectorButton_bottomRightRadius, 0f);
            isRadius = typedArray.getBoolean(R.styleable.SelectorButton_is_radius, true);
            isStroke = typedArray.getBoolean(R.styleable.SelectorButton_stroke, false);
            isBold = SystemUtils.getAttributeBooleanValue(attrs, R.attr.bold, false);
            backGroundColor = typedArray.getString(R.styleable.SelectorButton_bga_color);
            pressColor = typedArray.getString(R.styleable.SelectorButton_press_color);
            pressTextColor = typedArray.getString(R.styleable.SelectorButton_press_text_color);
            onlyStrokeColor = typedArray.getBoolean(R.styleable.SelectorButton_onlyStrokeColor, false);
            leftDrawableRes = typedArray.getResourceId(R.styleable.SelectorButton_android_drawableLeft, 0); //系统定义的属性 drawable_left
            if (leftDrawableRes != 0) {
                leftDrawable = getResources().getDrawable(typedArray.getResourceId(R.styleable.SelectorButton_android_drawableLeft, 0));
            }
            drawablePadding = typedArray.getDimensionPixelOffset(R.styleable.SelectorButton_android_drawablePadding, 10);
            backgroundDrawableRes = typedArray.getResourceId(R.styleable.SelectorButton_bga_drawable, 0);
            if (backgroundDrawableRes != 0) {
                backgroundDrawable = getResources().getDrawable(typedArray.getResourceId(R.styleable.SelectorButton_bga_drawable, 0));
                hasBackgroundDrawable = true;
            }

            //todo xhh修改
            mMaskColor = typedArray.getString(R.styleable.SelectorButton_mask_color);
            mBorderColor = typedArray.getString(R.styleable.SelectorButton_border_color);
            isShowBorder = typedArray.getBoolean(R.styleable.SelectorButton_is_show_border, false);
            mBorderWidth = typedArray.getDimension(R.styleable.SelectorButton_border_width, defaultBorderWidth);
            if (TextUtils.isEmpty(mMaskColor)) {
                mMaskColor = defaultMaskColor;
            }
            if (TextUtils.isEmpty(mBorderColor)) {
                mBorderColor = defaultBorderColor;
            } else {
                isShowBorder = true;
            }

            // 如果没有设置颜色,就用默认颜色
            if (backGroundColor == null) {
                backGroundColor = defaultColor;
            }
            // 如果没有设置文本的按压颜色,就用默认颜色
            if (pressTextColor == null) {
                pressTextColor = defaultTextColor;
            }

            // 如果没设置按压效果颜色,根据所设置的颜色，自动计算出按压层颜色
            if (pressColor == null) {
                pressColor = getShadowColor(defaultPressColor);
            }

            // mRadius优先使用
            if (mRadius > 0) {
                mTopLeftRadius = mRadius;
                mTopRightRadius = mRadius;
                mBottomLeftRadius = mRadius;
                mBottomRightRadius = mRadius;
            }

            typedArray.recycle();
        }

        // 这样，才能改默认颜色
        if (!isEnabled()) {
            setEnabled(isEnabled());
        }

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateText(getText().toString());
            }
        });
    }

    // 绘制UI
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置圆角矩形的长宽为默认或用户设置的长宽
        mRect.set(0, 0, measureWidth, measureHeight);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(backGroundColor));
        mPaint.setAntiAlias(true);
        // 设置中空效果
        if (isStroke) {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        if (hasBackgroundDrawable) {
            //绘制按钮背景图片，只要设置了背景图片，就不会再采用背景色方案
            drawBackDrawable(canvas);
        } else {
            // 绘制按钮背景颜色
            drawRoundRect(canvas, mRect, mPaint);
        }
        // 绘制文本
        drawText(canvas, getCurrentTextColor());
        //是否需要绘制边框
        if (isShowBorder) {
            Paint maskPaint = new Paint();
            maskPaint.setColor(Color.parseColor(mBorderColor));
            maskPaint.setStrokeWidth(mBorderWidth);
            maskPaint.setStyle(Paint.Style.STROKE);
            maskPaint.setAntiAlias(true);
            // 中空的，按压层才需要小于边框线
            RectF rectF = new RectF();
            //因为设置了边框，所以矩形的位置要稍微改变，不然会有部分边框内容超出控件大小范围内，超出的部分就看不见
            rectF.set(mBorderWidth / 2, mBorderWidth / 2, measureWidth - mBorderWidth / 2, measureHeight - mBorderWidth / 2);
            drawRoundRect(canvas, rectF, maskPaint);
        }

        //绘制drawLeft图片
        drawLeftDrawable(canvas);
        //如果是按压状态，则在顶部图层添加蒙版
        if (showShadow) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.parseColor(mMaskColor));
            drawRoundRect(canvas, mRect, mPaint);
        }
    }

    /**
     * 绘制按钮背景颜色
     */
    private void drawRoundRect(Canvas canvas, RectF rectF, Paint paint) {
        // 开始绘制圆角矩形
        if (isRadius) {
            float[] cornersRadius = {mTopLeftRadius, mTopLeftRadius,
                    mTopRightRadius, mTopRightRadius,
                    mBottomRightRadius, mBottomRightRadius,
                    mBottomLeftRadius, mBottomLeftRadius};
            Path path = new Path();
            path.addRoundRect(rectF, cornersRadius, Path.Direction.CW);
            canvas.drawPath(path, paint);
        } else {
            canvas.drawRoundRect(rectF, 0, 0, paint);
        }
    }

    /**
     * 绘制按钮背景图片
     */
    private void drawBackDrawable(Canvas canvas) {
        if (backgroundDrawable != null) {
            backgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawBitmap(((BitmapDrawable) backgroundDrawable).getBitmap(), 0, 0, mPaint);
        }
    }

    // 绘制文本
    private void drawText(Canvas canvas, int color) {
        // 设置文本的样式
        mPaint.setStyle(Paint.Style.FILL);// 充满
        mPaint.setColor(color);
        mPaint.setFakeBoldText(isBold);
        mPaint.setTextSize(getTextSize());
        mPaint.setTextAlign(Paint.Align.CENTER);
        // 设置文本居中
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //为基线到字体上边框的距离
        float top = fontMetrics.top;
        //为基线到字体下边框的距离
        float bottom = fontMetrics.bottom;
        //基线中间点的y轴计算公式
        int baseLineY = (int) (mRect.centerY() - top / 2 - bottom / 2);
        mTxtConterX = mRect.centerX();
        if (leftDrawable != null) {
            mTxtConterX = mRect.centerX() + (leftDrawable.getIntrinsicWidth() + drawablePadding) / 2;
        }
        canvas.drawText(mDrawText, mTxtConterX, baseLineY, mPaint);
    }

    /**
     * 添加绘制DrawableLeft
     * （后面根据实际情况添加top right bottom）
     *
     * @param canvas
     */
    private void drawLeftDrawable(Canvas canvas) {
        if (leftDrawable != null) {
            leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());
            //float bitmapLeft =  measureWidth / 2 - mPaint.measureText(mDrawText) / 2 - leftDrawable.getIntrinsicWidth() - drawablePadding;
            float bitmapLeft = mTxtConterX - mPaint.measureText(mDrawText) / 2 - leftDrawable.getIntrinsicWidth() - drawablePadding;
            float bitmapTop = measureHeight / 2 - leftDrawable.getIntrinsicHeight() / 2;
            canvas.drawBitmap(((BitmapDrawable) leftDrawable).getBitmap(), bitmapLeft, bitmapTop, mPaint);
        }
    }

    // 触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 按压时改变颜色
                showShadow = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 松开时恢复颜色
                showShadow = false;
                invalidate();
                break;
        }
        return true;
    }

    private int enableColor = -1;
    private String tempBGColor = null; // 临时颜色码

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            if (tempBGColor != null) {
                backGroundColor = tempBGColor;
                tempBGColor = null;
            }
            if (enableColor != -1) {
                setTextColor(enableColor);
            }
        } else {
            tempBGColor = backGroundColor;
            backGroundColor = "#d6d8db";
            enableColor = getCurrentTextColor();
            setTextColor(Color.WHITE);
        }
        invalidate();
    }

    public void setShowShadow(boolean showShadow) {
        this.showShadow = showShadow;
        invalidate();
    }

    //
    // 计算按压层颜色
    public static String getShadowColor(String color) {
        if (color == null) {
            return color;
        }
        String[] colors = new String[3];
        if (color.length() > 7) {
            // 暂不处理透明值[1-3]
            colors[0] = color.substring(3, 5);
            colors[1] = color.substring(5, 7);
            colors[2] = color.substring(7, 9);
        } else {
            colors[0] = color.substring(1, 3);
            colors[1] = color.substring(3, 5);
            colors[2] = color.substring(5, 7);
        }
        color = "#";
        for (int i = 0; i < 3; ++i) {
            int temp10Hex = to10Hex(colors[i]);
            if (temp10Hex >= 30) {
                temp10Hex = temp10Hex - 30;
            }
            color += temp10Hex < 16 ? "0" + to16Hex(temp10Hex) : to16Hex(temp10Hex);
        }
        return color;
    }

    // 10进制转16进制
    public static String to16Hex(int num) {
        return Integer.toHexString(num);
    }

    // 16进制转10进制
    public static int to10Hex(String num) {
        return Integer.parseInt(num, 16);
    }

    // 简化打印Log
    private static void log(String msg) {
        Log.d(TAG, msg);
    }

    // 设置文本
    public void updateText(String text) {
        this.mDrawText = text;
        invalidate();
    }

    // 设置背景色
    public void setBackGroundColor(String backGroundColor) {
        this.backGroundColor = backGroundColor;
        // 背景颜色改的时候,按压颜色也要改
        if (!isStroke) {
            pressColor = getShadowColor(backGroundColor);
        }
        invalidate();
    }

    public String getDrawText() {
        return mDrawText;
    }

    // 设置背景色
    public void setBackGroundColor(int color) {
        this.backGroundColor = "#" + Integer.toHexString(mContext.getResources().getColor(color));
        // 背景颜色改的时候,按压颜色也要改
        if (!isStroke) {
            pressColor = getShadowColor(backGroundColor);
        }
        invalidate();
    }


    public void setBoldType(boolean isBold) {
        if (isBold) {
            mPaint.setTypeface(boldType);
        } else {
            mPaint.setTypeface(normalType);
        }
        invalidate();
    }

    public void setBgaColor(String color) {
        backGroundColor = color;
        invalidate();
    }

}
