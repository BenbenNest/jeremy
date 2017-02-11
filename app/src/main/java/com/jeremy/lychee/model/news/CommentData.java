package com.jeremy.lychee.model.news;

import java.util.List;

/**
 * Created by zhangying-pd on 2016/3/22.
 *
 * 评论对象
 */
public class CommentData {
    private List<Comment> comments;
    private int next;
    private int total;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
