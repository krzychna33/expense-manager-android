package dev.krzychna33.news2.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.krzychna33.expensemanager.data.datasource.AuthDataSource
import dev.krzychna33.expensemanager.data.datasource.AuthDataSourceImpl
import dev.krzychna33.expensemanager.data.datasource.ExpensesDataSource
import dev.krzychna33.expensemanager.data.datasource.ExpensesDataSourceImpl
import dev.krzychna33.expensemanager.data.local.TokenManager
import dev.krzychna33.expensemanager.data.local.TokenManagerImpl
import dev.krzychna33.expensemanager.ui.repository.ExpensesRepository
import dev.krzychna33.expensemanager.ui.repository.AuthRepository
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


    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun providesAuthDataSource(auth: FirebaseAuth): AuthDataSource {
        return AuthDataSourceImpl(auth)
    }

    @Provides
    @Singleton
    fun providesTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManagerImpl(context)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(
        authDataSource: AuthDataSource,
        firebaseAuth: FirebaseAuth
    ): AuthRepository {
        return AuthRepository(authDataSource, firebaseAuth)
    }
}