package com.laka.androidlib.widget.refresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.laka.androidlib.features.login.OnRequestListener;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

/**
 * @Author Lyf
 * @CreateTime 2018/5/2
 * @Description 自动翻页的下拉控件的协议。
 * <p>
 * 泛型参数：
 * T: 代表列表控件。比如, ListView、RecycleView等等。
 * A: 代表适配器。Adapter。
 * L: 网络请求成功的回调
 * D: 数据
 * </p>
 * <p>
 * 方法命名规则：
 * 凡是set开头的API，如无特殊返回值，
 * 应以当前协议为返回值，以实现链式调用设置参数。
 * </p>
 **/
public interface IPageListLayout<T, A, D> {

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    void initProperty(Context context, @Nullable AttributeSet attrs);

    /**
     * 初始化UI
     *
     * @param context
     * @param attrs
     */
    void initViews(Context context, @Nullable AttributeSet attrs);

    //<editor-fold desc="属性配置">
    //----- set/add方法 -----//

    /**
     * 是否使用刷新功能
     *
     * @param enable
     * @return
     */
    IPageListLayout enableRefresh(boolean enable);

    /**
     * 是否使用加载更多功能
     *
     * @param enable
     * @return
     */
    IPageListLayout enableLoadMore(boolean enable);

    /**
     * 设置加载控件的头部
     *
     * @param header
     * @return
     */
    IPageListLayout setRefreshHeader(RefreshHeader header);

    /**
     * 设置加载控件的尾部
     *
     * @param footer
     * @return
     */
    IPageListLayout setRefreshFooter(RefreshFooter footer);

    /**
     * 设置Adapter
     *
     * @param adapter
     * @return
     */
    IPageListLayout setAdapter(A adapter);

    /**
     * 添加Adapter的Header
     *
     * @param view
     * @return
     */
    IPageListLayout addAdapterHeader(View view);

    /**
     * 移除Adapter指定的Header
     *
     * @param view
     * @return
     */
    IPageListLayout removeAdapterHeader(View view);

    /**
     * 添加Adapter的Footer
     *
     * @param view
     * @return
     */
    IPageListLayout addAdapterFooter(View view);

    /**
     * 移除Adapter指定的Footer
     *
     * @param view
     * @return
     */
    IPageListLayout removeAdapterFooter(View view);

    /**
     * 设置无数据状态View（在整个View的中间）
     *
     * @param view
     * @return
     */
    IPageListLayout setNoDataView(View view);

    /**
     * 设置LoadingView, 可以是Dialog，也可以是全屏加载的View。
     *
     * @param view
     * @return
     */
    IPageListLayout setLoadingView(View view);

    /**
     * 设置显示错误的View（在整个View的中间）
     *
     * @param errorView
     * @return
     */
    IPageListLayout setErrorView(View errorView);

    /**
     * 设置无更多数据展示的View（用于数据不满一屏的时候）
     *
     * @param noMoreDataView
     * @return
     */
    IPageListLayout setNoMoreDataView(View noMoreDataView);

    /**
     * 设置页面是否纯净滚动
     *
     * @param isPureMode
     * @return
     */
    IPageListLayout setPureScrollMode(boolean isPureMode);

    /**
     * 添加RecyclerViewItemDecoration
     *
     * @param decoration
     * @return
     */
    IPageListLayout addItemDecoration(RecyclerView.ItemDecoration decoration);

    /**
     * 删除RecyclerViewItemDecoration
     *
     * @param decoration
     * @return
     */
    IPageListLayout removeItemDecoration(RecyclerView.ItemDecoration decoration);

    /**
     * 设置RecyclerView的布局Manager
     *
     * @param layoutManager
     * @return
     */
    IPageListLayout setLayoutManager(RecyclerView.LayoutManager layoutManager);

    /**
     * 设置RecyclerView的Item动画
     *
     * @param animator
     * @return
     */
    IPageListLayout setItemAnimator(RecyclerView.ItemAnimator animator);

    /**
     * 是否启用双击Item功能
     *
     * @param isEnableMultiClick 默认为false，只能单点
     * @return
     */
    IPageListLayout setEnableMultiClick(boolean isEnableMultiClick);

    /**
     * 设置数据加载完毕之后，底部是否显示一个固定的加载数据完毕View
     *
     * @param isEnable
     * @return
     */
    IPageListLayout enableFooterNoMoreData(boolean isEnable);

