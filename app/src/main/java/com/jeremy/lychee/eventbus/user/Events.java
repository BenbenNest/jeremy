package com.jeremy.lychee.eventbus.user;

public class Events {
    public static class LoginCancel {
    }
    public static class LoginOk {
    }

    public static class LoginErr {
    }

    public static class Logout {
    }

    public static class OnDragEnabled {
        public boolean enabled;
        public OnDragEnabled(boolean val){
            this.enabled = val;
        }
    }

    public static class HasMoreHotChannels {
        public boolean val;
        public HasMoreHotChannels (boolean val){
            this.val = val;
        }
    }

    public static class ShowHotChannels {}
    public static class ShowSubTopics {}

    public static class OnAlbumListUpdated {
        public Object mData;

        public OnAlbumListUpdated(Object data) {
            mData = data;
        }
    }

    public static class OnAlbumDetailUpdated {
        public String transmitId;

        public OnAlbumDetailUpdated(String transmitId) {
            this.transmitId = transmitId;
        }
    }

    public static class OnSubscribedChannelUpdated {
        public int channelId;
        public boolean sub;

        public OnSubscribedChannelUpdated(int id, boolean sub) {
            channelId = id;
            this.sub = sub;
        }
    }

    public static class OnWeMediaChannelInfoUpdated {
        public OnWeMediaChannelInfoUpdated() {}
    }

    public static class OnSubscribedTopicListUpdated {
        public OnSubscribedTopicListUpdated() {}
    }

    public static class OnMediaChannelDelete {
        public OnMediaChannelDelete() {}
    }

    public static class OnMessageReceived {
        public OnMessageReceived() {}
    }

    public static class OnArticleEmpty{
        public OnArticleEmpty() {}
    }

    /**
     * Bug 72448 -
     * 【自频道】【发现】在发现频道点击登录后取消登录，出现刷新
     */
    public static class IgnoreRefreshNextTime {
        public IsIgnore mData;
        public IgnoreRefreshNextTime(IsIgnore data) {
            mData = data;
        }
        public static class IsIgnore{
            private volatile boolean flg_ ;
            public IsIgnore(boolean bl){
                flg_ = bl;
            }
            public synchronized boolean get(){
                if (flg_) {
                    flg_ = false;
                    return true;
                }
                return false;
            }
        }

    }

}
