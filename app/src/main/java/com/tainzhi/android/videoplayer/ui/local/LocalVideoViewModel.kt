package com.tainzhi.android.videoplayer.ui.local

import android.app.RecoverableSecurityException
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.common.base.ui.BaseViewModel
import com.tainzhi.android.videoplayer.bean.LocalVideo
import com.tainzhi.android.videoplayer.repository.LocalVideoRepository
import kotlinx.coroutines.withContext

class LocalVideoViewModel(private val localVideoRepository: LocalVideoRepository,
                          private val dispatcherProvider: CoroutinesDispatcherProvider
) : BaseViewModel() {
    private var pendingDeleteVideoUri: Uri? = null

    private val _permissionNeededForDelete = MutableLiveData<IntentSender?>()
    val permissionNeededForDelete: LiveData<IntentSender?> = _permissionNeededForDelete

    private val _localVideoList = MutableLiveData<List<LocalVideo>>()
    val localVideoList
        get() = _localVideoList

    fun getLocalVideos() {
        launch {
            val result = localVideoRepository.getLocalVideoList()
            emitData(result)
        }
    }

    fun deleteVideo(uri: Uri) {
        launch {
            withContext(dispatcherProvider.io) {
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
    }

    fun deletePendingVideo() {
        pendingDeleteVideoUri?.let { uri ->
            pendingDeleteVideoUri = null
            deleteVideo(uri)
        }
    }

    private suspend fun emitData(videos: List<LocalVideo>) {
        withContext(dispatcherProvider.main) {
            _localVideoList.value = videos
        }
    }
}