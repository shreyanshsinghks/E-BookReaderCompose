package com.hello.ebookreader.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hello.ebookreader.common.BookCategoryModel
import com.hello.ebookreader.common.BookModel
import com.hello.ebookreader.common.ResultState
import com.hello.ebookreader.domain.repository.AllBookRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(val repo: AllBookRepo): ViewModel() {

    private val _state: MutableState<ItemsState> = mutableStateOf(ItemsState())
    val state: MutableState<ItemsState> = _state

    init {
        loadBooks()
        loadCategories()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            repo.getAllBooks().collect { result ->
                when (result) {
                    is ResultState.Error -> {
                        _state.value = _state.value.copy(error = result.exception.message.toString(), isLoading = false)
                    }
                    ResultState.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _state.value = _state.value.copy(items = result.data, isLoading = false)
                    }
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repo.getAllCategory().collect { result ->
                when (result) {
                    is ResultState.Error -> {
                        _state.value = _state.value.copy(error = result.exception.message.toString(), isLoading = false)
                    }
                    ResultState.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _state.value = _state.value.copy(category = result.data, isLoading = false)
                    }
                }
            }
        }
    }

    fun loadBooksByCategory(category: String) {
        viewModelScope.launch {
            repo.getAllBooksByCategory(category).collect { result ->
                when (result) {
                    is ResultState.Error -> {
                        _state.value = _state.value.copy(error = result.exception.message.toString(), isLoading = false)
                    }
                    ResultState.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _state.value = _state.value.copy(categoryItems = result.data, isLoading = false)
                    }
                }
            }
        }
    }


    fun addBook(bookUrl: String, bookName: String, category: String) {
        viewModelScope.launch {
            repo.addBook(bookUrl = bookUrl , bookName = bookName, category = category).collect { result ->
                when (result) {
                    is ResultState.Error -> {
                        _state.value = _state.value.copy(error = result.exception.message.toString(), isLoading = false)
                    }
                    ResultState.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
            }
        }
    }
}



data class ItemsState(
    val items: List<BookModel> = emptyList(),
    val category: List<BookCategoryModel> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false,
    val categoryItems: List<BookModel> = emptyList(),
)