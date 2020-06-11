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

class TVSpiderTest {

    lateinit var tvSpider: TVSpider
    
    @Before
    fun setUp() {
        tvSpider = TVSpider.getInstance()
    }

    @Test
    fun getTvProgram() {
        // tvSpider.getTvProgram("cctv")
        tvSpider.getTvProgram("satellite")
    }
    
    @After
    fun tearDown() {
    }
}