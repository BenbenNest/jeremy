
package com.jeremy.lychee.share.weibo;

import java.io.Serializable;

/**
 * 
 * @author xiaozhongzhong
 *
 */
public class WeiboResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String error;
    private String request;
    private int error_code;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}
