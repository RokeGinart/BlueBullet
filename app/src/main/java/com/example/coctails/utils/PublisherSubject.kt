package com.example.coctails.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.Serializable

class PublisherSubject  : Serializable{

    private var mInstance: PublisherSubject? = null
    fun getInstance(): PublisherSubject? {
        if (mInstance == null) {
            mInstance = PublisherSubject()
        }
        return mInstance
    }

    private val publisher = PublishSubject.create<String>()
    fun publish(event: String) {
        publisher.onNext(event)
    }

    fun listen(): Observable<String> {
        return publisher
    }
}