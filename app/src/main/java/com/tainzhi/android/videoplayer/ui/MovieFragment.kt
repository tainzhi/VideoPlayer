package com.tainzhi.android.videoplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tainzhi.android.common.util.startKtxActivity
import com.tainzhi.android.videoplayer.R
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ffmpegPlayerHlv.setOnClickListener {
            PlayFFmepgPlayerActivity.startPlay(requireContext(), PlayFFmepgPlayerActivity.PlayHlv)
        }
        ffmpegPlayerRtmp.setOnClickListener {
            PlayFFmepgPlayerActivity.startPlay(requireContext(), PlayFFmepgPlayerActivity.PlayRtmp)
        }
        danmuBtn.setOnClickListener {
            startKtxActivity<DanmuActivity>()
        }
    }
}