package com.example.expense.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expense.data.dao.Converters
import com.example.expense.data.dao.ExpenseDao
import com.example.expense.data.dao.UserDao
import com.example.expense.data.model.Expense

@Database(entities = [Expense::class], version =  1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun expenseDao(): ExpenseDao

    companion object{

        @Volatile
        private var INSTANCE: AppDatabase? =null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}