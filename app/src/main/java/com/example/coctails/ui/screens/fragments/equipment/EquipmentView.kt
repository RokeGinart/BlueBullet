package com.example.coctails.ui.screens.fragments.equipment

import com.example.coctails.network.models.firebase.drink.Equipment
import com.example.coctails.ui.BaseView

interface EquipmentView : BaseView{
    fun showEquipment(equipmentList: List<Equipment>)
}