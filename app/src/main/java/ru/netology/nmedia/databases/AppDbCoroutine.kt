package ru.netology.nmedia.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nmedia.dao.PostDaoCoroutine
import ru.netology.nmedia.dao.PostEntity


@Database(entities = [PostEntity::class], version = 1)
abstract class AppDbCoroutine : RoomDatabase() {
    abstract fun postDao(): PostDaoCoroutine

    companion object {
        @Volatile
        private var instance: AppDbCoroutine? = null

        fun getInstance(context: Context): AppDbCoroutine {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDbCoroutine::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
