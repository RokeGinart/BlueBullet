package com.example.coctails.network.models.firebase.drink

import java.io.Serializable

data class Cocktails(
    var id: Int = 0,
    var name: String = "",
    var image: String = "",
    var category: Category? = null,
    var glass: Glass? = null,
    var abv: Int = 0,
    var cooktime: Int = 0,
    var instruction: String = "",
    var ingredients: List<Ingredients>? = null
) : Serializable {

    data class Ingredients(
        var id: Int = 0,
        var name: String = "",
        var category: String = ""
    ) : Serializable


    data class Category(
        var category: String = "",
        var name: String = ""
    ) : Serializable

    data class Glass(
        var id: Int = 0,
        var name: String = ""
    ) : Serializable
}
