package com.example.coctails.ui.screens.fragments.glass

import com.example.coctails.network.models.firebase.drink.GlassDetails
import com.example.coctails.ui.BaseView

interface GlassWSView : BaseView {

    fun showGlassList(glassList: List<GlassDetails>)
}