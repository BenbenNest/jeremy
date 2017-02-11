package com.jeremy.lychee.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table PROGRAM.
 */
public class Program {

    /** Not-null value. */
    private String id;
    private String column_name;
    private String channel_name;
    private String start_time;
    private String end_time;
    private String is_live;
    private Boolean is_sub;

    public Program() {
    }

    public Program(String id) {
        this.id = id;
    }

    public Program(String id, String column_name, String channel_name, String start_time, String end_time, String is_live, Boolean is_sub) {
        this.id = id;
        this.column_name = column_name;
        this.channel_name = channel_name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.is_live = is_live;
        this.is_sub = is_sub;
    }

    /** Not-null value. */
    public String getId() {
        return id;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setId(String id) {
        this.id = id;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getIs_live() {
        return is_live;
    }

    public void setIs_live(String is_live) {
        this.is_live = is_live;
    }

    public Boolean getIs_sub() {
        return is_sub;
    }

    public void setIs_sub(Boolean is_sub) {
        this.is_sub = is_sub;
    }

}