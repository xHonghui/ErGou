package com.laka.androidlib.abstracts;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * @Author Lyf
 * @CreateTime 2018/7/3
 * @Description 自定义View的抽象类, 采用skeletal implementation模式(参考{@link java.util.AbstractSet})。
 * 在子类中创建(匿名)内部类，享受抽象类的通用行为却不被类单一继承原则所约束。
 * 子类可以继承其它类
 **/
public abstract class AbstractCustomView {

    // 返回资源布局的id
    public abstract int getLayoutId();
    // 返回属性资源的id
    public abstract int[] getAttrsId();

    // 初始化View，在getLayoutId()方法之后调用
    public abstract void initView(Context context);
    // 初始化属性，getAttrsId()方法之后，并且属性值不为空时调用，会自动回收属性资源。
    public abstract void initProperties(Context context, TypedArray typedArray);

    public AbstractCustomView(@NonNull Context context, @Nullable AttributeSet attrs,
                              @Nullable ViewGroup root) {

        LayoutInflater.from(context).inflate(getLayoutId(),root);
        initView(context);

        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs,getAttrsId());
            initProperties(context, typedArray);
            typedArray.recycle();
        }
    }

}
