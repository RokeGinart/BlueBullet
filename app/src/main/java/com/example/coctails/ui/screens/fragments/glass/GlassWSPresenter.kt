package com.example.coctails.ui.screens.fragments.glass

import com.example.coctails.ui.BasePresenter

abstract class GlassWSPresenter : BasePresenter<GlassWSView>(){

    abstract fun getGlassList();
}