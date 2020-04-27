package com.example.coctails.network.models.firebase.drink

import java.io.Serializable

data class Cocktails(
    var id: Int = 0,
    var name: String = "",
    var image: String = "",
    var category: Category? = null,
    var glass: String = "",
    var iba: String = "",
    var cooktime: String = "",
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
}
