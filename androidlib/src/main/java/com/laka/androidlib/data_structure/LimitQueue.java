package com.laka.androidlib.data_structure;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @Author:Rayman
 * @Date:2018/1/16
 * @Description: 自定义固定大小队列
 */

public class LimitQueue<E> {

    private LinkedList<E> linkedList = new LinkedList<>();
    private int limit = -1;

    public LimitQueue(int limit) {
        this.limit = limit;
    }

    /**
     * 添加数据到队列头
     *
     * @param e
     */
    public void offerFirst(E e) {
        offerFirst(e, true);
    }

    /**
     * 添加数据到队列头
     *
     * @param e
     * @param isCanRepeat 数据是否可以重复
     */
    public void offerFirst(E e, boolean isCanRepeat) {
        if (isCanRepeat) {
            if (linkedList.size() >= limit) {
                linkedList.poll();
            }
            linkedList.offerFirst(e);
        } else {
            // 需要判断当前数据是否重复
            if (!linkedList.contains(e)) {
                if (linkedList.size() >= limit) {
                    linkedList.pollLast();//删除最后一个元素
                }
                linkedList.offerFirst(e);
            } else { //链表中已经存在，则把元素放到第一位
                linkedList.remove(e);
                linkedList.add(0, e);
            }
        }
    }

    /**
     * 添加数据到队列尾
     *
     * @param e
     */
    public void offerLast(E e) {
        offerLast(e, true);
    }

    /**
     * 添加数据到队列尾
     *
     * @param e
     * @param isCanRepeat 数据是否重复
     */
    public void offerLast(E e, boolean isCanRepeat) {
        if (isCanRepeat) {
            if (linkedList.size() >= limit) {
                linkedList.poll();
            }
            linkedList.offerLast(e);
        } else {
            // 需要判断当前数据是否重复
            if (!linkedList.contains(e)) {
                if (linkedList.size() >= limit) {
                    linkedList.poll();
                }
                linkedList.offerLast(e);
            }
        }
    }

    /**
     * 从队列获取数据
     *
     * @param position
     * @return
     */
    public E get(int position) {
        return linkedList.get(position);
    }

    public E getLast() {
        return linkedList.getLast();
    }

    public E getFirst() {
        return linkedList.getFirst();
    }

    public E remove(int index) {
        return linkedList.remove(index);
    }

    public boolean remove(E e) {
        return linkedList.remove(e);
    }

    public int getLimit() {
        return limit;
    }

    public int size() {
        return linkedList.size();
    }

    public LinkedList<E> getLinkedList() {
        return linkedList;
    }

    /**
     * description:清除当前列表的全部数据
     **/
    public void clearAll() {
        linkedList.clear();
    }
}
