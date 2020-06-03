package com.example.coctails.ui.screens.fragments.guide

import com.example.coctails.ui.BasePresenter

abstract class GuidePresenter : BasePresenter<GuideView>() {
    abstract fun getAllGuide()
}