package dev.krzychna33.expensemanager.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.krzychna33.expensemanager.data.entity.Expense

@Composable
fun TotalExpensesSummary(expenses: List<Expense>) {
    val totalAmount = expenses.sumOf { it.amount }
    Text(
        text = "Total Expenses: $${"%.2f".format(totalAmount)}",
        style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 16.dp)
    )
}