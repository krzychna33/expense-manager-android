package dev.krzychna33.expensemanager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.krzychna33.expensemanager.data.entity.Expense

@Composable
fun ExpenseItem(expense: Expense, index: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "${index + 1}. ${expense.name}",
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Amount: $${expense.amount}",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Category: ${expense.category}",
            style = androidx.compose.material3.MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}