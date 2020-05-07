package com.example.coctails.ui.screens.fragments.glassdetails

import com.example.coctails.ui.BasePresenter

abstract class GlassPresenter : BasePresenter<GlassView>() {
    abstract fun getGlass(glassId : Int)
}