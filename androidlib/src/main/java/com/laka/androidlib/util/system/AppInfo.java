package com.laka.androidlib.util.system;

/**
 * Created by linhz on 2015/11/29.
 * Email: linhaizhong@ta2she.com
 */
public class AppInfo {

    public String versionName;
    public int versionCode;
    public String marketChannel;
    public String packageName;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("versionName : ").append(versionName).append("\n");
        builder.append("versionCode : ").append(versionCode).append("\n");
        builder.append("marketChannel : ").append(marketChannel).append("\n");
        return builder.toString();
    }

    public String getVersionName() {
        if(versionName == null){
            return "";
        }
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getMarketChannel() {
        if(marketChannel == null){
            return "";
        }
        return marketChannel;
    }

    public String getPackageName() {
        return packageName;
    }
}
