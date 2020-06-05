package com.example.coctails.ui.screens.fragments.glassdetails

import com.example.coctails.network.models.firebase.drink.GlassDetails
import com.example.coctails.ui.BaseView

interface GlassView : BaseView {
    fun showGlass(glass : GlassDetails?, selected : Boolean)
    fun changesSuccess(id : Int, selected: Boolean)
}
