package com.laka.androidlib.widget;

import android.graphics.Color;
import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.Gravity;


import com.laka.androidlib.util.screen.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/9/7
 * @Description:垂直跑马灯TextView
 */

public class VerticalMarqueeView extends android.support.v7.widget.AppCompatTextView {

    public static final int POINT_MAX_THREAD_HOLD = 3;
    public static final int DURATION_SCROLL = 5000;
    public static final int DURATION_ANIMATOR = 1000;
    private int color = Color.WHITE;
    private int textSize = 36;
    private ArrayList<String> mData = new ArrayList<>();

    private int width;
    private int height;
    private int centerX;
    private int centerY;

    //绘制的文字内容
    private List<TextBlock> blocks = new ArrayList<TextBlock>(2);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean isStopScroll = false;

    /**
     * description:图片控制--废弃
     **/
    private Bitmap mBitmap;
    private float drawableLeftMargin = ScreenUtils.dp2px(10);
    private Gravity gravity;


    /**
     * description:即使外部提供了一个列表，这里还是提供一个变量供外部控制
     * 当前滚动的position
     * 默认是跨越一个位置滚动
     **/
    private int startPosition = 0;
    private int autoCount = 1;
    private int endPosition = startPosition + autoCount;
    private OnScrollListener onScrollListener;

    /**
     * description:控制滚动
     **/
    private CountDownTimer countDownTimer;
    private ValueAnimator animator;

    public VerticalMarqueeView(Context context) {
        this(context, null);
    }

