package com.laka.androidlib.net.http;

import com.laka.androidlib.net.consts.HttpCodes;

/**
 * @Author Lyf
 * @CreateTime 2018/2/23
 * @Description A HttpParameterBuilder class is a Builder for creating a set of parameters of http.
 **/
public final class HttpParameterBuilder {

    private boolean mAllowedEmptyData;
    private int mReadTimeOut = 3_000;
    private int mWriteTimeOut = 3_000;
    private int mConnectTimeOut = 3_000;

    private String mServerErrorMsg = "Network is unavailable.";
    private String mParseErrorMsg = "Parsing json error.";

    /**
     * Sets Read, Write and Connect TimeOut.
     *
     * @param timeOut
     */
    public HttpParameterBuilder setTimeOut(int timeOut) {

        setReadTimeOut(timeOut);
        setWriteTimeOut(timeOut);
        setConnectTimeOut(timeOut);
        return this;
    }

    public int getConnectTimeOut() {
        return mConnectTimeOut;
    }

    public HttpParameterBuilder setConnectTimeOut(int connectTimeOut) {
        this.mConnectTimeOut = connectTimeOut;
        return this;
    }

    public int getReadTimeOut() {
        return mReadTimeOut;
    }

    public HttpParameterBuilder setReadTimeOut(int readTimeOut) {
        this.mReadTimeOut = readTimeOut;
        return this;
    }

    public int getWriteTimeOut() {
        return mWriteTimeOut;
    }

    public HttpParameterBuilder setWriteTimeOut(int writeTimeOut) {
        this.mWriteTimeOut = writeTimeOut;
        return this;
    }

    public int getCodeSuccess() {
        return HttpCodes.CODE_SUCCESS;
    }

    public HttpParameterBuilder setCodeSuccess(int codeSuccess) {
        HttpCodes.CODE_SUCCESS = codeSuccess;
        return this;
    }

    public int getCodeFailure() {
        return HttpCodes.CODE_FAILURE;
    }

    public HttpParameterBuilder setCodeFailure(int codeFailure) {
        HttpCodes.CODE_FAILURE = codeFailure;
        return this;
    }

    public boolean isAllowedEmptyData() {
        return mAllowedEmptyData;
    }

    public HttpParameterBuilder setAllowedEmptyData(boolean allowedEmptyData) {
        this.mAllowedEmptyData = allowedEmptyData;
        return this;
    }


    public String getParseErrorMsg() {
        return mParseErrorMsg;
    }

    public HttpParameterBuilder setParseErrorMsg(String parseErrorMsg) {
        this.mParseErrorMsg = parseErrorMsg;
        return this;
    }

    public String getServerErrorMsg() {
        return mServerErrorMsg;
    }

    public HttpParameterBuilder setServerErrorMsg(String serverErrorMsg) {
        this.mServerErrorMsg = serverErrorMsg;
        return this;
    }
}
