package com.jeremy.lychee.model.user;

import com.jeremy.lychee.db.NewsListData;

public class UserMessage {
    public int type;
    public long id;
    public String user;
    public String userpic;
    public String uid;
    public long time;
    public ExtData extData;
    public boolean isUnRead;

    public class ExtData extends NewsListData {
        public String commentData;
    }
}
