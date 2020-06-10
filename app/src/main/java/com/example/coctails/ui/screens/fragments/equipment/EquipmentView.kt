package com.example.coctails.ui.screens.fragments.equipment

import com.example.coctails.core.room.entity.equipment_data.EquipmentFirebaseData
import com.example.coctails.ui.BaseView

interface EquipmentView : BaseView{
    fun showEquipment(equipmentFirebaseDataList: List<EquipmentFirebaseData>)
}