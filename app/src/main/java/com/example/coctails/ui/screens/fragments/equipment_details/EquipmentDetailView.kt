package com.example.coctails.ui.screens.fragments.equipment_details

import com.example.coctails.core.room.entity.equipment_data.EquipmentFirebaseData
import com.example.coctails.ui.BaseView

interface EquipmentDetailView : BaseView {
    fun showEquipment(equipmentFirebaseData: EquipmentFirebaseData?, selected : Boolean)
    fun changesSuccess(id : Int, selected: Boolean)
}