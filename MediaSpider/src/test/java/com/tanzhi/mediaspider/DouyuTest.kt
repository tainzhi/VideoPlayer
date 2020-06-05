package com.tanzhi.mediaspider

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.Network
import com.android.volley.RequestQueue
import com.android.volley.ResponseDelivery
import com.android.volley.toolbox.NoCache
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/5 14:22
 * @description:
 **/

@RunWith(AndroidJUnit4::class)
class DouyuTest1 {
    lateinit var delivery: ResponseDelivery

    @Mock lateinit var mockNetwork: Network

    @Before
    fun setUp() {
        delivery = ImmediateResponseDelivery()
        initMocks(this)
    }

    @Test
    fun getRoomId() {
        val queue = RequestQueue(NoCache(), mockNetwork, 0, delivery)
        val douyu = Douyu()
        douyu.getCode(queue)

    }
}