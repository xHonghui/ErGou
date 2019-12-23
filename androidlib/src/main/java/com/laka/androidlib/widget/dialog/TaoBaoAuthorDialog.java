package com.laka.androidlib.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.laka.androidlib.R;
import com.laka.androidlib.widget.SelectorButton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @Author:summer
 * @Date:2019/1/12
 * @Description: 淘宝授权确认弹窗
 */
public class TaoBaoAuthorDialog extends BaseDialog {

    private SelectorButton mSbSure;
    private ImageView mIvDelete;

    public TaoBaoAuthorDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_taobao_author;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mSbSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSureClickListener != null) {
                    mOnSureClickListener.onSureClick(v);
                    dismiss();
                }
            }
        });
        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void initView() {
        mSbSure = findViewById(R.id.sb_sure);
        mIvDelete = findViewById(R.id.iv_delete);
    }

    @Override
    protected void initAnima() {
        //设置弹出收起动画
        getWindow().setWindowAnimations(R.style.commonDialogAnim);
    }

    private OnSureClickListener mOnSureClickListener;

    public void setOnSureClickListener(OnSureClickListener onSureClickListener) {
        mOnSureClickListener = onSureClickListener;
    }

    public interface OnSureClickListener {
        void onSureClick(View view);
    }

}
