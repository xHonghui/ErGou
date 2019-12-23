package com.laka.androidlib.widget.refresh.decoration;

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:当前仅针对垂直状态下的分割线设置
 */

public class DividerInfo {

    /**
     * description:分别对应分割线左侧Padding
     * 右侧Padding
     * divider的透明度
     * divider的高度
     * divider的作用区域
     * divider是否忽略不计（不设置任何高度，宽度）
     **/
    private int dividerLeft;
    private int dividerRight;
    private int dividerAlpha;
    private int dividerSpacing;
    private int dividerTargetPosition;
    private boolean isDividerBottomIgnore = false;
    private boolean isDividerRightIgnore = false;
    private int ignoreItemByViewType = -1;

    public int getDividerLeft() {
        return dividerLeft;
    }

    public void setDividerLeft(int dividerLeft) {
        this.dividerLeft = dividerLeft;
    }

    public int getDividerRight() {
        return dividerRight;
    }

    public void setDividerRight(int dividerRight) {
        this.dividerRight = dividerRight;
    }

    public int getDividerAlpha() {
        return dividerAlpha;
    }

    public void setDividerAlpha(int dividerAlpha) {
        this.dividerAlpha = dividerAlpha;
    }

    public int getDividerSpacing() {
        return dividerSpacing;
    }

    public void setDividerSpacing(int dividerSpacing) {
        this.dividerSpacing = dividerSpacing;
    }

    public int getDividerTargetPosition() {
        return dividerTargetPosition;
    }

    public void setDividerTargetPosition(int dividerTargetPosition) {
        this.dividerTargetPosition = dividerTargetPosition;
    }

    public boolean isDividerBottomIgnore() {
        return isDividerBottomIgnore;
    }

    public void setDividerBottomIgnore(boolean dividerBottomIgnore) {
        isDividerBottomIgnore = dividerBottomIgnore;
    }

    public boolean isDividerRightIgnore() {
        return isDividerRightIgnore;
    }

    public void setDividerRightIgnore(boolean dividerRightIgnore) {
        isDividerRightIgnore = dividerRightIgnore;
    }

    public int getIgnoreItemByViewType() {
        return ignoreItemByViewType;
    }

    public void setIgnoreItemByViewType(int ignoreItemByViewType) {
        this.ignoreItemByViewType = ignoreItemByViewType;
    }
}
