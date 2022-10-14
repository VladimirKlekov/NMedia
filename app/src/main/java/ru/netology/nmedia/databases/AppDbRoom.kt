package ru.netology.nmedia.databases


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nmedia.dao.PostEntity
import ru.netology.nmedia.interfaces.PostDaoRoom

@Database(entities = [PostEntity::class], version = 1)
abstract class AppDbRoom : RoomDatabase() {
    abstract fun postDao(): PostDaoRoom

    companion object {
        @Volatile
        private var instance: AppDbRoom? = null

        fun getInstance(context: Context): AppDbRoom {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDbRoom::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }
}