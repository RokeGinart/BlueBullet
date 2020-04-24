package com.example.coctails.network.models.firebase.drink

data class Coctails(
    var id : String,
    var name: String,
    var image: String,
    var category: String,
    var glass: String,
    var iba: String,
    var instruction: String,
    var ingredients : List<Ingredients>
)

data class Ingredients(
    var id: String,
    var name: String,
    var image: String
)
