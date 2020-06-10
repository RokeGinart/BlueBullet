package com.example.coctails.ui.screens.fragments.glassdetails

import com.example.coctails.core.room.entity.glass_data.GlassFirebaseData
import com.example.coctails.ui.BaseView

interface GlassView : BaseView {
    fun showGlass(glass : GlassFirebaseData?, selected : Boolean)
    fun changesSuccess(id : Int, selected: Boolean)
}
