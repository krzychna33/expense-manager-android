package dev.krzychna33.expensemanager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import dev.krzychna33.expensemanager.data.entity.Expense
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ExpenseItem(expense: Expense, index: Int, onRemove: (Expense) -> Unit) {
    // Format date to YYYY-MM-DD HH:MM
    val formattedDate = try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = parser.parse(expense.date)
        if (date != null) formatter.format(date) else expense.date
    } catch (e: Exception) {
        expense.date
    }
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(0.7f)
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
            Text(
                text = "Date: $formattedDate",
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Column(
            modifier = Modifier
                .weight(0.3f)
                .align(Alignment.CenterVertically),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(onClick = { onRemove(expense) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Remove Expense"
                )
            }
        }
    }
}

