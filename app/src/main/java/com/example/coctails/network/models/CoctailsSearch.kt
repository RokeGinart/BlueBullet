package com.example.coctails.network.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class CocktailsSearch(
    @SerializedName("drinks")
    var drinks: List<Drink?>? = null
)

data class Drink(
    @SerializedName("idDrink")
    var idDrink: String? = null,
    @SerializedName("strDrink")
    var strDrink: String? = null,
    @SerializedName("strDrinkAlternate")
    var strDrinkAlternate: Any? = null,
    @SerializedName("strDrinkES")
    @Expose
    var strDrinkES: Any? = null,
    @SerializedName("strDrinkDE")
    var strDrinkDE: Any? = null,
    @SerializedName("strDrinkFR")
    var strDrinkFR: Any? = null,
    @SerializedName("strDrinkZH-HANS")
    var strDrinkZHHANS: Any? = null,
    @SerializedName("strDrinkZH-HANT")
    var strDrinkZHHANT: Any? = null,
    @SerializedName("strTags")
    var strTags: Any? = null,
    @SerializedName("strVideo")
    var strVideo: Any? = null,
    @SerializedName("strCategory")
    var strCategory: String? = null,
    @SerializedName("strIBA")
    var strIBA: Any? = null,
    @SerializedName("strAlcoholic")
    var strAlcoholic: String? = null,
    @SerializedName("strGlass")
    var strGlass: String? = null,
    @SerializedName("strInstructions")
    var strInstructions: String? = null,
    @SerializedName("strInstructionsES")
    var strInstructionsES: Any? = null,
    @SerializedName("strInstructionsDE")
    var strInstructionsDE: String? = null,
    @SerializedName("strInstructionsFR")
    var strInstructionsFR: Any? = null,
    @SerializedName("strInstructionsZH-HANS")
    var strInstructionsZHHANS: Any? = null,
    @SerializedName("strInstructionsZH-HANT")
    var strInstructionsZHHANT: Any? = null,
    @SerializedName("strDrinkThumb")
    var strDrinkThumb: String? = null,
    @SerializedName("strIngredient1")
    var strIngredient1: String? = null,
    @SerializedName("strIngredient2")
    var strIngredient2: String? = null,
    @SerializedName("strIngredient3")
    var strIngredient3: String? = null,
    @SerializedName("strIngredient4")
    var strIngredient4: String? = null,
    @SerializedName("strIngredient5")
    var strIngredient5: Any? = null,
    @SerializedName("strIngredient6")
    var strIngredient6: Any? = null,
    @SerializedName("strIngredient7")
    var strIngredient7: Any? = null,
    @SerializedName("strIngredient8")
    var strIngredient8: Any? = null,
    @SerializedName("strIngredient9")
    var strIngredient9: Any? = null,
    @SerializedName("strIngredient10")
    var strIngredient10: Any? = null,
    @SerializedName("strIngredient11")
    var strIngredient11: Any? = null,
    @SerializedName("strIngredient12")
    var strIngredient12: Any? = null,
    @SerializedName("strIngredient13")
    var strIngredient13: Any? = null,
    @SerializedName("strIngredient14")
    var strIngredient14: Any? = null,
    @SerializedName("strIngredient15")
    var strIngredient15: Any? = null,
    @SerializedName("strMeasure1")
    var strMeasure1: String? = null,
    @SerializedName("strMeasure2")
    var strMeasure2: String? = null,
    @SerializedName("strMeasure3")
    var strMeasure3: String? = null,
    @SerializedName("strMeasure4")
    var strMeasure4: String? = null,
    @SerializedName("strMeasure5")
    var strMeasure5: Any? = null,
    @SerializedName("strMeasure6")
    var strMeasure6: Any? = null,
    @SerializedName("strMeasure7")
    var strMeasure7: Any? = null,
    @SerializedName("strMeasure8")
    var strMeasure8: Any? = null,
    @SerializedName("strMeasure9")
    var strMeasure9: Any? = null,
    @SerializedName("strMeasure10")
    var strMeasure10: Any? = null,
    @SerializedName("strMeasure11")
    var strMeasure11: Any? = null,
    @SerializedName("strMeasure12")
    var strMeasure12: Any? = null,
    @SerializedName("strMeasure13")
    var strMeasure13: Any? = null,
    @SerializedName("strMeasure14")
    var strMeasure14: Any? = null,
    @SerializedName("strMeasure15")
    var strMeasure15: Any? = null,
    @SerializedName("strCreativeCommonsConfirmed")
    var strCreativeCommonsConfirmed: String? = null,
    @SerializedName("dateModified")
    var dateModified: String? = null
)
