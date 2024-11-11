package com.tainzhi.android.videoplayer.ui.movie

import android.content.Context
import android.content.Intent
import androidx.core.view.isVisible
import com.tainzhi.android.videoplayer.base.ui.fragment.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.MovieDetailFragmentBinding
import com.tainzhi.android.videoplayer.widget.dialog.ChooseMovieEpisodeDialog
import com.tainzhi.mediaspider.movie.bean.DetailData
import com.tainzhi.mediaspider.movie.bean.Episode
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * File:     MovieDetailFragment
 * Author:   tainzhi
 * Created:  2020/12/21 10:52
 * Mail:     QFQ61@qq.com
 * Description:
 */
class MovieDetailFragment : BaseVmBindingFragment<MovieViewModel, MovieDetailFragmentBinding>() {
    companion object {
        // private const val MOVIE_SOURCE = "movie_source"
        const val MOVIE_ID = "movie_id"
        fun start(startContext: Context, movieId: String) {
            startContext.startActivity(
                    Intent(startContext, MovieDetailFragment::class.java).apply {
                        // putExtra(MOVIE_SOURCE, movieSource)
                        putExtra(MOVIE_ID, movieId)
                    }
            )
        }
    }

    override fun initVM(): MovieViewModel = getViewModel()

    override fun initData() {
        // val movieSource = intent.getStringExtra(MOVIE_SOURCE)
        val movieId = arguments?.getString(MOVIE_ID)
        // if (movieSource != null && movieId != null) {
        if (movieId != null) {
            mViewModel.getMovieDetail(movieId)
        }
    }

    override fun startObserve() {
        mViewModel.movie.observe(this) { detailData ->
            mBinding.movie = detailData
            if (!detailData.episodeList.isNullOrEmpty()) {
                if (detailData.episodeList!!.size > 1) {
                    with(mBinding) {
                        moreEpisodeIv.isVisible = true
                        episodeNameTv.setOnClickListener {
                            ChooseMovieEpisodeDialog(detailData.episodeList!!).apply {
                                episodeCallback = { episode ->
                                    startPlay(detailData, episode)
                                }
                            }.show(childFragmentManager, "Choose Movie Episode")
                        }
                    }
                } else {
                    startPlay(detailData!!)
                }
            }
        }
    }

    private fun startPlay(movie: DetailData, episode: Episode? = null) {
        val showName = if (episode == null) movie.type else episode.name + " : " + movie.type
        val playingEpisodeName = movie.name + if (episode == null) "" else " : " + episode.name
        mBinding.episodeNameTv.text = playingEpisodeName
        // TODO: 2020/12/24
        // 以下是播放url
        // episode.playUrl
    }

    override fun getLayoutResId(): Int {
        return R.layout.movie_detail_fragment
    }
}