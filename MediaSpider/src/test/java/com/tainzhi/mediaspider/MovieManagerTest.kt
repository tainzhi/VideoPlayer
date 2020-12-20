package com.tainzhi.mediaspider

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
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

    @Test
    fun getConfig() {
        val mainAssetsBasePath = "../../main/assets/"
        val fileName = "assets/film_site.json"
        // javaClass.classLoader!!.getResourceAsStream(mainAssetsBasePath + fileName).use { inputStream ->
        //     val readString = inputStream.bufferedReader().use { it.readText() }
        // }
        fileObjectShouldNotBeNull(fileName)
    }

    @Test
    fun getContextConfig() {

    }

    private fun getFileFromPath(obj: Any, fileName: String): File {
        val classLoader = obj.javaClass.classLoader
        val resource: URL = classLoader.getResource(fileName)
        return File(resource.getPath())
    }

    @Throws(Exception::class)
    fun fileObjectShouldNotBeNull(path: String) {
        val file: File = getFileFromPath(this, path)
        assertThat(file, notNullValue())
    }
}