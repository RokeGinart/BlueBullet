package com.example.coctails.ui.screens.fragments.guide_detail

import com.example.coctails.core.room.entity.guide_data.GuideFirebaseData
import com.example.coctails.ui.BaseView

interface GuideDetailView : BaseView {
    fun showGuide(guideFirebaseData: GuideFirebaseData?)
}