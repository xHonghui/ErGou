package com.laka.androidlib.widget.refresh;

import java.util.List;

/**
 * @Author Lyf
 * @CreateTime 2018/5/3
 * @Description 所有List的数据，都在getList里面转换并返回
 **/
public abstract class BaseListBean<T> {

    public int pageTotalCount = 1;

    public abstract List<T> getList();

    public boolean isEmpty() {
        List list = getList();
        return list == null || list.isEmpty();
    }

    public int getPageTotalCount() {
        return pageTotalCount;
    }

    public BaseListBean setPageTotalCount(int pageTotalCount) {
        this.pageTotalCount = pageTotalCount;
        return this;
    }

    @Override
    public String toString() {
        return "BaseListBean{" +
                "pageTotalCount=" + pageTotalCount +
                '}';
    }
}
