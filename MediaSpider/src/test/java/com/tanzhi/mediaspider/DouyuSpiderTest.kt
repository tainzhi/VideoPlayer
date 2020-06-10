package com.tanzhi.mediaspider

import org.junit.After
import org.junit.Before
import org.junit.Test

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
        douyuSpider.getRoomLive("9999")
    }

    @Test
    fun getAllGame() {
        douyuSpider.getAllRoom()
    }
    
    @After
    fun tearDown() {
    }
}