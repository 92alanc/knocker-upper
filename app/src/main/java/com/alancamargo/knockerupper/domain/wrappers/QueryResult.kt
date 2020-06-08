package com.alancamargo.knockerupper.domain.wrappers

sealed class QueryResult<out T> {

    object Error : QueryResult<Nothing>()

    data class Success<T>(val body: T) : QueryResult<T>()

}