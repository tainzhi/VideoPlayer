package com.qfq.tainzhi.videoplayer.bean

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
class DouyuRoomBean constructor() {
    private var room_id: Int = 0
    private var room_thumb: String? = null
    private var room_src: String? = null
    private var room_name: String? = null
    private var nickname: String? = null
    private var owner_name: String? = null
    private var roomSrc: String? = null
    private var online: Int = 0
    private var popularity: Int = 0
    private var isLive: Int = 0
    fun getRoom_id(): Int {
        return room_id
    }
    
    fun setRoom_id(mRoom_id: Int) {
        room_id = mRoom_id
    }
    
    fun getRoom_thumb(): String? {
        return room_thumb
    }
    
    fun setRoom_thumb(mRoom_thumb: String?) {
        room_thumb = mRoom_thumb
    }
    
    fun getRoom_src(): String? {
        return room_src
    }
    
    fun setRoom_src(mRoom_src: String?) {
        room_src = mRoom_src
    }
    
    fun getRoom_name(): String? {
        return room_name
    }
    
    fun setRoom_name(mRoom_name: String?) {
        room_name = mRoom_name
    }
    
    fun getNickname(): String? {
        return nickname
    }
    
    fun setNickname(mNickname: String?) {
        nickname = mNickname
    }
    
    fun getOwner_name(): String? {
        return owner_name
    }
    
    fun setOwner_name(mOwner_name: String?) {
        owner_name = mOwner_name
    }
    
    fun getRoomSrc(): String? {
        return roomSrc
    }
    
    fun setRoomSrc(mRoomSrc: String?) {
        roomSrc = mRoomSrc
    }
    
    fun getOnline(): Int {
        return online
    }
    
    fun setOnline(mOnline: Int) {
        online = mOnline
    }
    
    fun getPopularity(): Int {
        return popularity
    }
    
    fun setPopularity(mPopularity: Int) {
        popularity = mPopularity
    }
    
    fun getIsLive(): Int {
        return isLive
    }
    
    fun setIsLive(mIsLive: Int) {
        isLive = mIsLive
    }
    
    public override fun toString(): String {
        return ("DouyuRoomBean{" +
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
                '}')
    }
}