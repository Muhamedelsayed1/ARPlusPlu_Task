package com.example.arplusplu_task.ui.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.arplusplu_task.ui.models.Article

@Database(
    entities = [Article::class],
    version = 2,
     exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ArticleDataBase : RoomDatabase() {
    abstract fun getArticleDao():ArticleDao

    //to make an instance for or database
    companion object{
        @Volatile // volatile to allow other threads to see when thread change the instance
        private var instance:ArticleDataBase? = null
        private val LOCK = Any()
        // to ensure that we have only one instance of our database
        operator fun invoke (context: Context)= instance ?:synchronized(LOCK){
            //any block of code we write here can not be accessed by anther thread
            instance?:createDatabase(context).also { instance=it }
        }

        private fun createDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            ArticleDataBase::class.java,
            "article_db.db"
        ).build()
    }
}