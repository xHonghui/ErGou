package com.laka.androidlib.mvp;

import com.laka.androidlib.widget.refresh.BaseListBean;

/**
 * @Author:Rayman
 * @Date:2019/1/21
 * @Description:基于项目RefreshRecyclerView封装,加载列表View层
 */

public interface IBaseListView<D> extends IBaseLoadingView<D> {

    /**
     * 正确数据回调
     *
     * @param baseListBean
     */
    void onGetListDataSuccess(BaseListBean<D> baseListBean);

    /**
     * 获取列表数据，网络错误
     */
    void showGetDataNetWorkErrorView();

    /**
     * 获取列表数据，数据错误（看业务情况，暂时定义是对应JSON解析失败的情况）
     */
    void showGetDataErrorView();

}
