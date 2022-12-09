package com.example.photoediting.data

import android.graphics.drawable.Icon
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.photoediting.data.offline.entity.IconEntity
import com.example.photoediting.data.offline.room.IconsDao
import com.example.photoediting.utils.AppExecutor

class IconRepository constructor(
    private val iconsDao: IconsDao,
//    private val appExecutor: AppExecutor
) {
    private val result = MediatorLiveData<Result<List<IconEntity>>>()

    val readAllIcons: LiveData<List<IconEntity>> = iconsDao.getIcons()

    suspend fun addIcons(icon: IconEntity) {
        iconsDao.insertIcon(icon)
    }

    fun deleteIcons() {
        iconsDao.deleteAll()
    }
}