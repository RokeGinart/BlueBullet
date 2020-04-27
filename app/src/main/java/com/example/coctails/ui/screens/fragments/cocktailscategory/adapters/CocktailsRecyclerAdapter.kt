package com.example.coctails.ui.screens.fragments.cocktailscategory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.firebase.drink.Cocktails
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cocktail_item.view.*

class CocktailsRecyclerAdapter(private val onRecyclerItemClick: OnRecyclerItemClick) :
    RecyclerView.Adapter<CocktailsRecyclerAdapter.ViewHolder>() {

    private val cocktails = ArrayList<Cocktails>()

    fun setList(stList: List<Cocktails>) {
        cocktails.clear()
        cocktails.addAll(stList)
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        cocktails.clear()
    }

    fun getAdapterList() : List<Cocktails> = cocktails

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

        fun bind(coctails: Cocktails) {
            itemView.setOnClickListener { onItemClick.onItemClick(adapterPosition) }

            Glide.with(itemView.context)
                .load(coctails.image)
                .centerCrop()
                .into(itemView.cocktailsImageMV)

            itemView.cocktailsNameMV.text = coctails.name
        }
    }
}

