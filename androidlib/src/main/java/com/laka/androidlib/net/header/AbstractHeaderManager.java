package com.laka.androidlib.net.header;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;


/**
 * @Author Lyf
 * @CreateTime 2018/1/22
 * @Description A HeaderManager class is simply for getting and adding Headers to each request on Http.
 **/
public abstract class AbstractHeaderManager implements IHeaderManager {

    private static int sInstanceCount = 0;
    private final static String TAG = AbstractHeaderManager.class.getSimpleName();

    /**
     * Converts the null param's value into empty string if need.
     */
    private boolean mTurnsNullValueToEmpty = true;

    /**
     * Saving All header params.
     */
    private ArrayMap<String, String> mHeaders;


    protected AbstractHeaderManager() {
        ++sInstanceCount;
        if (sInstanceCount > 1) {
            throw new UnsupportedOperationException("You can't create two instances of AbstractHeaderManager or it's subclass!");
        }
        mHeaders = new ArrayMap<>();
        addHeaders();
    }

    /**
     * The subClass has to override this method to add Headers
     */
    protected abstract void addHeaders();

    @Override
    public ArrayMap<String, String> getHeaders() {
        return mHeaders;
    }

    @Override
    public boolean addHeader(@NonNull String headerName, String headerValue) {

        // avoid to add a null as header's value.
        if (headerName == null) {
            throw new NullPointerException("name == null");
        }
        if (headerName.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }

        // turns the null value into empty string.
        if (mTurnsNullValueToEmpty && headerValue == null) {
            headerValue = "";
        }


        mHeaders.put(headerName, headerValue);

        return true;
    }

    @Override
    public boolean addHeaders(ArrayMap<String, String> params) {

        try {
            mHeaders.putAll((SimpleArrayMap<? extends String, ? extends String>) params);
        } catch (Exception e) {
            Log.d(TAG, "Add Headers failed = " + e.toString());
            return false;
        }
        return true;
    }

    @Override
    public boolean removeHeader(@Nullable String headerName) {
        mHeaders.remove(headerName);
        return true;
    }

    @Override
    public void clearHeaders() {
        mHeaders.clear();
    }

    @Override
    public String getHeader(String headerName) {

        if (headerName == null) {
            return null;
        }

        return mHeaders.get(headerName);
    }

}