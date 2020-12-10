package com.tainzhi.android.videoplayer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tainzhi.android.videoplayer.network.VideoService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers.*
import org.hamcrest.core.IsNull.notNullValue
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.*
import org.junit.runners.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * File:     VideoServiceTest
 * Author:   tainzhi
 * Created:  2020/12/10 10:27
 * Mail:     QFQ61@qq.com
 * Description:
 */
@RunWith(JUnit4::class)
class VideoServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: VideoService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(
                        MoshiConverterFactory.create(
                                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        )
                )
                .build()
                .create(VideoService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getGameRoomsTest() = runBlocking {
        enqueueResponse("api-response/DouyuRoom.json")
        val rooms = service.getGameRooms("lol", 0, 10).data

        assertThat(rooms, notNullValue())
        assertThat(rooms?.size, `is`(2))
        assertThat(rooms[0].room_id, `is`("5384600"))
    }

    @Test
    fun getRecommendRoomsTest() = runBlocking {
        enqueueResponse("api-response/DouyuRoom.json")
        val rooms = service.getRecommendRooms(0, 10).data

        assertThat(rooms, notNullValue())
        assertThat(rooms.size, `is`(2))
        assertThat(rooms[0].room_id, `is`("5384600"))
    }

    @Test
    fun getDouyuGamesTest() = runBlocking {
        enqueueResponse("api-response/DouyuGame.json")
        val games = service.getAllGames().data

        assertThat(games, notNullValue())
        assertThat(games.size, `is`(4))
        assertThat(games[0].cate_id, `is`(1855))
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
                mockResponse
                        .setBody(source.readString(Charsets.UTF_8))
        )
    }
}