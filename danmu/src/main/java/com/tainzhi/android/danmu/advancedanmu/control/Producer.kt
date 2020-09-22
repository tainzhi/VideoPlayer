package com.tainzhi.android.danmu.advancedanmu.control

import android.os.Handler
import android.os.Message
import com.tainzhi.android.danmu.advancedanmu.Danmu
import com.tainzhi.android.danmu.advancedanmu.control.ProducerHandler.Companion.SIGNAL_CONSUMED_DANMU

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
    
    fun produce(index: Int, danmu: Danmu) {
        val produceMessage = ProduceMessage(index, danmu)
        val message = producerHandler.obtainMessage().apply {
            obj = produceMessage
            what = ProducerHandler.SIGNAL_ADD_DANMU
        }
        producerHandler.sendMessage(message)
    }
    
    fun jumpQueue(danmus: List<Danmu>) {
        producedPool.jumpQueue(danmus)
    }
    
    fun release() {
        producerHandler.run {
            removeMessages(SIGNAL_CONSUMED_DANMU)
            release()
        }
        
    }
}

class ProducerHandler(private val producer: Producer) : Handler() {
    companion object {

        const val SLEEP_TIME = 100L
        const val SIGNAL_CONSUMED_DANMU = 1
        const val SIGNAL_ADD_DANMU = 2
    }

    init {
        obtainMessage(SIGNAL_CONSUMED_DANMU).sendToTarget()
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when (msg.what) {
            SIGNAL_CONSUMED_DANMU -> {
                // 定时每100ms取出ProducedPool中缓存的Danmu发送到 ConsumedPool
                producer.producedPool.dispatch()?.let { it ->
                    producer.consumedPool.put(it)
                }
                val message = obtainMessage().apply { what = 1 }
                sendMessageDelayed(message, SLEEP_TIME)
            }
            SIGNAL_ADD_DANMU -> {
                // 新的弹幕添加到ProducedPool, 缓存起来
                val producerMessage = msg.obj as ProduceMessage
                producer.producedPool.addDanmu(producerMessage.index, producerMessage.danmu)
            }
        }
    }

    fun release() {
        producer.producedPool.clear()
    }
}

data class ProduceMessage(var index: Int, val danmu: Danmu)

