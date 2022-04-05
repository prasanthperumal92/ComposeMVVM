package com.prasanth.tiktokexoplayercompose.usecase.model

import java.lang.Exception

class Entity<T:Any> {
    open class STATUS
    object DATA_AVAILABLE: STATUS()
    object FAILED:STATUS()
    object LOADING:STATUS()
    var status:STATUS = LOADING
    var response:T? = null
    lateinit var exception: Exception


    companion object{
        fun <T : Any> success(response: T):Entity<T>{
            val entity = Entity<T>()
            entity.status=DATA_AVAILABLE
            entity.response= response
            return entity
        }

        fun <T : Any> success(response: List<T>):Entity<List<T>>{
            val entity = Entity<List<T>>()
            entity.status=DATA_AVAILABLE
            entity.response= response
            return entity
        }



        fun  <T:Any> failure(exception:Exception):Entity<T>{
            val entity = Entity<T>()
            entity.status=FAILED
            entity.exception= exception
            return entity
        }
    }

}