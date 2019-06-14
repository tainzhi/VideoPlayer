package com.qfq.tainzhi.videoplayer.bean;

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
public class DouyuRoomBean {
    private int room_id;
    private String room_thumb;
    private String room_src;
    private String room_name;
    private String nickname;
    private String owner_name;
    private String roomSrc;
    private int online;
    private int popularity;
    private int isLive;
    
    public int getRoom_id() {
        return room_id;
    }
    
    public void setRoom_id(int mRoom_id) {
        room_id = mRoom_id;
    }
    
    public String getRoom_thumb() {
        return room_thumb;
    }
    
    public void setRoom_thumb(String mRoom_thumb) {
        room_thumb = mRoom_thumb;
    }
    
    public String getRoom_src() {
        return room_src;
    }
    
    public void setRoom_src(String mRoom_src) {
        room_src = mRoom_src;
    }
    
    public String getRoom_name() {
        return room_name;
    }
    
    public void setRoom_name(String mRoom_name) {
        room_name = mRoom_name;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String mNickname) {
        nickname = mNickname;
    }
    
    public String getOwner_name() {
        return owner_name;
    }
    
    public void setOwner_name(String mOwner_name) {
        owner_name = mOwner_name;
    }
    
    public String getRoomSrc() {
        return roomSrc;
    }
    
    public void setRoomSrc(String mRoomSrc) {
        roomSrc = mRoomSrc;
    }
    
    public int getOnline() {
        return online;
    }
    
    public void setOnline(int mOnline) {
        online = mOnline;
    }
    
    public int getPopularity() {
        return popularity;
    }
    
    public void setPopularity(int mPopularity) {
        popularity = mPopularity;
    }
    
    public int getIsLive() {
        return isLive;
    }
    
    public void setIsLive(int mIsLive) {
        isLive = mIsLive;
    }
    
    @Override
    public String toString() {
        return "DouyuRoomBean{" +
                       "room_id=" + room_id +
                       ", room_thumb='" + room_thumb + '\'' +
                       ", room_src='" + room_src + '\'' +
                       ", room_name='" + room_name + '\'' +
                       ", nickname='" + nickname + '\'' +
                       ", owner_name='" + owner_name + '\'' +
                       ", roomSrc='" + roomSrc + '\'' +
                       ", online=" + online +
                       ", popularity=" + popularity +
                       ", isLive=" + isLive +
                       '}';
    }
}
