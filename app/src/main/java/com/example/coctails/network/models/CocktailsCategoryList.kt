package com.example.coctails.network.models

import com.google.gson.annotations.SerializedName

data class CocktailsCategoryList(
    @SerializedName("drinks")
     val drinks : List<Drink>
) {
    data class Drink(
        @SerializedName("strDrink")
        val strDrink : String,
        @SerializedName("strDrinkThumb")
        val strDrinkThumb : String,
        @SerializedName("idDrink")
        val idDrink : String
    )
}



