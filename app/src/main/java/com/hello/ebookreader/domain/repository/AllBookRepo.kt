package com.hello.ebookreader.domain.repository

import com.hello.ebookreader.common.BookModel
import com.hello.ebookreader.common.ResultState
import kotlinx.coroutines.flow.Flow

// This is interface because we store get the full data in the data layer then filter it out in the domain layer then again access it in the data layer or view model. So the functions here will be interface only.
interface AllBookRepo {
    fun getAllBooks(): Flow<ResultState<List<BookModel>>>
}