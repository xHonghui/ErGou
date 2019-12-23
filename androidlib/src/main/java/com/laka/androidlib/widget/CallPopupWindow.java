package com.laka.androidlib.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.laka.androidlib.R;
import com.orhanobut.logger.Logger;

/**
 * @Author:Rayman
 * @Date:2018/5/4
 * @Description:拨号PopupWindow
 */

public class CallPopupWindow implements BasePopupWindow.OnDismissListener, CommonPopupWindow.OnPopupItemClickListener {

    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private CommonPopupWindow popupWindow;
    private String phone;
    private StringBuffer encryptionPhone;
    private String content;

    public CallPopupWindow(Context context, String phone) {
        this(context, phone, false);
    }

    public CallPopupWindow(Context context, String phone, boolean isEncry) {
        this.context = context;
        this.phone = phone;
        encryptionPhone = new StringBuffer(phone);
        if (isEncry) {
            if (encryptionPhone.length() == 11) {
                content = "是否拨打：" + encryptionPhone.replace(3, encryptionPhone.length() - 4, "****");
            } else {
                content = "是否拨打：" + encryptionPhone.toString();
            }
        } else {
            content = "是否拨打：" + encryptionPhone.toString();
        }

        Logger.i(TAG, "输出Content：" + content);
        popupWindow = new CommonPopupWindow.Builder(context)
                .title("拨打电话")
                .content(content)
                .cancelText("取消")
                .confirmText("确定")
                .build();
        popupWindow.setOnDismissListener(this);
        popupWindow.setOnPopItemClickListener(this);
    }

    public void show() {
        popupWindow.showPopupWindow();
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    @Override
    public void onDismiss() {

    }

    @Override
    public void onClick(boolean isPositive) {
        if (isPositive) {
            //逻辑处理
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            context.startActivity(intent);
            dismiss();
        } else {
            dismiss();
        }
    }
}
