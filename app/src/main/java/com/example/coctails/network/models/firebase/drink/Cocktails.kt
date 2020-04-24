package com.example.coctails.network.models.firebase.drink

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
) {

    data class Ingredients(
        var id: Int = 0,
        var name: String = ""
    )

    data class Category(
        var category: String = "",
        var name: String = ""
    )
}
