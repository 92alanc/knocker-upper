package com.alancamargo.knockerupper.domain.model

sealed class QueryResult<out T> {

    object Error : QueryResult<Nothing>()

    data class Success<T>(val body: T) : QueryResult<T>()

}