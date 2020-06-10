package com.example.coctails.ui.screens.fragments.glass

import com.example.coctails.core.room.entity.glass_data.GlassFirebaseData
import com.example.coctails.ui.BaseView

interface GlassWSView : BaseView {

    fun showGlassList(glassList: List<GlassFirebaseData>)
}