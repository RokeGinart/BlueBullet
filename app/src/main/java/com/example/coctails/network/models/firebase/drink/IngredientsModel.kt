package com.example.coctails.network.models.firebase.drink

data class IngredientsModel(
    var id : Int = 0,
    var name: String = "",
    var image: String = "",
    var description: String = "",
    var country: String = "",
    var link: String = "",
    var abv: Int = 0,
    var category: Category? = null
){
    data class Category(
        var category: String = "",
        var name: String = ""
    )
}
