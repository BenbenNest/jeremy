package com.jeremy.lychee.model.user;

import java.io.Serializable;

/**
 * Created by zhangying-pd on 2016/2/16.
 * 图像验证码 返回结果
    参数名	类型	是否必要	说明	默认值
    errno	string  是
    data	obj	是
    rtime	string	是	请求时间戳
    result	string  是	图像二进制码base64编码后字符串
    imgcode    string  是	图像标识码
 */
public class ImgCodeData implements Serializable{
    private String rtime;
    private String result;
    private String imgcode;

    public String getRtime() {
        return rtime;
    }

    public void setRtime(String rtime) {
        this.rtime = rtime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getImgcode() {
        return imgcode;
    }

    public void setImgcode(String imgcode) {
        this.imgcode = imgcode;
    }
}
