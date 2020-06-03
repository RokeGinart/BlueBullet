package com.example.coctails.ui.screens.fragments.shopping

import com.example.coctails.core.room.entity.Shopping
import com.example.coctails.ui.BaseView

interface ShoppingView : BaseView {

    fun showResult(shopping : List<Shopping>)
    fun showMessage()
}