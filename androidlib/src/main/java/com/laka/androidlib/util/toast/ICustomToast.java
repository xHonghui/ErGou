package com.laka.androidlib.util.toast;

/**
 * @Author Lyf
 * @CreateTime 2018/3/7
 * @Description
 **/
public interface ICustomToast {

    void setText(CharSequence s);

    void show();

    void setDuration(int duration);

    void setGravity(int gravity, int xOffset, int yOffset);

}
