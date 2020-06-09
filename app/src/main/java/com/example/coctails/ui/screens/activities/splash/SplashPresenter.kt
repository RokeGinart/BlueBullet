package com.example.coctails.ui.screens.activities.splash

import com.example.coctails.ui.BasePresenter

abstract class SplashPresenter : BasePresenter<SplashView>() {
    abstract fun downloadData()
}