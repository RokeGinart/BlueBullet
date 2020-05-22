package com.example.coctails.ui.screens.fragments.guide_detail

import com.example.coctails.ui.BasePresenter

abstract class GuideDetailsPresenter : BasePresenter<GuideDetailView>() {
    abstract fun getGuide(id : Int)
}