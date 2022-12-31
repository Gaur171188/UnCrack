package com.geekymusketeers.uncrack.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.geekymusketeers.uncrack.data.AccountDao

abstract class AccountDatabase : RoomDatabase(){

    abstract val accountDao : AccountDao

    companion object{
        @Volatile
        var INSTANCE : AccountDatabase ? = null

        @Synchronized
        fun getDatabase(context:Context)  : AccountDatabase {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    AccountDatabase::class.java,
                    "account_database"
                ).fallbackToDestructiveMigration().build()
            }
            return INSTANCE!!
        }
    }
}