
package com.jeremy.lychee.model.news;

import java.io.Serializable;

public class ResultCmt<T> implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int errno;
    private T data;
    private String message;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
