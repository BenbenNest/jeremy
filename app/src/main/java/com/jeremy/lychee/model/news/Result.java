
package com.jeremy.lychee.model.news;

import java.io.Serializable;

public class Result<T> implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int status;
    private T data;
    private String msg;
    private String json;

    public int errno;
    public T list;
    public T rawList;

    //user center err msg
    private String errmsg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public static final int HTTP_OK = 0;
    public static final int HTTP_DIGGED = 108;

}
