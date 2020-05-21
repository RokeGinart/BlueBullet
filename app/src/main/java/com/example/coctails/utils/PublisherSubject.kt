package com.example.coctails.utils

import com.example.coctails.ui.screens.fragments.cocktaildetails.model.IngredientModelCD
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.Serializable

class PublisherSubject  : Serializable {

    private var mInstance: PublisherSubject? = null
    fun getInstance(): PublisherSubject? {
        if (mInstance == null) {
            mInstance = PublisherSubject()
        }
        return mInstance
    }

    private val publisher = PublishSubject.create<String>()
    private val publisherIngredient = PublishSubject.create<IngredientModelCD>()

    fun publish(event: String) {
        publisher.onNext(event)
    }

    fun listen(): Observable<String> {
        return publisher
    }

    fun publishIngredient(ingredient: IngredientModelCD) {
        publisherIngredient.onNext(ingredient)
    }

    fun listenChange(): Observable<IngredientModelCD> {
        return publisherIngredient
    }
}