package com.example.coctails.utils

import com.example.coctails.ui.screens.fragments.cocktaildetails.model.IngredientModelCD
import com.example.coctails.ui.screens.fragments.favorites.model.FavoriteSubjectModel
import com.example.coctails.ui.screens.fragments.shopping.model.ItemChange
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

    fun publish(event: String) {
        publisher.onNext(event)
    }

    fun listen(): Observable<String> {
        return publisher
    }

    private val publisherIngredient = PublishSubject.create<IngredientModelCD>()

    fun publishIngredient(ingredient: IngredientModelCD) {
        publisherIngredient.onNext(ingredient)
    }


    fun listenChange(): Observable<IngredientModelCD> {
        return publisherIngredient
    }

    private val favoritePublisher = PublishSubject.create<FavoriteSubjectModel>()

    fun publishFavorite(favorite: FavoriteSubjectModel) {
        favoritePublisher.onNext(favorite)
    }

    fun listenFavoriteChange(): Observable<FavoriteSubjectModel> {
        return favoritePublisher
    }

    private val shoppingPublisher = PublishSubject.create<ItemChange>()

    fun publishItem(shopping: ItemChange) {
        shoppingPublisher.onNext(shopping)
    }

    fun listenShoppingChange(): Observable<ItemChange> {
        return shoppingPublisher
    }

}