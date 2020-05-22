package com.example.coctails.ui.screens.fragments.equipment

import com.example.coctails.ui.BasePresenter

abstract class EquipmentPresenter : BasePresenter<EquipmentView>() {
    abstract fun getAllEquipment()
}