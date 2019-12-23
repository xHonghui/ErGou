package com.laka.ergou.mvp.advert.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.laka.androidlib.widget.dialog.BaseDialog;
import com.laka.ergou.R;


/**
 * @Author:sming
 * @Date:2019/6/14
 * @Description: 通用加载弹窗
 */
public class ErGouDialog extends BaseDialog {

    private ImageView mImageViewLoading;
    private AnimationDrawable mAnimDrawable;

    public ErGouDialog(Context context) {
        super(context);
    }


    @Override
    protected void initView() {
        setLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mImageViewLoading = findViewById(R.id.image_view_loading);
        mAnimDrawable = (AnimationDrawable) getContext().getResources().getDrawable(R.drawable.anim_ergou_pull_refresh_blackbg);
        mImageViewLoading.setImageDrawable(mAnimDrawable);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_er_dog;
    }

    @Override
    public void show() {
        super.show();
        if (mAnimDrawable == null) {
            mAnimDrawable = (AnimationDrawable) getContext().getResources().getDrawable(R.drawable.anim_ergou_pull_refresh_blackbg);
            mImageViewLoading.setImageDrawable(mAnimDrawable);
        }
        mAnimDrawable.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mAnimDrawable != null) {
            mAnimDrawable.stop();
//            for (int i = 0; i < mAnimDrawable.getNumberOfFrames(); ++i) {
//                Drawable frame = mAnimDrawable.getFrame(i);
//                if (frame instanceof BitmapDrawable) {
//                    ((BitmapDrawable) frame).getBitmap().recycle();
//                }
//                frame.setCallback(null);
//            }
//            mAnimDrawable.setCallback(null);
        }
    }
}