    /**
     * 是否手动处理空数据的情况
     * 因为开发中发现，有部分页面需要自己去处理空数据。
     * 不然就被一个EmptyView给覆盖了
     *
     * @param isManual
     * @return
     */
    IPageListLayout enableManualHandleEmptyData(boolean isManual);

    /**
     * 设置默认的起始页,默认是1。
     *
     * @param defaultPage
     * @return
     */
    IPageListLayout setDefaultPage(int defaultPage);

    /**
     * 根据后台返回的列表大小计算出列表页
     *
     * @param listSize 后台返回列表大小
     * @param limit    请求一页的数据值
     * @return
     */
    IPageListLayout setDefaultPage(int listSize, int limit);

    /**
     * 网络请求的监听者，实际的网络请求，在这个方法里面执行。
     *
     * @param onRequestListener 网络请求的监听者，实际的网络请求，在这个方法里面执行。
     * @return
     */
    IPageListLayout setOnRequestListener(OnRequestListener<OnResultListener> onRequestListener);

    /**
     * 设置请求结果的回调
     *
     * @param onResultListener 请求结果的回调
     * @return
     */
    IPageListLayout setOnResultListener(OnResultListener onResultListener);
    //</editor-fold>

    //<editor-fold desc="刷新操作以及View层显示">

    /**
     * 刷新
     */
    void refresh();

    /**
     * 刷新时候是否带头部刷新动画
     * 主要用于第一次进入的时候自动刷新
     *
     * @param isAnimation true,带动画，false，不带
     */
    void refresh(boolean isAnimation);

    /**
     * 这种情况下，肯定是要动画的，所以，不加isAnimation
     *
     * @param delayed  延时刷新
     * @param duration 弹出刷新界面所花的时间
     * @param dragRate 回弹比例。建议为1，则弹出来的界面跟刷新界面所需的高度一致。
     */
    void refresh(int delayed, final int duration, final float dragRate);

    /**
     * 停止刷新
     */
    void stopRefresh();

    /**
     * 获取下一页的数据
     */
    void getNextPage();

    /**
     * 加载更多数据
     */
    void loadData();

    /**
     * 显示空View
     */
    void showEmptyView();

    /**
     * 显示错误的View
     */
    void showErrorView();

    /**
     * 显示加载中的状态
     */
    void showLoadingView();

    /**
     * 显示加载更多的状态
     */
    void showLoadMoreView();

    /**
     * 隐藏空View
     */
    void hideEmptyView();

    /**
     * 隐藏错误的View
     */
    void hideErrorView();

    /**
     * 隐藏加载中的状态
     */
    void hideLoadingView();

    /**
     * 隐藏加载更多的状态
     */
    void hideLoadMoreView();
    //</editor-fold>

    //<editor-fold desc="滚动操作">

    /**
     * 根据当前的ViewType去判断滚动的位置
     *
     * @param itemType 当前Item的ViewType
     */
    void scrollToPositionByType(int itemType);

    /**
     * 根据当前的ViewType去判断滚动的位置
     *
     * @param itemType 当前Item的ViewType
     * @param offset   额外的纵坐标值
     */
    void scrollToPositionByTypeWithOffset(int itemType, int offset);

    /**
     * 根据position滚动到指定位置
     *
     * @param position 指定位置
     */
    void scrollToPosition(int position);

    /**
     * 根据position滚动到指定位置
     *
     * @param position 指定位置
     * @param offset   额外的纵坐标值
     */
    void scrollToPositionWithOffset(int position, int offset);
    //</editor-fold>

    //<editor-fold desc="get方法">


    /**
     * 获取当前页数
     *
     * @return 返回当前页数
     */
    int getCurrentPage();

    /**
     * 获取当前列表最大值
     *
     * @return 返回当前列表页数最大值
     */
    int getDefaultPage();

    /**
     * 获取列表控件, 这个控件只有获取，没有设置的API。
     * 不建议，PageListLayout的列表控件可以动态设置。
     * 因为，不同的列表控件的API差别过大。不同列表控件，
     * 建议，依照相同的协议，封装起来。
     *
     * @return
     */
    T getContentView();

    /**
     * 获取适配器
     *
     * @return
     */
    A getAdapter();
    //</editor-fold>

    //<editor-fold desc="数据操作相关方法">

    /**
     * 添加数据
     *
     * @param d
     */
    void addData(D d);


    /**
     * 更新数据
     *
     * @param d
     */
    void upDateData(D d);

    /**
     * 清除数据
     */
    void clearAll();

    void resetPage();
    //</editor-fold>
}
