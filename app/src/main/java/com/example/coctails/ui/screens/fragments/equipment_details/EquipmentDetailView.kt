package com.example.coctails.ui.screens.fragments.equipment_details

import com.example.coctails.network.models.firebase.drink.Equipment
import com.example.coctails.ui.BaseView

interface EquipmentDetailView : BaseView {
    fun showEquipment(equipment: Equipment?, selected : Boolean)
    fun changesSuccess(id : Int, selected: Boolean)
}