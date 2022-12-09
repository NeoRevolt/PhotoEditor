package com.example.photoediting.data.offline.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.photoediting.data.offline.entity.IconEntity

@Database(entities = [IconEntity::class], version = 1, exportSchema = false)

abstract class IconsDatabase : RoomDatabase() {
    abstract fun iconsDao(): IconsDao

    companion object {
        @Volatile
        private var instance: IconsDatabase? = null

        fun getInstance(context: Context): IconsDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    IconsDatabase::class.java, "Icons.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
    }
}