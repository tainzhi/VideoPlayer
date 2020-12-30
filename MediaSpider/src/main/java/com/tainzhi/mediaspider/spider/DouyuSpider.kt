package com.tainzhi.mediaspider.spider

import com.tainzhi.mediaspider.utils.JsEngine
import com.tainzhi.mediaspider.utils.OkHttpUtil
import com.tainzhi.mediaspider.utils.fromJson
import com.tainzhi.mediaspider.utils.toMD5
import org.jsoup.Jsoup
import java.lang.System.currentTimeMillis

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/9 13:32
 * @description: 使用[KRequest] 爬取斗鱼信息
 *
 * 用Volley爬取斗鱼直播间直播源. 因为使用了回调,
 * 非常不方便用于爬虫(比如爬取room id, 需要多个请求和回调, 繁琐)
 * 建议使用 KRequest实现的[DouyuSpider]
 *
 * 通过chrome debug工具获取的地址 https://tc-tct.douyucdn2.cn/dyliveflv1/562483rmTwjem0AN_1200p.flv
 * 其中_1200p 对应清晰度
 **/

class DouyuSpider {

    private var rid: String = ""
    private val did: String = "10000000000000000000000000001501"
    private var t10: String = ""
    private var ver = "219032101"

    fun getRoomCircuitId(id: String): String {
        this.rid = id
        val t13 = currentTimeMillis()
        t10 = (t13 / 1000).toString()

        val url = "https://playweb.douyucdn.cn/lapi/live/hlsH5Preview/$rid"
        val auth = (rid + t13).toMD5()
        val header = mutableMapOf(
                "rid" to rid,
                "time" to t13.toString(),
                "auth" to auth
        )

        val postData = mutableMapOf(
                "rid" to rid,
                "did" to did,
        )
        lateinit var livingRoomId: String
        try {
            val htmlPage = OkHttpUtil.instance.post(url, header, postData)
            val response = htmlPage.fromJson<RoomBean>()
            if (response != null) {
                if (response.error == 0 && response.data != null) {
                    val rtmpLive = response.data!!.rtmpLive
                    if (rtmpLive.contains("mix=1")) {
                        throw Exception("circuit not found; PKing")
                    } else {
                        val regex = Regex(pattern = "[0-9a-zA-Z]*")
                        val found = regex.find(rtmpLive)
                        if (found != null) {
                            livingRoomId = found.value
                        } else {
                            throw Exception("regex to find room live url failed")
                        }
                    }
                } else if (response.error == 102) {
                    // 房间不存在
                    throw RoomNotExist()
                } else if (response.error == 104) {
                    // 房间未开播
                    throw RoomNotLiving()
                } else {
                    return getIdByExecJs(rid)
                }
            } else {
                return getIdByExecJs(rid)
            }
        } catch (e: Exception) {
            return getIdByExecJs(rid)
        }
        return livingRoomId
    }

    /**
     * 因为直接通过header和post body无法获取到直播间直播编号
     * 只能通过执行js代码获取
     */
    private fun getIdByExecJs(id: String): String {
        val htmlPage = OkHttpUtil.instance.request("https://m.douyu.com/${id.trim()}")
        try {
            val ubFun = Regex(pattern = "(function ub98484234.*)\\s(var.*)").find(htmlPage)!!.value
            val funcUb9 = Regex("eval.*;\\}").replace(ubFun, "strc;\\}")
            val funResult = JsEngine.execJs(funcUb9, "ub98484234")

            val v = Regex("v=(\\d+)").find(funResult)!!.groupValues[1]
            val rb = (rid + did + t10 + v).toMD5()

            val originalSignFun = Regex("return rt;\\}\\);?").replace(funResult, "return rt;\\}")
            val signFun = originalSignFun
                    .replace("(function (", "function sign(")
                    .replace("CryptoJS.MD5(cb).toString()", "\"${rb}\"")

            val signResult = JsEngine.execJs(signFun, "sign", rid, did, t10)
            // Regex("""sign=(\S+)""").find(params)
            val signParam = Regex("sign=(\\S*)").find(signResult)!!.groupValues[1]
            val postBodyMap = mapOf(
                    "v" to v,
                    "did" to did,
                    "tt" to t10,
                    "sign" to signParam,
                    "ver" to ver,
                    "rid" to rid,
                    "rate" to "-1",
            )

            val url = "https://m.douyu.com/api/room/ratestream"
            val response: String = OkHttpUtil.instance.post(url, postMap = postBodyMap)
            val livingRoomId: String = Regex("(\\d{1,7}[0-9a-zA-Z]+)_?\\d{0,4}(.m3u8|/playlist)").find(response)!!.groupValues[1]
            return livingRoomId
        } catch (e: Exception) {
            throw NotFoundException(e.message
                    ?: "throw exec js to get room:${rid} living id faled", e.cause)
        }
    }


    /*
    <li class="layout-Classify-item"><a class="layout-Classify-card secondCateCard" href="/g_jdqs" target="_blank"><i class="secondCateCard-icon">
        <div class="LazyLoad is-visible DyImg secondCateCard-img">
          <img src="https://sta-op.douyucdn.cn/dycatr/08dcd21b98a6b71bb1d37b0c30376734.png?x-oss-process=image/format,webp/quality,q_75" class="DyImg-content is-normal " />

         </div></i><strong>绝地求生</strong>

         fixme
         获取的图片是默认的, 因为douyu懒加载了图片, 还需要找到图片加载地址
     */
    fun getAllRoom() {
        val response = OkHttpUtil.instance.request("https://www.douyu.com/directory")

        val doc = Jsoup.parse(response)
        doc.select("li.layout-Classify-item a.layout-Classify-card").forEach { item ->
            if (item.attr("href").isNotEmpty()) {
                val type = item.attr("href")
                val href = item.select("img[src]").attr("src")
                val name = item.select("strong").text()
            }
        }

    }

    companion object {
        @Volatile
        private var instance: DouyuSpider? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: DouyuSpider().also { instance = it }
        }
    }
}
