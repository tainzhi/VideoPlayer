package com.tanzhi.android.danmu.advancedanmu.control

import android.os.Handler
import android.os.Message
import com.tanzhi.android.danmu.advancedanmu.DanmuModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:39
 * @description:
 **/

class Producer(val producedPool: ProducedPool, val consumedPool: ConsumedPool) {
    private lateinit var producerHandler: ProducerHandler
    fun start() {
        producerHandler = ProducerHandler(this)
    }

    fun produce(index: Int, danmuModel: DanmuModel) {
        val produceMessage = ProduceMessage(index, danmuModel)
        val message = producerHandler.obtainMessage().apply {
            obj = produceMessage
            what = 2
        }
        producerHandler.sendMessage(message)
    }

    fun jumpQueue(danmuModels: List<DanmuModel>) { producedPool.jumpQueue(danmuModels)}

    fun release() {
        producerHandler.run {
            removeMessages(1)
            release()
        }

    }
}

class ProducerHandler(val producer: Producer): Handler() {
    companion object {
        const val SLEEP_TIME = 100L
    }
    init {
        obtainMessage(1).sendToTarget()
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when(msg.what) {
            1 -> {
                producer.producedPool.dispatch()?.let {it ->
                    producer.consumedPool.put(it)
                }
                val message = obtainMessage().apply { what = 1 }
                sendMessageDelayed(message, SLEEP_TIME)
            }
            2 -> {
                val producerMessage = msg.obj as ProduceMessage
                producer.producedPool.addDanmu(producerMessage.index, producerMessage.danmuModel)
            }
        }
    }

    fun release() {
        producer.producedPool.clear()
    }
}

data class ProduceMessage(var index: Int, val danmuModel: DanmuModel)

