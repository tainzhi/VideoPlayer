package com.tanzhi.mediaspider

import org.jsoup.Jsoup

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/11 下午2:48
 * @description: 爬取tv频道当前节目
 **/

class TVSpider {

    /*
    <tr>
     <td class="tdchn"><a href="/program/CCTV/CCTV1" title="CCTV-1综合" tp="32" res="29" class="black_link">CCTV-1综合</a></td>
     <td class="pt15 font16 td1"><a href="/drama/YmspXGVf" title="谁说我结不了婚">谁说我结不了婚第36集</a><br><span class="block mt8 mb12 font14" style="color:#999">15:47</span></td>
     <td class="tdbar">
      <div class="pbar" title="余下23分钟">
       <div class="progbar" style="width:56%">
       </div>
      </div></td>
     <td class="pt15 font16 td2"><a title="第一动画乐园" href="/tvcolumn/MlIi" tp="12" res="2020-06-11 16:40_MlIi">第一动画乐园</a>-2020-445<br><span class="block mt8 mb12 font14" style="color:#999">16:40</span></td>
     <td class="pt15 font16 td3"><a href="/tvcolumn/MlIi" title="第一动画乐园" tp="12" res="2020-06-11 17:03_MlIi">第一动画乐园-2020-446</a><br><span class="block mt8 mb12 font14" style="color:#999">17:03</span></td>
    </tr>
     */
    /**
     * 从 [https://www.tvmao.com/program/playing/cctv]
     * 获取各卫视频道当前直播节目
     *
     * @param type cctv 或者 satellite, 即央视和地方卫视
     */
    fun getTvProgram(type: String): Map<String, String> {
        val response = KRequest().get("https://www.tvmao.com/program/playing/$type/")
        val doc = Jsoup.parse(response)
        val tvProgramMap = hashMapOf<String, String>()
        doc.select("tr").forEach { item ->
            // 获取 /program/CCTV/CCTV1, 正则表达式捕获组 捕获1组
            val href = item.select("tr")[0].select("a")[0].attr("href")
            val key = (".*/(.*)").toRegex().findAll(href).first().groupValues[1]
            val program = item.select("td.pt15")[0].select("[href]").text()
            tvProgramMap[key] = program
        }
        return tvProgramMap
    }

    companion object {
        @Volatile private var instance: TVSpider? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: TVSpider().also { instance = it}
        }
    }

}

