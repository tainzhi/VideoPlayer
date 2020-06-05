package com.tanzhi.mediaspider

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2020/6/5 09:55
 * @descriptione
 */

@RunWith(AndroidJUnit4::class)
class DouyuTest {

    lateinit var context: Context
    lateinit var douyu: Douyu

    @Before
    fun setUp(){
        context = InstrumentationRegistry.getInstrumentation().targetContext
        douyu = Douyu(context)
    }
    @Test
    fun getCode() {
        douyu.getCode()
    }

    @After
    fun tearDown() {
    }
}