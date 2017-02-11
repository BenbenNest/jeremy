package com.jeremy.lychee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelBase<T> {
    @SerializedName("errno")
    @Expose
    private Integer errno;
    @SerializedName("errmsg")
    @Expose
    private String errmsg;
    @SerializedName("data")
    @Expose
    private T data;

    /**
     *
     * @return
     * The errno
     */
    public Integer getErrno() {
        return errno;
    }

    /**
     *
     * @param errno
     * The errno
     */
    public void setErrno(Integer errno) {
        this.errno = errno;
    }
    /**
     *
     * @return
     * The errmsg
     */
    public String getErrmsg() {
        return errmsg;
    }

    /**
     *
     * @param errmsg
     * The errmsg
     */
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    /**
     *
     * @return
     * The data
     */
    public T getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(T data) {
        this.data = data;
    }
}
