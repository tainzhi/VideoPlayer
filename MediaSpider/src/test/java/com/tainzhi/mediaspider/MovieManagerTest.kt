package com.tainzhi.mediaspider

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tainzhi.mediaspider.film.bean.MovieManager
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*
import org.junit.*
import org.junit.runner.*
import java.io.File
import java.net.URL

/**
 * File:     MovieManager
 * Author:   tainzhi
 * Created:  2020/12/20 12:05
 * Mail:     QFQ61@qq.com
 * Description:
 */
@RunWith(AndroidJUnit4::class)
class MovieManagerTest {

    val context = InstrumentationRegistry.getInstrumentation().targetContext

    val sitePath = "assets/movie_site.json"
    val configPath = "assets/movie_site_config.json"

    val movieManager: MovieManager = MovieManager.getInstance(
            context,
            getFileFromPath(this, configPath),
            getFileFromPath(this, sitePath),
    )

    @Test
    fun fileExistsTest() {
        fileObjectShouldNotBeNull(sitePath)
        fileObjectShouldNotBeNull(configPath)
    }

    @Test
    fun getContextConfig() {
        movieManager.curUseSourceConfig().requestHomeData { homedata ->
            if (homedata != null) {
            }
        }
    }

    private fun getFileFromPath(obj: Any, fileName: String): File {
        val classLoader = obj.javaClass.classLoader
        val resource: URL = classLoader.getResource(fileName)
        return File(resource.getPath())
    }

    @Throws(Exception::class)
    private fun fileObjectShouldNotBeNull(path: String) {
        val file: File = getFileFromPath(this, path)
        assertThat(file, notNullValue())
    }
}