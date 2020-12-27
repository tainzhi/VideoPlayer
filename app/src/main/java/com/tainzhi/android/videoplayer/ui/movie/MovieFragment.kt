package com.tainzhi.android.videoplayer.ui.movie

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.tainzhi.android.common.base.ui.LazyLoad
import com.tainzhi.android.common.base.ui.fragment.BaseVmBindingFragment
import com.tainzhi.android.common.util.startKtxActivity
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.MovieFragmentBinding
import com.tainzhi.android.videoplayer.widget.dialog.ChooseMovieSourceDialog
import com.tainzhi.mediaspider.film.bean.Classify
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MovieFragment : BaseVmBindingFragment<MovieViewModel, MovieFragmentBinding>(), LazyLoad {

    private val classifyList = arrayListOf<Classify>()

    override fun getLayoutResId(): Int {
        return R.layout.movie_fragment
    }

    // 在本地视频, 斗鱼, 电影, tv fragment之间切换,
    // 该fragment生命周期为: onViewCreated() -> initData() -> onStop() -> onDestory()
    // 防止重复加载: 之前请求的 MovieChannelFragment请求数据, 而此时又再次请求新的数据 mViewModel.getHomeData()再重新创建新的 MovieChannelFragment请求数据
    // 造成多次请求的情况
    // private var isLoaded = false

    override fun initData() {
        setHasOptionsMenu(true)
        // if (!isLoaded) {
        //     isLoaded = true
        //     mViewModel.getHomeData()
        // }
        mViewModel.getHomeData()
    }

    override fun initVM(): MovieViewModel = getViewModel()

    override fun startObserve() {
        mViewModel.classifyListLiveData.observe(viewLifecycleOwner) {
            classifyList.clear()
            classifyList.add(Classify("new", "最新"))
            classifyList.addAll(it)
            with(mBinding) {
                movieViewPager2.run {
                    offscreenPageLimit = 10
                    adapter = object : FragmentStateAdapter(this@MovieFragment) {
                        override fun createFragment(position: Int): Fragment {
                            return MovieChannelFragment.newInstance(classifyList[position].id)
                        }

                        override fun getItemCount() = classifyList.size
                    }
                }
                TabLayoutMediator(movieTabLayout, movieViewPager2) { tab, position ->
                    tab.text = classifyList[position].name
                }.attach()

                changeMovieSiteFab.setOnClickListener {
                    childFragmentManager.let { manager ->
                        ChooseMovieSourceDialog().show(manager, "ChooseMovieSourceDialog")
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movie_search, menu)
        menu.findItem(R.id.search)?.apply {
            isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        openMovieSearch()
        return super.onOptionsItemSelected(item)
    }

    /**
     * 打开搜索页面
     */
    private fun openMovieSearch() {
        startKtxActivity<MovieSearchActivity>()
    }
}