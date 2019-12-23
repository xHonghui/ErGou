package com.laka.ergou.mvp.customer.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.laka.androidlib.util.StatusBarUtil;
import com.laka.androidlib.util.toast.ToastHelper;
import com.laka.androidlib.widget.titlebar.TitleBarView;
import com.laka.ergou.R;
import com.laka.ergou.mvp.customer.constant.CustomerConstant;
import com.laka.ergou.mvp.customer.util.ImageUtil;
import com.laka.ergou.mvp.customer.util.PermissionUtil;
import com.laka.ergou.mvp.customer.client.ReWebChomeClient;
import com.laka.ergou.mvp.customer.client.ReWebViewClient;
import com.laka.ergou.mvp.user.UserUtils.UserUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:客服页面
 */
public class CustomerActivity extends AppCompatActivity implements ReWebChomeClient.OpenFileChooserCallBack {
    private WebView webView;
    private TitleBarView mTitleBarView;
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private Intent mSourceIntent;
    public ValueCallback<Uri[]> mValueCallback;
    private ValueCallback<Uri> mUploadMsg;
    private static final int P_CODE_PERMISSIONS = 101;
    //参数
    private String mTitle = "客服";
    private String mUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        setStatusBar(R.color.black);
        initParams();
        initView();
    }

    private void setStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this,R.color.white), 0);
            StatusBarUtil.setLightModeNotFullScreen(this, true);
        } else {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this,color), 0);
        }
    }

    private void initParams() {
        mTitle = getIntent().getStringExtra(CustomerConstant.KEY_CUSTOMER_TITLE);
        //拼接url，这样客服那边看能看到完整的用户信息
        mUrl = getIntent().getStringExtra(CustomerConstant.KEY_CUSTOMER_URL)
                + "&u_cust_id=" + UserUtils.INSTANCE.getUserId()
                + "&u_cust_name=" + UserUtils.INSTANCE.getUserId()
                + "|" + UserUtils.INSTANCE.getUserName()
                + "&u_custom_info=Android";
    }

    private void initView() {
        mTitleBarView = findViewById(R.id.title_bar);
        mTitleBarView.setTitle(mTitle)
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16);
        webView = findViewById(R.id.web_view);
        webView.loadUrl(mUrl);
        fixDirPath();
        webView.setWebViewClient(new ReWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new ReWebChomeClient(this));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
            }

            if (mValueCallback != null) {         // for android 5.0+
                mValueCallback.onReceiveValue(null);
            }
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:
            case REQUEST_CODE_PICK_IMAGE: {
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMsg == null) {
                            return;
                        }
                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMsg.onReceiveValue(uri);

                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (mValueCallback == null) {        // for android 5.0+
                            return;
                        }
                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mValueCallback.onReceiveValue(new Uri[]{uri});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new DialogOnCancelListener());
        alertDialog.setTitle("请选择操作");
        String[] options = {"相册", "拍照"};
        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (PermissionUtil.isOverMarshmallow()) {
                                if (!PermissionUtil.isPermissionValid(CustomerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    ToastHelper.showToast(getString(R.string.str_media_permission));
                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }
                            }

                            try {
                                mSourceIntent = ImageUtil.choosePicture();
                                startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastHelper.showToast(getString(R.string.str_media_permission));
                                restoreUploadMsg();
                            }

                        } else {
                            if (PermissionUtil.isOverMarshmallow()) {
                                if (!PermissionUtil.isPermissionValid(CustomerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    ToastHelper.showToast(getString(R.string.str_media_permission));
                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }

                                if (!PermissionUtil.isPermissionValid(CustomerActivity.this, Manifest.permission.CAMERA)) {
                                    ToastHelper.showToast(getString(R.string.str_camra_permission));
                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }
                            }

                            try {
                                mSourceIntent = ImageUtil.takeBigPicture();
                                startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);

                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastHelper.showToast(getString(R.string.str_media_permission));
                                restoreUploadMsg();
                            }
                        }
                    }
                }
        ).show();
    }

    private void fixDirPath() {
        String path = ImageUtil.getDirPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMsg = uploadMsg;
        showOptions();
    }


    @Override
    public boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        mValueCallback = filePathCallback;
        showOptions();
        return true;
    }


    private class DialogOnCancelListener implements DialogInterface.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            restoreUploadMsg();
        }
    }

    private void restoreUploadMsg() {
        if (mUploadMsg != null) {
            mUploadMsg.onReceiveValue(null);
            mUploadMsg = null;

        } else if (mValueCallback != null) {
            mValueCallback.onReceiveValue(null);
            mValueCallback = null;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case P_CODE_PERMISSIONS:
                requestResult(permissions, grantResults);
                restoreUploadMsg();
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestPermissionsAndroidM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> needPermissionList = new ArrayList<>();
            needPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            needPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            needPermissionList.add(Manifest.permission.CAMERA);
            PermissionUtil.requestPermissions(CustomerActivity.this, P_CODE_PERMISSIONS, needPermissionList);
        } else {
            return;
        }
    }

    public void requestResult(String[] permissions, int[] grantResults) {
        ArrayList<String> needPermissions = new ArrayList<String>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (PermissionUtil.isOverMarshmallow()) {
                    needPermissions.add(permissions[i]);
                }
            }
        }

        if (needPermissions.size() > 0) {
            StringBuilder permissionsMsg = new StringBuilder();
            for (int i = 0; i < needPermissions.size(); i++) {
                String strPermissons = needPermissions.get(i);
                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(strPermissons)) {
                    permissionsMsg.append("," + getString(R.string.permission_storage));
                } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(strPermissons)) {
                    permissionsMsg.append("," + getString(R.string.permission_storage));
                } else if (Manifest.permission.CAMERA.equals(strPermissons)) {
                    permissionsMsg.append("," + getString(R.string.permission_camera));
                }
            }
            String strMessage = "请允许使用\"" + permissionsMsg.substring(1).toString() + "\"权限, 以正常使用APP的所有功能.";
            Toast.makeText(CustomerActivity.this, strMessage, Toast.LENGTH_SHORT).show();
        } else {
            return;
        }
    }
}






