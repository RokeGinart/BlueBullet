package com.example.coctails.ui.screens.fragments.cocktaildetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coctails.R
import com.example.coctails.core.room.entity.cocktails_data.EquipmentDB
import com.example.coctails.interfaces.OnRecyclerItemClickS
import com.example.coctails.utils.clickWithDebounce
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_equipments_cd_item.view.*

class EquipmentsRecyclerAdapter(private val onRecyclerItemClick: OnRecyclerItemClickS) :
    RecyclerView.Adapter<EquipmentsRecyclerAdapter.ViewHolder>() {

    private val equipments = ArrayList<EquipmentDB?>()

    fun setList(stList: List<EquipmentDB?>) {
        equipments.addAll(stList)
        notifyDataSetChanged()
    }

    fun getAdapterList() : ArrayList<EquipmentDB?> = equipments

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recycler_equipments_cd_item, parent, false)

        val viewHolder = ViewHolder(view)
        viewHolder.onItemClick = onRecyclerItemClick

        return viewHolder
    }

    override fun getItemCount(): Int {
        return equipments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val equip = equipments[position]
            holder.bind(equip!!)
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        lateinit var onItemClick: OnRecyclerItemClickS

        fun bind(equipment: EquipmentDB) {
            itemView.clickWithDebounce { onItemClick.onItemClickS(adapterPosition) }
            itemView.equipmentName.text = equipment.name
        }
    }
}