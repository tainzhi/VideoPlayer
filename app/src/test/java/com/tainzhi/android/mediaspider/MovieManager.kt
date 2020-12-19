package com.tainzhi.android.mediaspider

import androidx.test.ext.junit.runners.AndroidJUnit4

import androidx.test.platform.app.InstrumentationRegistry
import com.tainzhi.android.videoplayer.spider.movie.MovieManager
import org.junit.*
import org.junit.runner.*

/**
 * File:     MovieManager
 * Author:   tainzhi
 * Created:  2020/12/19 20:06
 * Mail:     QFQ61@qq.com
 * Description:
 */
@RunWith(AndroidJUnit4::class)
class MovieManagerTest {

    val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun getConfig() {
        MovieManager(context.applicationContext).config
    }

    @Test
    fun getConfigMap() {
    }

    @Test
    fun getMovieSites() {
    }

    @Test
    fun sourceConfigs() {
    }
}