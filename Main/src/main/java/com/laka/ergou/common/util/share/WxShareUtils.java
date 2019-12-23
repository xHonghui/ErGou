package com.laka.ergou.common.util.share;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.Util;
import com.laka.androidlib.R;
import com.laka.androidlib.util.PackageUtils;
import com.laka.androidlib.util.image.BitmapUtils;
import com.laka.androidlib.util.toast.ToastHelper;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.io.File;
import java.util.ArrayList;

/**
 * @Author:Rayman
 * @Date:2019/1/23
 * @Description:分享帮助类
 */

public class WxShareUtils {

    private WxHelper mHelper;
    private Context mContext;
    private volatile static WxShareUtils mInstance;

    private WxShareUtils(Context context) {
        mHelper = new WxHelper(context);
        this.mContext = context;
    }

    public static WxShareUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (WxShareUtils.class) {
                if (mInstance == null) {
                    mInstance = new WxShareUtils(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public void shareTextAction(String text, Boolean isShareToFriend) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req shareRequest = new SendMessageToWX.Req();
        shareRequest.transaction = buildTransaction("text");
        shareRequest.message = msg;
        if (isShareToFriend) {
            shareRequest.scene = SendMessageToWX.Req.WXSceneSession;
        } else {
            shareRequest.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        mHelper.sendShareReq(shareRequest);
    }

    public void shareWebAction(boolean isShareToFriend,
                               String title, String description, String shareUrl,
                               Bitmap bitmap) {
        if (bitmap != null) {
            shareWebActionWithThumb(isShareToFriend, title, description, shareUrl, BitmapUtils.transBitmapToByte(bitmap, true));
        } else {
            shareWebActionWithThumb(isShareToFriend, title, description, shareUrl, null);
        }
    }

    private void shareWebActionWithThumb(boolean isShareToFriend,
                                         String title, String description, String shareUrl,
                                         byte[] thumbData) {
        if (!mHelper.isInstallWeChat()) {
            ToastHelper.showToast(R.string.please_install_weixin);
            return;
        }

        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = shareUrl;
        WXMediaMessage shareMessage = new WXMediaMessage(webPage);
        shareMessage.title = title;
        shareMessage.description = description;
        if (thumbData != null) {
            shareMessage.thumbData = thumbData;
        }

        SendMessageToWX.Req shareRequest = new SendMessageToWX.Req();
        shareRequest.transaction = buildTransaction("webpage");
        shareRequest.message = shareMessage;
        if (isShareToFriend) {
            shareRequest.scene = SendMessageToWX.Req.WXSceneSession;
        } else {
            shareRequest.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        mHelper.sendShareReq(shareRequest);
    }

    public void shareWebActionBitMap(boolean isShareToFriend,
                                     String title, String description, String shareUrl,
                                     Bitmap bitmap) {
        if (!mHelper.isInstallWeChat()) {
            ToastHelper.showToast(R.string.please_install_weixin);
            return;
        }

        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = shareUrl;
        WXMediaMessage shareMessage = new WXMediaMessage(webPage);
        shareMessage.title = title;
        shareMessage.description = description;
        if (bitmap != null) {
            shareMessage.setThumbImage(bitmap);
        }

        SendMessageToWX.Req shareRequest = new SendMessageToWX.Req();
        shareRequest.transaction = buildTransaction("webpage");
        shareRequest.message = shareMessage;
        if (isShareToFriend) {
            shareRequest.scene = SendMessageToWX.Req.WXSceneSession;
        } else {
            shareRequest.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        mHelper.sendShareReq(shareRequest);
    }

    public void shareImageAction(boolean isShareToFriend, String title, String description, byte[] imageData) {
        if (!mHelper.isInstallWeChat()) {
            ToastHelper.showToast(R.string.please_install_weixin);
            return;
        }

        if (imageData == null || imageData.length <= 0) {
            ToastHelper.showCenterToast("截图失败，请重试");
            return;
        }

        WXImageObject shareImage = new WXImageObject(imageData);
        WXMediaMessage shareMessage = new WXMediaMessage(shareImage);
        shareMessage.title = title;
        shareMessage.description = description;

        SendMessageToWX.Req shareRequest = new SendMessageToWX.Req();
        shareRequest.transaction = buildTransaction("img");
        shareRequest.message = shareMessage;
        if (isShareToFriend) {
            shareRequest.scene = SendMessageToWX.Req.WXSceneSession;
        } else {
            shareRequest.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        mHelper.sendShareReq(shareRequest);
    }

    public void shareImageAction(boolean isShareToFriend, String title, String description, String imgPath) {
        if (!mHelper.isInstallWeChat()) {
            ToastHelper.showToast(R.string.please_install_weixin);
            return;
        }

        if (!new File(imgPath).exists()) {
            ToastHelper.showCenterToast("图片保存失败，请重试");
            return;
        }

        WXImageObject shareImage = new WXImageObject();
        shareImage.imagePath = imgPath;
        WXMediaMessage shareMessage = new WXMediaMessage(shareImage);
        shareMessage.title = title;
        shareMessage.description = description;

        SendMessageToWX.Req shareRequest = new SendMessageToWX.Req();
        shareRequest.transaction = buildTransaction("img");
        shareRequest.message = shareMessage;
        if (isShareToFriend) {
            shareRequest.scene = SendMessageToWX.Req.WXSceneSession;
        } else {
            shareRequest.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        mHelper.sendShareReq(shareRequest);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 微信多图分享，使用系统的分享方式
     */
    public void shareMultiImageAction(Context context, ArrayList<File> fileList) {
        if (fileList == null || fileList.isEmpty()) return;
        if (!PackageUtils.isAppInstalled(context, PackageUtils.WEI_XIN)) {
            return;
        }
        //分享到微信好友
        ArrayList<Uri> imageUri = new ArrayList();
        for (int i = 0; i < fileList.size(); i++) {
            imageUri.add(Uri.fromFile(fileList.get(i)));
        }
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        if (imageUri.size() == 0) return;
        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUri);
        context.startActivity(intent);
    }

    public boolean isInstallWeChat() {
        return mHelper.isInstallWeChat();
    }


    /**
     * 视频类型分享
     */
    public void shareVideoAction(String videoUrl, String videoTitle,
                                 String videoDes, byte[] thumbBmp, boolean isCircleFriends) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = videoUrl;
        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = videoTitle;
        msg.description = videoDes;
        msg.thumbData = thumbBmp;

        SendMessageToWX.Req shareRequest = new SendMessageToWX.Req();
        shareRequest.transaction = buildTransaction("video");
        shareRequest.message = msg;
        if (isCircleFriends) {
            shareRequest.scene = SendMessageToWX.Req.WXSceneTimeline;
        } else {
            shareRequest.scene = SendMessageToWX.Req.WXSceneSession;
        }
        mHelper.sendShareReq(shareRequest);
    }

    /**
     * 视频类型分享
     */
    public void shareVideoAction(String videoUrl, String videoTitle,
                                 String videoDes, Bitmap thumbBmp, boolean isCircleFriends) {
        shareVideoAction(videoUrl, videoTitle, videoDes, BitmapUtils.compressBitmapToByte(thumbBmp, 50, true), isCircleFriends);
    }

    public void shareVideoAction(final String videoUrl, final String videoTitle,
                                 final String videoDes, int imgRes, final boolean isCircleFriends) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), imgRes);
        if (bitmap != null) {
            shareVideoAction(videoUrl, videoTitle, videoDes, BitmapUtils.compressBitmapToByte(bitmap, 50, false), isCircleFriends);
        }
    }

    public void shareVideoAction(final String videoUrl, final String videoTitle,
                                 final String videoDes, String imgUrl, final boolean isCircleFriends) {
        Glide.with(mContext).load(imgUrl).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                if (resource.length > 0) {
                    shareVideoAction(videoUrl, videoTitle, videoDes, resource, isCircleFriends);
                }
            }
        });
    }

}
