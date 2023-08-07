package com.example.arplusplu_task.ui.util
//this class helps us to differentiate between successfull and error responses
sealed class Resources<T>(
    val data:T ?= null,
     val message:String ?= null
    ) {
    class Success<T>(data: T) :Resources<T>(data)
    class Error<T>(message:String,data: T?=null) :Resources<T>(data,message)
    class Loading<T> :Resources<T>()



}