package com.tainzhi.android.videoplayer.ui.local

import android.app.RecoverableSecurityException
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.BaseViewModel
import com.tainzhi.android.common.CoroutineDispatcherProvider
import com.tainzhi.android.videoplayer.bean.LocalVideo
import com.tainzhi.android.videoplayer.network.doIfSuccess
import com.tainzhi.android.videoplayer.repository.LocalVideoRepository

class LocalVideoViewModel(
        private val localVideoRepository: LocalVideoRepository,
        coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : BaseViewModel(coroutineDispatcherProvider) {
    private var pendingDeleteVideoUri: Uri? = null

    private val _permissionNeededForDelete = MutableLiveData<IntentSender?>()
    val permissionNeededForDelete: LiveData<IntentSender?> = _permissionNeededForDelete

    private val _localVideoList: MutableLiveData<List<LocalVideo>> = MutableLiveData()
    val localVideoList
        get() = _localVideoList

    fun getLocalVideos() {
        launchIO {
            localVideoRepository
                    .getLocalVideoList()
                    .doIfSuccess { _localVideoList.postValue(it) }
        }
    }

    /**
     * 删除视频uri, 如果删除失败, 捕获了权限相同通知, 提示用户重新获取删除权限(android10+上)
     * @param uri 要删除的视频uri
     */
    fun deleteVideo(uri: Uri) {
        launchIO {
            try {
                localVideoRepository.deleteVideo(uri)
            } catch (securityException: SecurityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val recoverableSecurityException =
                            securityException as? RecoverableSecurityException
                                    ?: throw securityException

                    // Signal to the Activity that it needs to request permission and
                    // try the delete again if it succeeds.
                        pendingDeleteVideoUri = uri
                        _permissionNeededForDelete.postValue(
                                recoverableSecurityException.userAction.actionIntent.intentSender
                        )
                    } else {
                        throw securityException
                    }
                }
            }
        }

    fun deletePendingVideo() {
        pendingDeleteVideoUri?.let { uri ->
            pendingDeleteVideoUri = null
            deleteVideo(uri)
        }
    }
}