package com.hello.ebookreader.data.repo

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.hello.ebookreader.common.BookCategoryModel
import com.hello.ebookreader.common.BookModel
import com.hello.ebookreader.common.ResultState
import com.hello.ebookreader.domain.repository.AllBookRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AllBookRepoImpl @Inject constructor(val firebaseDatabase: FirebaseDatabase) : AllBookRepo {
    override suspend fun getAllBooks(): Flow<ResultState<List<BookModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var items: List<BookModel> = snapshot.children.mapNotNull { value ->
                    value.getValue(BookModel::class.java)
                }

                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.toException()))
            }
        }
        firebaseDatabase.reference.child("Books").addValueEventListener(valueEvent)
        awaitClose {
            firebaseDatabase.reference.removeEventListener(valueEvent)
            close()
        }
    }

    override suspend fun getAllCategory(): Flow<ResultState<List<BookCategoryModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var items: List<BookCategoryModel> = snapshot.children.mapNotNull { value ->
                    value.getValue(BookCategoryModel::class.java)
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.toException()))
            }
        }
        firebaseDatabase.reference.child("BookCategory").addValueEventListener(valueEvent)
        awaitClose {
            firebaseDatabase.reference.removeEventListener(valueEvent)
            close()
        }
    }

    override suspend fun getAllBooksByCategory(category: String): Flow<ResultState<List<BookModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var items: List<BookModel> = emptyList()
                    items = snapshot.children.filter {
                    it.getValue<BookModel>()!!.category == category
                }.map { value ->
                    value.getValue<BookModel>()!!
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.toException()))
            }
        }
        firebaseDatabase.reference.child("Books").addValueEventListener(valueEvent)
        awaitClose {
            firebaseDatabase.reference.removeEventListener(valueEvent)
            close()
        }
    }



    override suspend fun addBook(bookUrl: String, bookName: String, category: String): Flow<ResultState<Unit>> = callbackFlow {
        trySend(ResultState.Loading)

        val reference = firebaseDatabase.reference.child("Books").push()

        reference.setValue(BookModel(bookName = bookName, bookUrl = bookUrl, category = category)).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                trySend(ResultState.Success(Unit))
            } else {
                trySend(ResultState.Error(task.exception ?: Exception("Unknown error occurred")))
            }
            close()
        }

        awaitClose()
    }

}