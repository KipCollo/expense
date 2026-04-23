package com.example.expense.repository

import com.example.expense.data.dao.UserDao
import com.example.expense.data.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val allusers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun insert(user: User){
        userDao.insert(user)
    }

    suspend fun getById(id: Int): User?{
        return userDao.getUserById(id)
    }

    suspend fun login(email: String, password: String): User?{
        return userDao.login(email,password)
    }

    suspend fun delete(user: User){
        userDao.delete(user)
    }
}