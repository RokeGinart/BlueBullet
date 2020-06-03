package com.example.coctails.ui.screens.fragments.guide

import com.example.coctails.network.models.firebase.drink.Guide
import com.example.coctails.ui.BaseView

interface GuideView : BaseView {
    fun showAllGuide(guide : List<Guide>)
}