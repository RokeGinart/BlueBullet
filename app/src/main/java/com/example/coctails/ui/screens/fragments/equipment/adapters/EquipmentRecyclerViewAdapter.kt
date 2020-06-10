package com.example.coctails.ui.screens.fragments.equipment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.core.room.entity.equipment_data.EquipmentFirebaseData
import com.example.coctails.utils.clickWithDebounce
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_glass_item.view.*

class EquipmentRecyclerViewAdapter(private val onRecyclerItemClick: OnRecyclerItemClick) : RecyclerView.Adapter<EquipmentRecyclerViewAdapter.ViewHolder>(){

    private val glass = ArrayList<EquipmentFirebaseData>()

    fun setList(stList: List<EquipmentFirebaseData>){
        glass.clear()
        glass.addAll(stList)
        setSortedByNameList()
    }

    private fun setSortedByNameList() {
        glass.sortWith(Comparator { firstName, secondName -> firstName.name.compareTo(secondName.name) })
        notifyDataSetChanged()
    }

    fun getAdapterList() : List<EquipmentFirebaseData> = glass

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recycler_glass_item, parent, false)

        val viewHolder = ViewHolder(view)
        viewHolder.onItemClick = onRecyclerItemClick

        return viewHolder
    }

    override fun getItemCount(): Int = glass.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = glass[position]
        holder.bind(item)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        lateinit var onItemClick: OnRecyclerItemClick

        fun bind(equipmentFirebaseData: EquipmentFirebaseData){
            itemView.clickWithDebounce { onItemClick.onItemClick(adapterPosition) }

            Glide.with(itemView.context)
                .load(equipmentFirebaseData.image)
                .centerCrop()
                .into(itemView.glassImageGV)

            itemView.glassNameGV.text = equipmentFirebaseData.name
        }
    }
}