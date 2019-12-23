package com.laka.androidlib.util;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @Author Lyf
 * @CreateTime 2018/4/27
 * @Description 一些通用的UI设置，可以抽在这里面
 **/
public class TextViewHelper {


    /**
     * 记录焦点
     */
    public static void recordFocus(EditText editText1, EditText editText2) {
        editText1.setTag(editText1.hasFocus());
        editText2.setTag(editText2.hasFocus());
    }

    /**
     * 恢复原来的焦点记录
     */
    public static void recoveryFocus(EditText editText1, EditText editText2) {
        Object tag = editText1.getTag();
        if (tag instanceof Boolean) {
            Boolean flag = (Boolean) tag;
            if (flag) {
                editText2.clearFocus();
                editText1.requestFocus();
            } else {
                editText1.clearFocus();
                editText2.requestFocus();
            }
        }
    }

    /**
     * 锁定view 的焦点
     */
    public static void lockFocusForView(View view) {

    }


    /**
     * 限制输入框的最大值和小数点的位数
     *
     * @param editText 输入框
     * @param max      输入框的最大数额
     *                 小数点的位数默认是2位
     */
    public static boolean limitEditText(EditText editText,
                                        String before, double max) {

        String text = editText.getText().toString().trim();

        if (text.length() > 0) {

            try {
                if (Double.valueOf(text) > max) {
                    editText.setText(before);
                    editText.setSelection(before.length());
                    return true;
                } else {

                    if (text.contains(".")) {
                        if (text.length() - text.indexOf(".") > 3) {
                            text = text.subSequence(0, text.indexOf(".") + 3).toString();
                            editText.setText(text);
                            editText.setSelection(text.length());
                        }
                    }
                }
            } catch (Exception e) {
            }

        }

        return false;
    }

    /**
     * 限制输入框的小数点的位数
     *
     * @param editText 输入框
     *                 小数点的位数默认是2位
     */
    public static boolean limitEditText(EditText editText, String before) {

        String text = editText.getText().toString().trim();

        if (text.length() > 0) {

            try {

                if (text.contains(".")) {
                    if (text.length() - text.indexOf(".") > 3) {
                        text = text.subSequence(0, text.indexOf(".") + 3).toString();
                        editText.setText(text);
                        editText.setSelection(text.length());
                    }
                }

            } catch (Exception e) {
            }

        }

        return false;
    }

    /**
     * 设置密码的可见与不可见
     *
     * @param editText
     * @param isChecked
     */
    public static void visiblePassword(EditText editText, boolean isChecked) {
        if (isChecked) {
            editText.setTransformationMethod(
                    HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(
                    PasswordTransformationMethod.getInstance());
        }
        //因为切换显示或隐藏状态后，光标会移到前面去，所以，要再将其移到最后。
        editText.setSelection(editText.getText().toString().length());
    }

    /**
     * 将View的高度设为状态栏的高度
     */
    public static void setViewHeightAsSBarHeight(@NonNull View view) {

        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = SystemUtils.getStatusBarHeight();
            view.setLayoutParams(layoutParams);
        } else {
            log("view can't be null");
        }

    }

    /**
     * 获取TextView或其子类的内容。
     * 比如，EditText
     */
    public static String getText(@NonNull TextView textView) {

        if (textView != null) {
            return textView.getText().toString();
        } else {
            log("editText can't be null");
            return "";
        }
    }

    /**
     * 设置Span
     */
    public static void setSpan(Builder builder) {

        Spannable span = new SpannableString(builder.getText());

        if (builder.getEnd() == 0) {
            // 如果没有设置结束位置，就修改整个。
            builder.setEnd(builder.getText().length());
        }

        if (builder.isSetSize()) {
            span.setSpan(new AbsoluteSizeSpan(builder.getTextSize(), true), builder.getStart(), builder.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (builder.isSetColor()) {
            span.setSpan(new ForegroundColorSpan(ResourceUtils.getColor(builder.getTextColor())), builder.getStart(), builder.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (builder.isBold()) {
            span.setSpan(new StyleSpan(Typeface.BOLD), builder.getStart(), builder.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (builder.isStrikeTrough()) {
            span.setSpan(new StrikethroughSpan(), builder.getStart(), builder.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (builder.isUnderLine()) {
            span.setSpan(new UnderlineSpan(), builder.getStart(), builder.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        builder.getTextView().setText(span);
    }

    public static final class Builder {

        private int textSize;
        private int textColor;
        private int start;
        private int end;

        // 是否加粗
        private boolean isBold;
        // 是否删除线
        private boolean isStrikeTrough;
        // 是否下划线
        private boolean isUnderLine;

        private String text;
        private TextView textView;
        /*
         * 标识是否有设置相关属性
         */
        private boolean isSetColor = false;
        private boolean isSetSize = false;

        public Builder setBold(boolean bold) {
            isBold = bold;
            return this;
        }

        public Builder setStrike(boolean isStrikeTrough) {
            this.isStrikeTrough = isStrikeTrough;
            return this;
        }

        public Builder setUnderLine(boolean isUnderLine) {
            this.isUnderLine = isUnderLine;
            return this;
        }

        /**
         * @param textSize dip
         */
        public Builder setTextSize(int textSize) {
            this.isSetSize = true;
            this.textSize = textSize;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.isSetColor = true;
            this.textColor = textColor;
            return this;
        }

        public Builder setStart(int start) {
            this.start = start;
            return this;
        }

        public Builder setEnd(int end) {
            this.end = end;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTextView(TextView textView) {
            this.textView = textView;
            return this;
        }

        public int getTextSize() {
            return textSize;
        }

        public int getTextColor() {
            return textColor;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public String getText() {
            return text;
        }

        public TextView getTextView() {
            return textView;
        }

        public boolean isSetColor() {
            return isSetColor;
        }

        public boolean isSetSize() {
            return isSetSize;
        }

        public boolean isBold() {
            return isBold;
        }

        public boolean isStrikeTrough() {
            return isStrikeTrough;
        }

        public boolean isUnderLine() {
            return isUnderLine;
        }
    }


    private static void log(String msg) {
        LogUtils.error(msg);
    }

}
