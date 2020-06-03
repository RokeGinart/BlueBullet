package com.example.coctails.ui.screens.fragments.shopping.adapters

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.core.room.entity.Shopping
import com.example.coctails.interfaces.OnRecyclerIconClick
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.utils.clickWithDebounce
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_shopping_item.view.*

class ShoppingRecyclerViewAdapter(private val onRecyclerItemClick: OnRecyclerItemClick, private val onRecyclerIconClick: OnRecyclerIconClick) : RecyclerView.Adapter<ShoppingRecyclerViewAdapter.ViewHolder>(){

    private val shopping = ArrayList<Shopping>()
    private var sparseArray : SparseBooleanArray? = null


    fun setList(stList: List<Shopping>){
        shopping.clear()
        sparseArray = SparseBooleanArray()
        shopping.addAll(stList)
        notifyDataSetChanged()
    }

    fun resetDataItem(position: Int, isSelected: Boolean){
        shopping[position].selected = isSelected
        sparseArray?.put(position, shopping[position].selected)
    }

    fun getAdapterList() : List<Shopping> = shopping

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recycler_shopping_item, parent, false)

        val viewHolder = ViewHolder(view)
        viewHolder.onItemClick = onRecyclerItemClick
        viewHolder.onIconClick = onRecyclerIconClick

        return viewHolder
    }

    override fun getItemCount(): Int = shopping.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = shopping[position]

        if(item.selected){
            sparseArray?.put(position, true)
        }

        holder.bind(item, sparseArray?.get(position)!!)
    }

    private fun addToSpare(position: Int, isSelected: Boolean) {
        if (isSelected) {
            sparseArray?.put(position, !sparseArray?.get(position)!!)
        } else {
            sparseArray?.put(position, isSelected)
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        lateinit var onItemClick: OnRecyclerItemClick
        lateinit var onIconClick: OnRecyclerIconClick

        fun bind(shopping: Shopping, isSelected: Boolean){
            var status = isSelected
            selectView(status)

            itemView.clickWithDebounce { onItemClick.onItemClick(adapterPosition) }
            itemView.shoppingIcon.clickWithDebounce {
                status = !status
                selectView(status)
                addToSpare(adapterPosition, status)
                shopping.selected = status

                onIconClick.onIconClick(adapterPosition, status)
            }

            Glide.with(itemView.context).load(shopping.image).into(itemView.shoppingImage)
            itemView.shoppingName.text = shopping.name
        }

        private fun selectView(isSelected: Boolean) {
            if (isSelected) {
                itemView.shoppingIcon.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_grocery_trolley_selected))
            } else {
                itemView.shoppingIcon.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_grocery_trolley))
            }
        }
    }
}