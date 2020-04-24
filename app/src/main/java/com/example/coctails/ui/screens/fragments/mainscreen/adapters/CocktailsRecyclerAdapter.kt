package com.example.coctails.ui.screens.fragments.mainscreen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.CocktailsCategoryList
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cocktail_item.view.*

class CocktailsRecyclerAdapter(private val onRecyclerItemClick: OnRecyclerItemClick) :
    RecyclerView.Adapter<CocktailsRecyclerAdapter.ViewHolder>() {

    private val cocktails = ArrayList<CocktailsCategoryList.Drink>()

    fun setList(stList: List<CocktailsCategoryList.Drink>) {
        cocktails.addAll(stList)
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        cocktails.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.cocktail_item, parent, false)

        val viewHolder = ViewHolder(view)
        viewHolder.onItemClick = onRecyclerItemClick

        return viewHolder
    }

    override fun getItemCount(): Int {
        return cocktails.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drink = cocktails[position]
        holder.bind(drink)
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        lateinit var onItemClick: OnRecyclerItemClick

        fun bind(cocktailsCategoryList: CocktailsCategoryList.Drink) {
            itemView.setOnClickListener { onItemClick.onItemClick(adapterPosition) }

            Glide.with(itemView.context)
                .load(cocktailsCategoryList.strDrinkThumb)
                .centerCrop()
                .into(itemView.cocktailsImageMV)
            itemView.cocktailsNameMV.text = cocktailsCategoryList.strDrink
        }
    }
}

