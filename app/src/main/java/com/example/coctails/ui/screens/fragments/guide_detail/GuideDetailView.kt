package com.example.coctails.ui.screens.fragments.guide_detail

import com.example.coctails.network.models.firebase.drink.Equipment
import com.example.coctails.network.models.firebase.drink.Guide
import com.example.coctails.ui.BaseView

interface GuideDetailView : BaseView {
    fun showGuide(guide: Guide?)
}