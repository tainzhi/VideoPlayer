package com.tainzhi.android.videoplayer.ui.like

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.base.ui.BaseViewModel
import com.tainzhi.android.videoplayer.repository.PreferenceRepository

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/9 10:09
 * @description:
 **/

class LikeViewModel(private val preferenceRepository: PreferenceRepository) : BaseViewModel() {

    private val _advertising = MutableLiveData<Boolean>()
    val advertising: LiveData<Boolean>
        get() = _advertising

    // // private val _playerType = MutableLiveData<Int>()
    // // val playType: LiveData<Int>
    // //     get() = _playerType
    // // val availablePlayTypes: LiveData<Map<Int, String>> = liveData {
    // //     emit(mapOf(
    // //             Constant.PlayerType.SYSTEM_PLAYER to "System Player",
    // //             Constant.PlayerType.IJK_PLAYER to "IjkPlayer",
    // //             Constant.PlayerType.EXO_PLAYER to "ExoPlayer"
    // //     ))
    // // }
    //
    //
    // private val _playRenderType = MutableLiveData<Int>()
    // val playRenderType: LiveData<Int>
    //     get() = _playRenderType
    // val availablePlayerRenderType: LiveData<List<Int>> = liveData {
    //     emit(listOf(
    //             Constant.RenderType.SURFACE_VIEW,
    //             Constant.RenderType.TEXTURE_VIEW,
    //             Constant.RenderType.GL_SURFACE_VIEW
    //     ))
    // }

    init {
        launch {
            _advertising.postValue(preferenceRepository.advertising)
        }
    }

    fun toggleAdvertising(checked: Boolean) {
        _advertising.postValue(checked)
        preferenceRepository.advertising = checked
    }

    // fun setPlayType(playType: Int) {
    //     launch {
    //         _playerType.postValue(playType)
    //     }
    // }
    //
    // fun setPlayerRenderType(playerRenderType: Int) {
    //     launch {
    //         _playRenderType.postValue(playerRenderType)
    //     }
    // }
}