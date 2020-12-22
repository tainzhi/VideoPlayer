package com.tainzhi.android.videoplayer.ui.movie

import android.content.Context
import android.content.Intent
import com.tainzhi.android.common.base.ui.BaseVmBindingActivity
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.MovieDetailActivityBinding
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


    override fun initView() {
    }

    override fun initData() {
        // val movieSource = intent.getStringExtra(MOVIE_SOURCE)
        val movieId = intent.getStringArrayExtra(MOVIE_ID)
        // if (movieSource != null && movieId != null) {
        if (movieId != null) {
            mViewModel.getMovieDetail(movieId as String)
        }
    }

    override fun startObserve() {
        mViewModel.movie.observe(this) {

        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.movie_detail_activity
    }
}