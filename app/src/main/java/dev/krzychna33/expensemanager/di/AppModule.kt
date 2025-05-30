package dev.krzychna33.news2.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.krzychna33.expensemanager.data.datasource.ExpensesDataSource
import dev.krzychna33.expensemanager.data.datasource.ExpensesDataSourceImpl
import dev.krzychna33.expensemanager.ui.repository.ExpensesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun provideExpensesDataSource(firestore: FirebaseFirestore): ExpensesDataSource {
        return ExpensesDataSourceImpl(firestore)
    }

    @Singleton
    @Provides
    fun providesExpensesRepository(expensesDataSource: ExpensesDataSource): ExpensesRepository {
        return ExpensesRepository(expensesDataSource)
    }

}