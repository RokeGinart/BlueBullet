package com.example.coctails.ui.screens.fragments.guide

import com.example.coctails.core.room.entity.guide_data.GuideFirebaseData
import com.example.coctails.ui.BaseView

interface GuideView : BaseView {
    fun showAllGuide(guideFirebaseData : List<GuideFirebaseData>)
}