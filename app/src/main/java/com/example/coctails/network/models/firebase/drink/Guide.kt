package com.example.coctails.network.models.firebase.drink

import java.io.Serializable

data class Guide(
    var id: Int = 0,
    var title: String = "",
    var source: Source? = null,
    var steps: List<Steps>? = null
) : Serializable {

    data class Steps(
        var image: String = "",
        var description: String = ""
    ) : Serializable

    data class Source(
        var name: String = "",
        var link: String = ""
    ) : Serializable
}