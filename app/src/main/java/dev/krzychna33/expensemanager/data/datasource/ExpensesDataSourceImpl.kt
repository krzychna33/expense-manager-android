package dev.krzychna33.expensemanager.data.datasource

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dev.krzychna33.expensemanager.data.entity.Expense
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

const val TAG = "ExpensesDataSourceImpl"

class ExpensesDataSourceImpl @Inject() constructor(private val firestore: FirebaseFirestore) :
    ExpensesDataSource {

    override suspend fun getExpenses(): List<Expense> = suspendCancellableCoroutine { cont ->
        firestore.collection("expenses")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                var expenses = emptyList<Expense>()

                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val currentDateTime = dateFormat.format(Date())

                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    expenses = expenses +
                            Expense(
                                id = document.id,
                                name = document.getString("name") ?: "",
                                amount = document.getDouble("amount") ?: 0.0,
                                date = document.getString("date") ?: currentDateTime,
                                category = document.getString("category") ?: "Default"
                            )
                }
                cont.resume(expenses, null)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                cont.resumeWithException(exception)
            }
    }

    override suspend fun addExpense(expense: Expense): String = suspendCancellableCoroutine { cont ->
        val expenseData = hashMapOf(
            "name" to expense.name,
            "amount" to expense.amount,
            "date" to expense.date,
            "category" to expense.category
        )

        firestore.collection("expenses")
            .add(expenseData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Expense added with ID: ${documentReference.id}")
                cont.resume(documentReference.id, null)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error adding expense", exception)
                cont.resumeWithException(exception)
            }
    }
}