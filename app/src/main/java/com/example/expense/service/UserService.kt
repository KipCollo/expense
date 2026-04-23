package com.example.expense.service

import com.example.expense.data.dao.UserDao
import com.example.expense.data.model.User
import kotlinx.coroutines.flow.Flow

class UserService(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun register(user: User) {
        userDao.register(user)
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    suspend fun getById(id: Int): User? {
        return userDao.getUserById(id)
    }

    suspend fun getByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
    }
}
