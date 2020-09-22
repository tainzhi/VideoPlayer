package com.tainzhi.android.videoplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tainzhi.android.danmu.advancedanmu.DanMuHelper
import com.tainzhi.android.danmu.advancedanmu.model.DanmuEntity
import com.tainzhi.android.videoplayer.R
import kotlinx.android.synthetic.main.activity_danmu.addDanmuBtn
import kotlinx.android.synthetic.main.activity_danmu.danmuContainerView
import kotlinx.android.synthetic.main.activity_danmu.hideDanmuBtn

class DanmuActivity : AppCompatActivity() {

    private lateinit var danmuHelper: DanMuHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_danmu)

        danmuHelper = DanMuHelper(this, danmuContainerView)

        addDanmuBtn.setOnClickListener {
            danmuHelper.addDanmu(DanmuEntity(
                    "https://q.qlogo.cn/qqapp/100229475/E573B01150734A02F25D8E9C76AFD138/100",
                    "张三",
                    "0719",
                    5,
                    1,
                    "欲穷千里目, 更上一层楼~~~"
            ))
        }

        hideDanmuBtn.setOnClickListener {
            danmuHelper.hideAllDanmu()
        }
    }
}