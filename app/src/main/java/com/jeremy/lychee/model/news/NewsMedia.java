package com.jeremy.lychee.model.news;

public class NewsMedia {

    private int id;
    private String name;
    private String icon;
    private NewsAlbum album;

    public int getId() {
        return id;
    }

    public NewsAlbum getAlbum() {
        return album;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAlbum(NewsAlbum album) {
        this.album = album;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public class NewsAlbum{
        private String id;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
