package com.tainzhi.android.videoplayer.ui.movie

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.tainzhi.android.common.base.ui.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.MovieFragmentBinding
import com.tainzhi.android.videoplayer.util.openBrowser
import com.tainzhi.android.videoplayer.util.replyUrl
import com.tainzhi.android.videoplayer.widget.dialog.ChooseMovieSourceDialog
import com.tainzhi.mediaspider.film.bean.Classify
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MovieFragment : BaseVmBindingFragment<MovieViewModel, MovieFragmentBinding>() {

    private val classifyList = arrayListOf<Classify>()

    override fun getLayoutResId(): Int {
        return R.layout.movie_fragment
    }

    override fun initData() {
        setHasOptionsMenu(true)
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
                    offscreenPageLimit = 4
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
                    fragmentManager?.let { manager ->
                        ChooseMovieSourceDialog().show(manager, "ChooseMovieSourceDialog")
                    }
                }
            }
        }
    }

    private var searchMenu: MenuItem? = null
    private var searchView: SearchView? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        requireContext().openBrowser(replyUrl)
        return super.onOptionsItemSelected(item)
    }
}