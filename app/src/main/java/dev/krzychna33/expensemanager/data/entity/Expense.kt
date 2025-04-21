package dev.krzychna33.expensemanager.data.entity

data class Expense (
    val id: String,
    val name: String,
    val amount: Double,
    val date: String,
    val category: String,
    val description: String? = null
)

