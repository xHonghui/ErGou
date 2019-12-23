package com.laka.androidlib.features.update.util;

import android.support.annotation.NonNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author Lyf
 * @CreateTime 2018/5/14
 * @Description DefaultDownloadTaskImpl allows you to download apk file.
 * @Warning: Your activity may be leaked or a NullPointerException thrown.
 * To prevent these bugs. Make sure the instance of OnDownloadListener
 * will be released when the activity is destroyed.
 **/
public class DefaultDownloadTaskImpl implements IDownloadTask {

    // Cancel task if it's true.
    private boolean isCanceledTask = false;
    // True if it's downloading.
    private boolean isDownloading;

    private OnDownloadListener mCallBack;
    private DownloadApkUtil.Builder mBuilder;

    @Override
    public void setBuilder(@NonNull DownloadApkUtil.Builder builder) {
        mBuilder = builder;
        mCallBack = builder.getCallback();
    }

    @Override
    public void startDownloadApk() {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                isDownloading = true;

                InputStream is = null;
                FileOutputStream fos = null;

                int process;
                float total = mBuilder.getContentLength();

                try {

                    // Get the inputStream and the outputStream here.
                    is = mBuilder.getInputStream();
                    fos = new FileOutputStream(mBuilder.getFilePath());

                    if (is != null) {
                        byte[] buf = new byte[1024];
                        int ch;
                        process = 0;
                        while ((ch = is.read(buf)) != -1) {

                            // Cancel the current task.
                            if (isCanceledTask()) {
                                onCancel();
                                return;
                            }

                            fos.write(buf, 0, ch);
                            process += ch;
                            // Update the Downloading progress
                            emitter.onNext((int) (process / total * 100));
                        }

                    }

                    // Flush and Close the outputStream.
                    fos.flush();
                    fos.close();

                    // Now, The task is done.
                    emitter.onComplete();

                } catch (Exception e) {
                    // A certain error is happened.
                    emitter.onError(e);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer progress) {
                        // Update progress of downloading task.
                        onProgress(progress);
                    }

                    @Override
                    public void onError(Throwable e) {
                        isDownloading = false;
                        DefaultDownloadTaskImpl.this.onError(new Exception(e));
                    }

                    @Override
                    public void onComplete() {
                        isDownloading = false;
                        onFinish(mBuilder.getFilePath());
                    }
                });
    }

    @Override
    public void cancelDownloadTask() {

        setCanceledTask(true);
        if (mCallBack != null) {
            mCallBack.onCancel();
        }
    }

    @Override
    public boolean isDownloading() {
        return isDownloading;
    }

    @Override
    public void onError(Exception e) {

        if (mCallBack != null) {
            mCallBack.onError(e);
        }
    }

    @Override
    public void onProgress(int progress) {
        if (mCallBack != null) {
            mCallBack.onProgress(progress);
        }
    }

    @Override
    public void onCancel() {
        if (mCallBack != null) {
            mCallBack.onCancel();
        }
    }

    @Override
    public void onFinish(String filePath) {
        if (mCallBack != null) {
            mCallBack.onFinish(filePath);
        }
    }

    public boolean isCanceledTask() {
        return isCanceledTask;
    }

    /**
     * @param isCanceledTask Cancel the current downloading task if canceledTask is true.
     */
    public void setCanceledTask(boolean isCanceledTask) {
        this.isCanceledTask = isCanceledTask;
    }

}