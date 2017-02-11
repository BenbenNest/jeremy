package com.jeremy.lychee.eventbus.news;

import com.jeremy.lychee.db.NewsListData;

public class Events {
    public static class GoToChannelByIndex {
        public int index;

        public GoToChannelByIndex(int index) {
            this.index = index;
        }
    }

    public static class GoToScene {
        public String sceneId;
        public String name;

        public GoToScene(String sceneId, String name) {
            this.sceneId = sceneId;
            this.name = name;
        }
    }

    public static class GoToChannelManage {

    }

    public static class UpdateNewsChannelListEvent {
        public int currentChannelIndex;

        public UpdateNewsChannelListEvent(int currentChannelIndex) {
            this.currentChannelIndex = currentChannelIndex;
        }
    }

    public static class UpdateNewsChannelError {
        public String errorMsg;

        public UpdateNewsChannelError(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }

    public static class NewsLayoutChangeEvent {
        public int level;

        public NewsLayoutChangeEvent(int level) {
            this.level = level;
        }
    }


    public static class OnNewsDeleted {
        public NewsListData data;
        public OnNewsDeleted(NewsListData data) {
            this.data = data;
        }
    }

    public static class UpdateNewsEvent {

    }

    public static class OnNewsAlbumChanged {
        public String albumId;
        public OnNewsAlbumChanged(String albumId) {
            this.albumId = albumId;
        }
    }
	 public static class HidePopWinEvent{

    }
    public static class ChannelCoverEvent{
        public boolean show;

        public ChannelCoverEvent(boolean show) {
            this.show = show;
        }
    }

    public static class ChangeNewsStatusEvent{
        public boolean isEdited;

        public ChangeNewsStatusEvent(boolean isEdited) {
            this.isEdited = isEdited;
        }
    }

    public static class ZanClick {
        public boolean isclick;
        public ZanClick(boolean zan){
            this.isclick = zan;
        }
    }
}
