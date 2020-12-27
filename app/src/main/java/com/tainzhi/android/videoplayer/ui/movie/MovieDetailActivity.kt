package com.tainzhi.android.videoplayer.ui.movie

import android.content.Context
import android.content.Intent
import androidx.core.view.isVisible
import com.tainzhi.android.common.base.ui.BaseVmBindingActivity
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.MovieDetailActivityBinding
import com.tainzhi.android.videoplayer.widget.dialog.ChooseMovieEpisodeDialog
import com.tainzhi.mediaspider.film.bean.DetailData
import com.tainzhi.mediaspider.film.bean.Video
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * File:     MovieDetailActivity
 * Author:   tainzhi
 * Created:  2020/12/21 10:52
 * Mail:     QFQ61@qq.com
 * Description:
 */
class MovieDetailActivity : BaseVmBindingActivity<MovieViewModel, MovieDetailActivityBinding>() {
    companion object {
        // private const val MOVIE_SOURCE = "movie_source"
        private const val MOVIE_ID = "movie_id"
        fun start(startContext: Context, movieId: String) {
            startContext.startActivity(
                    Intent(startContext, MovieDetailActivity::class.java).apply {
                        // putExtra(MOVIE_SOURCE, movieSource)
                        putExtra(MOVIE_ID, movieId)
                    }
            )
        }
    }

    override fun initVM(): MovieViewModel = getViewModel()

    override fun initData() {
        // val movieSource = intent.getStringExtra(MOVIE_SOURCE)
        val movieId = intent.getStringExtra(MOVIE_ID)
        // if (movieSource != null && movieId != null) {
        if (movieId != null) {
            mViewModel.getMovieDetail(movieId as String)
        }
    }

    override fun startObserve() {
        mViewModel.movie.observe(this) { detailData ->
            mBinding.movie = detailData
            if (!detailData.videoList.isNullOrEmpty()) {
                if (detailData.videoList!!.size > 1) {
                    with(mBinding) {
                        moreEpisodeIv.isVisible = true
                        episodeNameTv.setOnClickListener {
                            ChooseMovieEpisodeDialog(detailData.videoList!!).apply {
                                episodeCallback = { episode ->
                                    startPlay(detailData, episode)
                                }
                            }.show(supportFragmentManager, "Choose Movie Episode")
                        }
                    }
                } else {
                    startPlay(detailData!!)
                }
            }
        }
    }

    private fun startPlay(movie: DetailData, episode: Video? = null) {
        val showName = if (episode == null) movie.type else episode.name + " : " + movie.type
        val playingEpisodeName = movie.name + if (episode == null) "" else " : " + episode.name
        mBinding.episodeNameTv.text = playingEpisodeName
        // TODO: 2020/12/24
        // 以下是播放url
        // episode.playUrl
    }

    override fun getLayoutResId(): Int {
        return R.layout.movie_detail_activity
    }
}