    public VerticalMarqueeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalMarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        centerX = width / 2;
        centerY = height / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).draw(canvas);
        }
    }

    private void initView() {
        textSize = ScreenUtils.sp2px(13);
        countDownTimer = new CountDownTimer(DURATION_SCROLL, DURATION_ANIMATOR) {
            @Override
            public void onTick(long millisUntilFinished) {
                isStopScroll = false;
            }

            @Override
            public void onFinish() {
                scroll();
                if (!isStopScroll) {
                    countDownTimer.start();
                }
            }
        };
    }

    public VerticalMarqueeView setData(List<String> datas) {
        this.mData = (ArrayList<String>) datas;
        return this;
    }

    /**
     * 设置起始的position，可能业务需求会出现从第二个开始等
     *
     * @param startPosition
     * @return
     */
    public VerticalMarqueeView setStartPosition(int startPosition) {
        this.startPosition = startPosition;
        return this;
    }

    /**
     * 配置开始的Position和结束的endPosition的自增数
     * 默认从0开始，自增1
     */
    public VerticalMarqueeView setAutoIncreaseCount(int autoIncreaseCount) {
        this.autoCount = autoIncreaseCount;
        return this;
    }

    public VerticalMarqueeView setScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
        return this;
    }

    public VerticalMarqueeView setTitleDrawable(Drawable drawable) {
        if (drawable == null) {
            return this;
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        mBitmap = bitmapDrawable.getBitmap();
        return this;
    }

    public void commit() {
        if (this.mData == null || mData.size() == 0) {
            return;
        }

        blocks.clear();
        //添加显示区域的文字块
        TextBlock block1 = new TextBlock(paint);
        block1.reset(mData.get(startPosition), centerX, centerY, startPosition);
        blocks.add(block1);
        if (mData.size() >= 1) {
            TextBlock block2 = new TextBlock(paint);
            block2.reset(mData.get(endPosition), centerX, centerY + height, endPosition);
            blocks.add(block2);
        }

        paint.setColor(color);
        paint.setTextSize(textSize);
    }

    /**
     * 开始滚动
     */
    public void startScroll() {
        isStopScroll = false;
        if (mData == null || mData.size() == 0 || mData.size() == 1) {
            return;
        }
        if (!isStopScroll && countDownTimer != null) {
            countDownTimer.start();
        }
    }

    public void stopScroll() {
        this.isStopScroll = true;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /**
     * 具体动画执行的过程，向上滚动两个View的高度
     */
    private void scroll() {
        if (blocks.size() != 2) {
            return;
        }
        animator = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofInt(
                "scrollY", centerY, centerY - height));
        animator.setDuration(DURATION_ANIMATOR);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int scrollY = (int) animation.getAnimatedValue("scrollY");
                //设置两个文字块的Y轴
                blocks.get(0).reset(scrollY);
                blocks.get(1).reset(scrollY + height);
                invalidate();
            }
        });

        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                if (onScrollListener != null) {
                    onScrollListener.onScrollStart(blocks.get(0).getPosition());
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onScrollListener != null) {
                    onScrollListener.onScrollEnd(blocks.get(1).getPosition());
                }

                //找到第二个block，根据当时配置的数据位置，判断数据的变化。
                endPosition = blocks.get(1).getPosition();

                //移除第一个block
                TextBlock textBlock = blocks.remove(0);

                if (endPosition == mData.size() - autoCount) {
                    //自增为1的时候，重置为开始的position
                    if (autoCount == 1) {
                        endPosition = startPosition;
                    } else {
                        //自增不为1的情况下，分两种（基数列和偶数列）
                        //偶数列的数据和自增为1是一样的
                        //基数列的数组初始值则为autoCount
                        if (mData.size() % 2 == 0) {
                            endPosition = startPosition;
//                        } else {
//                            endPosition = autoCount - 1;
                        }
                    }
                } else {
                    endPosition += autoCount;
                }
                textBlock.reset(mData.get(endPosition), centerY + height, endPosition);

                //添加第一个
                blocks.add(textBlock);

                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
        animator.start();
    }

    public int getCurrentPosition() {
        if (mData == null || mData.size() == 0) {
            return -1;
        }
        if (mData.size() == 1 && blocks.size() == 1) {
            return 0;
        }
        return blocks.get(0).getPosition();
    }

    public void release() {
        stopScroll();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (animator.isRunning()) {
            animator.pause();
            animator.removeAllUpdateListeners();
            animator.removeAllListeners();
        }
    }

    /**
     * 文字区域控制块，处理setY和drawText逻辑
     */
    public class TextBlock {
        private String text;
        private int drawX = 0;
        private int drawY;
        private Paint paint;
        private int position;
        int drawableWidth = 0;
        int drawableHeight = 0;
        int drawDrawableY = 0;

        public TextBlock(Paint paint) {
            this.paint = paint;
        }

        public void reset(int centerY) {
            reset(text, centerX, centerY, position);
        }

        public void reset(String text, int centerY) {
            reset(text, centerX, centerY, position);
        }

        public void reset(String text, int centerY, int position) {
            reset(text, centerX, centerY, position);
        }

        public void reset(String text, int centerX, int centerY, int position) {
            this.text = text;
            this.position = position;
//            int measureWidth = (int) paint.measureText(text);
//            drawX = (width - measureWidth) / 2;
            Paint.FontMetrics metrics = paint.getFontMetrics();
            drawY = (int) (centerY + (metrics.bottom - metrics.top) / 2 - metrics.bottom);
            if (mBitmap != null) {
                drawableWidth = mBitmap.getWidth();
                drawableHeight = mBitmap.getHeight();
                drawX = (int) (drawableWidth + drawableLeftMargin);
                drawDrawableY = centerY - drawableHeight / 2;
            }
//            LogUtils.error("输出drawY:" + drawY
//                    + "\n控件高度：" + height
//                    + "\ncenterY：" + centerY
//                    + "\ndrawable高度：" + drawableHeight);
        }

        public int getPosition() {
            return position;
        }

        public void draw(Canvas canvas) {
            //判断当前的measureText是否比当前的View还要大，是的话就截取成为...
            if (paint.measureText(text) > width - drawableWidth) {
                text = text.substring(0, findTextEndIndex(text, width - drawableWidth)) + "...";
            }
//            LogUtils.info("输出当前Text的长度：" + paint.measureText(text)
//                    + "\n当前控件宽度：" + width
//                    + "\n输出当前文字：" + text
//                    + "\n输出...的宽度：" + paint.measureText("...") * POINT_MAX_THREAD_HOLD
//                    + "\n输出绘制DrawableX：" + drawableLeftMargin + "\tDrawableY：" + drawDrawableY
//                    + "\nDrawable宽高：" + drawableWidth + "\t" + drawableHeight
//                    + "\nTextX：" + drawX + "\tTextY:" + drawY);
            if (mBitmap != null) {
                canvas.drawBitmap(mBitmap, 0, drawDrawableY, paint);
            }
            canvas.drawText(text, drawX, drawY, paint);
        }

        /**
         * 返回当前字符串长度，最接近宽度的Index（为了截取字符串）
         *
         * @return
         */
        private int findTextEndIndex(String str, float width) {
            float pointWidth = paint.measureText("...");
            float[] charWidth = getCharWidth(str);
            float charTotalWidth = 0.0f;
            int curPosition = 0;
            //考虑到部分手机兼容问题，这个值需要尽量贴近，但是不能过于贴近width，否则添加不了...
            while (charTotalWidth + pointWidth * POINT_MAX_THREAD_HOLD < width) {
                charTotalWidth += charWidth[curPosition];
                curPosition++;
            }
//            LogUtils.info("输出元文本：" + str
//                    + "\n输出最接近Width的字符：" + str.charAt(curPosition)
//                    + "\n输出当前Index：" + curPosition);
            return curPosition;
        }

        /**
         * 截取单个字符的宽度
         *
         * @param string
         * @return
         */
        private float[] getCharWidth(String string) {
            float[] charWidth = new float[string.length()];
            for (int i = 0; i < string.length(); i++) {
                String singleChar = string.charAt(i) + "";
                charWidth[i] = paint.measureText(singleChar);
            }
            return charWidth;
        }


    }

    public interface OnScrollListener {

        void onScrollStart(int currentPosition);

        void onScrollEnd(int currentPosition);

    }
}
