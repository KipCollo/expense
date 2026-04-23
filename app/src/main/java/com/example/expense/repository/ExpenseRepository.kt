package com.example.expense.repository

import com.example.expense.data.dao.ExpenseDao
import com.example.expense.data.model.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()

    suspend fun insert(expense: Expense){
        expenseDao.insertExpense(expense)
    }

    suspend fun update(expense: Expense){
        expenseDao.updateExpense(expense)
    }

    fun searchExpensesByTitle(title: String): Flow<List<Expense>>{
        return expenseDao.searchByTitle(title)
    }

//    fun getExpenseById(id: Int): Expense? {
//        return expenseDao.getExpenseById(id)
//    }
}