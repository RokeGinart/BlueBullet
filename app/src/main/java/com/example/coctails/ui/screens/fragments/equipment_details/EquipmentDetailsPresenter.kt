package com.example.coctails.ui.screens.fragments.equipment_details

import com.example.coctails.ui.BasePresenter

abstract class EquipmentDetailsPresenter : BasePresenter<EquipmentDetailView>() {
    abstract fun getEquipment(id : Int)
}