package com.shinkevich.mailclientkotlin.model.mappers

public fun<S, R> listMapper(sourceList : List<S>, mapper: (S) -> R) : List<R>{
    val resultList = mutableListOf<R>()
    for(sourceObj in sourceList){
        resultList.add(mapper(sourceObj))
    }
    return resultList
}