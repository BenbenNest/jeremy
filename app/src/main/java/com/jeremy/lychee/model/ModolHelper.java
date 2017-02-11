package com.jeremy.lychee.model;

import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.model.news.SquareModel;

public class ModolHelper {
    public static NewsListData SquareElementModelToNewsListData(SquareModel.ElementModel model) {
        NewsListData data = new NewsListData();
        data.setId(model.getId());
        data.setNid(model.getNid());
        data.setMd5(model.getMd5());
        data.setCid(model.getCid());
        data.setAlbum_pic(model.getAlbum_pic());
        data.setTitle(model.getTitle());
        data.setUrl(model.getUrl());
        data.setPdate(model.getPdate());
        data.setSource(model.getSource());
        data.setNews_from(model.getNews_from());
        data.setNews_type(model.getNews_type());
        data.setOpen_type(model.getOpen_type());
        data.setNews_stick(model.getNews_stick());
        data.setNews_stick_time(model.getNews_stick_time());
        data.setNews_data(model.getNews_data());
        data.setZm(model.getZm());
        data.setComment(model.getComment());
        data.setShare(model.getShare());
        data.setTransmit_num(model.getTransmit_num());
        return data;
    }
}
