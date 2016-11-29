/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeremy.lychee.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * <p>
 * Run it as a Java application (not Android).
 *
 * @author Markus
 */
public class LianxianDaoGenerator {

    public static void main(String[] args) throws Exception {

//        Schema schema = new Schema(16, "com.qihoo.lianxian.db");
        Schema schema = new Schema(16, "com.jeremy.lychee.db2");

        generateWeMediaChannel(schema);
        generateWeMediaTopic(schema);
        generateNewsChannel(schema);
        generateNewsListData(schema);
        generateLiveThumbsUp(schema);
        generateProgram(schema);
        generateLiveColumnListData(schema);
        generateLiveHotListData(schema);
        new DaoGenerator().generateAll(schema, "app/src/main/java/");
    }

    private static void generateWeMediaChannel(Schema schema) {
        Entity channel = schema.addEntity("WeMediaChannel");
        channel.addStringProperty("id").primaryKey().notNull();
        channel.addStringProperty("uid");
        channel.addStringProperty("c_id");
        channel.addStringProperty("source_type");
        channel.addStringProperty("topic_tag");
        channel.addStringProperty("name").notNull();
        channel.addStringProperty("domain");
        channel.addStringProperty("icon");
        channel.addStringProperty("backimg");
        channel.addStringProperty("summary");
        channel.addStringProperty("weight");
        channel.addIntProperty("is_deleted");
        channel.addIntProperty("is_public");
        channel.addStringProperty("create_time");
        channel.addBooleanProperty("is_sub");
        channel.addBooleanProperty("is_my");
        channel.addBooleanProperty("is_hot");
        channel.addStringProperty("nickname");
        channel.addIntProperty("news_num");
        channel.addIntProperty("sub_num");
        channel.addIntProperty("increase_sub_num");
        channel.addIntProperty("update_news_num");
        channel.addStringProperty("follow");//XX万+
        channel.addStringProperty("fans");//XX万+
    }


    private static void generateNewsChannel(Schema schema) {
        Entity entity = schema.addEntity("NewsChannel");
        entity.addStringProperty("cid").primaryKey().notNull();
        entity.addStringProperty("cname");
        entity.addStringProperty("icon");
        entity.addStringProperty("tagname");
        entity.addStringProperty("init");
        entity.addStringProperty("is_start");
        entity.addBooleanProperty("isShow");
    }

    private static void generateNewsListData(Schema schema) {

        Entity entity = schema.addEntity("NewsListData");
        entity.addLongProperty("_id").primaryKey().autoincrement();
        entity.addStringProperty("id");
        entity.addStringProperty("nid");
        entity.addStringProperty("uid");
        entity.addStringProperty("channel");
        entity.addLongProperty("time");
        entity.addStringProperty("md5");
        entity.addStringProperty("cid");
        entity.addStringProperty("album_pic");
        entity.addStringProperty("title");
        entity.addStringProperty("zm");
        entity.addStringProperty("url");
        entity.addStringProperty("comment");
        entity.addStringProperty("pdate");
        entity.addStringProperty("source");
        entity.addStringProperty("module");
        entity.addStringProperty("share");
        entity.addStringProperty("news_from");
        entity.addStringProperty("open_type");
        entity.addStringProperty("news_type");
        entity.addStringProperty("news_data");
        entity.addStringProperty("news_stick");
        entity.addStringProperty("news_stick_time");
        entity.addStringProperty("transmit_num");
        entity.addStringProperty("is_focus");
        entity.addStringProperty("uuid_flag");
        entity.addStringProperty("video_length");
        //视频feed的信息
        entity.addStringProperty("live_channel_id");
        entity.addStringProperty("live_topic_id");
        entity.addStringProperty("live_channel_tag");
        //topic信息
        entity.addStringProperty("summary");
    }

    private static void generateLiveThumbsUp(Schema schema) {
        Entity channel = schema.addEntity("ThumbsUp");
        channel.addStringProperty("id").primaryKey().index().notNull();
        channel.addBooleanProperty("voted").notNull();
    }

    private static void generateProgram(Schema schema) {
        Entity channel = schema.addEntity("Program");
        channel.addStringProperty("id").primaryKey().notNull();
        channel.addStringProperty("column_name");
        channel.addStringProperty("channel_name");
        channel.addStringProperty("start_time");
        channel.addStringProperty("end_time");
        channel.addStringProperty("is_live");
        channel.addBooleanProperty("is_sub");

    }

    private static void generateLiveColumnListData(Schema schema) {
        Entity channel = schema.addEntity("LiveColumnListData");
        channel.addStringProperty("id").primaryKey().notNull();
        channel.addStringProperty("video_channel_name");
        channel.addStringProperty("video_name");
        channel.addStringProperty("video_column_name");
        channel.addStringProperty("video_desc");
        channel.addStringProperty("video_icon");
        channel.addStringProperty("video_img");
        channel.addStringProperty("video_islive");
        channel.addStringProperty("video_relate_id");
        channel.addStringProperty("video_play_url");
        channel.addStringProperty("tag");
        channel.addStringProperty("pdate");
        channel.addStringProperty("video_duration");
        channel.addStringProperty("video_type");
        channel.addStringProperty("video_publish_status");
        channel.addStringProperty("channel_cid");
        channel.addStringProperty("column_id");
        channel.addLongProperty("time");
        channel.addStringProperty("news_type");
        channel.addStringProperty("news_from");
    }


    private static void generateLiveHotListData(Schema schema) {

        Entity channel = schema.addEntity("LiveHotItem");
        channel.addStringProperty("id").primaryKey().notNull();
        channel.addStringProperty("video_channel_name");
        channel.addStringProperty("video_column_id");
        channel.addStringProperty("video_column_name");
        channel.addStringProperty("video_type");
        channel.addStringProperty("video_desc");
        channel.addStringProperty("video_name");
        channel.addStringProperty("video_duration");
        channel.addStringProperty("video_icon");
        channel.addStringProperty("video_publish_status");
        channel.addStringProperty("video_img");
        channel.addStringProperty("video_islive");
        channel.addStringProperty("video_play_url");
        channel.addStringProperty("video_url");
        channel.addStringProperty("pdate");
        channel.addStringProperty("tag");
        channel.addStringProperty("watches");
        channel.addStringProperty("is_seg");
        channel.addStringProperty("news_type");
        channel.addStringProperty("news_from");
        channel.addStringProperty("zm");
        channel.addStringProperty("video_key");
        channel.addStringProperty("comment");
        channel.addStringProperty("ding");
        channel.addStringProperty("share");
        channel.addStringProperty("is_focus");
        channel.addLongProperty("time");
        channel.addStringProperty("source");


    }


    private static void generateWeMediaTopic(Schema schema) {
        Entity channel = schema.addEntity("WeMediaTopic");
        channel.addIntProperty("id").primaryKey().notNull();
        channel.addStringProperty("title");
        channel.addIntProperty("weight");
        channel.addStringProperty("image");
        channel.addStringProperty("create_time");
        channel.addStringProperty("keyword");
        channel.addStringProperty("update_time");
        channel.addBooleanProperty("is_sub");
    }


}
