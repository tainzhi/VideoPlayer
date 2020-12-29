package com.tainzhi.mediaspider

import com.tainzhi.mediaspider.spider.DouyuSpider
import org.junit.*

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2020/6/9 14:00
 * @description:
 */

class DouyuSpiderTest {

    lateinit var douyuSpider: DouyuSpider

    @Before
    fun setUp() {
        douyuSpider = DouyuSpider.getInstance()
    }

    @Test
    fun getRoomLive() {
        douyuSpider.getRoomCircuitId("6584315")
    }

    @After
    fun tearDown() {
    }
}