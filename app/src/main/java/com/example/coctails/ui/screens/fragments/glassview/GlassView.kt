package com.example.coctails.ui.screens.fragments.glassview

import com.example.coctails.network.models.firebase.drink.GlassDetails
import com.example.coctails.ui.BaseView

interface GlassView : BaseView {
    fun showGlass(glass : GlassDetails?)
}
