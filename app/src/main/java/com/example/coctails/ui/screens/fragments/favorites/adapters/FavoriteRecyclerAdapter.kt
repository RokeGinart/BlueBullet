package com.example.coctails.ui.screens.fragments.favorites.adapters

import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.core.room.entity.FavoriteModel
import com.example.coctails.interfaces.OnRecyclerIconClick
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.utils.clickWithDebounce
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_favorite_item.view.*

class FavoriteRecyclerAdapter(private val onRecyclerItemClick: OnRecyclerItemClick,
                              private val onRecyclerIconClick: OnRecyclerIconClick) : RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder>(){

    private val favoriteModel = ArrayList<FavoriteModel?>()
    private var sparseArray : SparseBooleanArray? = null

    fun setList(stList: List<FavoriteModel?>) {
        favoriteModel.clear()
        sparseArray = SparseBooleanArray()
        favoriteModel.addAll(stList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recycler_favorite_item, parent, false)

        val viewHolder = ViewHolder(view)
        viewHolder.onItemClick = onRecyclerItemClick
        viewHolder.onIconClick = onRecyclerIconClick

        return viewHolder
    }

    fun getAdapterList() : ArrayList<FavoriteModel?> = favoriteModel

    override fun getItemCount(): Int = favoriteModel.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favoriteModel[position]

        if(favorite?.favorite!!){
            sparseArray?.put(position, true)
        }

        holder.bind(favorite, sparseArray?.get(position)!!)
    }

    private fun addToSpare(position: Int, isSelected: Boolean) {
        if (isSelected) {
            sparseArray?.put(position, !sparseArray?.get(position)!!)
        } else {
            sparseArray?.put(position, isSelected)
        }
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        lateinit var onItemClick: OnRecyclerItemClick
        lateinit var onIconClick: OnRecyclerIconClick

        @SuppressLint("SetTextI18n")
        fun bind(favoriteItem: FavoriteModel?, isSelected: Boolean) {
            var status = isSelected

            selectView(status)

            itemView.clickWithDebounce { onItemClick.onItemClick(adapterPosition) }
            itemView.favoriteIcon.clickWithDebounce {
                status = !status
                selectView(status)
                addToSpare(adapterPosition, status)
                favoriteItem?.favorite = status

                onIconClick.onIconClick(adapterPosition, status)
            }

            Glide.with(itemView.context)
                .load(favoriteItem?.image)
                .into(itemView.favoriteImage)

            itemView.favoriteName.text = favoriteItem?.name
            itemView.favoriteCategory.text = favoriteItem?.categoryName
            itemView.abvCategory.text =  favoriteItem?.abv.toString() + itemView.context.getString(R.string.percent)
        }

        private fun selectView(isSelected: Boolean) {
            if (isSelected) {
                itemView.favoriteIcon.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_favorite_s))
            } else {
                itemView.favoriteIcon.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_favorite_ns))
            }
        }
    }
}