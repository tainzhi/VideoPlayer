package com.tainzhi.android.videoplayer.repository

import com.orhanobut.logger.Logger
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.bean.TvProgram
import com.tainzhi.android.videoplayer.db.TvDao
import com.tainzhi.android.videoplayer.network.NetworkBoundRepository
import com.tainzhi.android.videoplayer.network.RateLimiter
import com.tainzhi.android.videoplayer.network.Result
import com.tainzhi.android.videoplayer.spider.TVSpider
import com.tainzhi.android.videoplayer.spider.TvProgramBean
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit.MINUTES

/**
 * File:     TVRepository
 * Author:   tainzhi
 * Created:  2020/12/9 16:48
 * Mail:     QFQ61@qq.com
 * Description:
 */
class TVRepository(
        val tvDao: TvDao
) {

    // 1min更新一次
    val rateLimiter = RateLimiter<String>(1, timeUnit = MINUTES)

    companion object {
        const val TAG = "TVRepository.getTvPrograms"
    }

    fun loadTVs(): Result<List<Tv>> {
        return try {
            val tvs = tvDao.getAllTv()
            tvs.forEach { tv ->
                tv.tvCircuit = loadTVSource(tv.id)
            }
            Result.success(tvs)
        } catch (e: Exception) {
            Logger.e(e.toString())
            Result.error(e.toString())
        }
    }

    /**
     * 查询卫视tvId的所有直播源
     *
     * @program tvId 卫视tvId
     */
    private fun loadTVSource(tvId: String): List<String> {
        return tvDao.getTvCircuit(tvId)
    }

    fun getTvPrograms(): Flow<Result<Map<String, TvProgramBean>>> {
        return object : NetworkBoundRepository<Map<String, TvProgramBean>, Map<String, TvProgramBean>>() {
            override fun shouldFetch(): Boolean = rateLimiter.shouldFetch(TAG)

            override suspend fun saveRemoteData(response: Map<String, TvProgramBean>) {
                val tvProgramList = mutableListOf<TvProgram>()
                response.forEach { (_, u) ->
                    tvProgramList.add(TvProgram(u.tvId, u.liveProgram, u.liveProgramTime, u.nextProgram, u.nextProgramTime))
                }
                tvDao.insertAllTvPrograms(tvProgramList)
            }

            override fun fetchFromLocal(): Map<String, TvProgramBean> {
                val tvProgramBeanMap = hashMapOf<String, TvProgramBean>()
                val programs: List<TvProgram> = tvDao.getAllTvPrograms()
                programs.forEach { program ->
                    tvProgramBeanMap[program.tvId] =
                            TvProgramBean(program.tvId, program.liveProgram, program.liveProgramTime, program.nextProgram, program.nextProgramTime)
                }
                return tvProgramBeanMap

            }

            override suspend fun fetchFromRemote(): Map<String, TvProgramBean> {
                return coroutineScope {
                    val cctvJob = async {
                        TVSpider.getInstance().getTvProgram("cctv")
                    }
                    val stateJob = async {
                        TVSpider.getInstance().getTvProgram("satellite")
                    }
                    cctvJob.await() + stateJob.await()
                }
            }
        }.asFlow()
    }
}