package dev.krzychna33.expensemanager

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ExpenseManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Coming_inside_onCreate")
    }

    companion object {
        const val TAG = "NewsApplication"
    }
}