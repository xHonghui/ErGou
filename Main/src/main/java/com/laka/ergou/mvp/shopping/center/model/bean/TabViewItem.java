package com.laka.ergou.mvp.shopping.center.model.bean;

import android.view.View;

public class TabViewItem {
    private View layoutView;
    private boolean isHavaSelect = true;

    public TabViewItem(View layoutView) {
        this.layoutView = layoutView;
    }

    public TabViewItem(View layoutView, boolean isHavaSelect) {
        this.layoutView = layoutView;
        this.isHavaSelect = isHavaSelect;
    }

    public View getLayoutView() {
        return layoutView;
    }

    public void setLayoutView(View layoutView) {
        this.layoutView = layoutView;
    }

    public boolean isHavaSelect() {
        return isHavaSelect;
    }

    public void setHavaSelect(boolean havaSelect) {
        isHavaSelect = havaSelect;
    }
}
