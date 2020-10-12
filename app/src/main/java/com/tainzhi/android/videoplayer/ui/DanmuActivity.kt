package com.tainzhi.android.videoplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tainzhi.android.danmu.advancedanmu.DanMuHelper
import com.tainzhi.android.danmu.advancedanmu.model.DanmuEntity
import com.tainzhi.android.videoplayer.R
import kotlinx.android.synthetic.main.activity_danmu.addDanmuBtn
import kotlinx.android.synthetic.main.activity_danmu.danmuContainerView
import kotlinx.android.synthetic.main.activity_danmu.hideDanmuBtn
import kotlin.random.Random

class DanmuActivity : AppCompatActivity() {

    private lateinit var danmuHelper: DanMuHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_danmu)

        danmuHelper = DanMuHelper(this, danmuContainerView)

        addDanmuBtn.setOnClickListener {
            danmuHelper.addDanmu(
                DanmuEntity(
                    arrayOf(
                        "https://q.qlogo.cn/qqapp/100229475/E573B01150734A02F25D8E9C76AFD138/100",
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602513751308&di=4750ba99977f4478144d0790b6857069&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F00%2F73%2F65%2F0656df6b43bacf5.jpg",
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602481010486&di=1eea4306e070ce336e0c28f82480613a&imgtype=0&src=http%3A%2F%2Fp.geitu.net%2F409%2F5P-jvh.jpg%3Fx-oss-process%3Dimage%2Fresize%2Cw_256%2Climit_1",
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602481030402&di=a510961dbd68fe447679c54c29e677b2&imgtype=0&src=http%3A%2F%2Favatar.gxnews.com.cn%2Favatar%2F000%2F08%2F33%2F51_avatar_big.jpg",
                        "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=781934719,1232780622&fm=15&gp=0.jpg",
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602481030258&di=b485992c8565f1123baa9e98889a867e&imgtype=0&src=http%3A%2F%2Fbbsimg.0411cxd.com%2Fforum%2F201105%2F03%2F203852cp0q2mzlq92mps43.png"
                    ).random(),
                    arrayOf("张三", "李四", "王五", "老板boss", "至尊大使", "尊贵皇希特").random(),
                    "0719",
                    Random.nextInt(30),
                    1,
                    arrayOf(
                        "欲穷千里目, 更上一层楼~~~",
                        "666666666666666666666666666666666666666666666666666666666666666666666666666666666666",
                        "sb fgsb fgsb fgsb fgsb fgsb fgsb",
                        "nbbbbbbbbbbbbility",
                        "fgsbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
                        "fgnbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
                        "操作犀利, 姿势帅气, 震惊世界",
                        "我上单, 带你赢, 小菜鸡, 搞快点",
                        "天王盖地虎, 小鸡炖蘑菇"
                    ).random()
                )
            )
        }

        hideDanmuBtn.setOnClickListener {
            danmuHelper.hideAllDanmu()
        }
    }
}