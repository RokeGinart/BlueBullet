package com.example.coctails.ui.screens.fragments.cocktaildetails.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.firebase.drink.Cocktails
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.ingredients_item.view.*

class IngredientsRecyclerAdapter(private val onRecyclerItemClick: OnRecyclerItemClick) :
    RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder>() {

    private val ingredients = ArrayList<Cocktails.Ingredients?>()

    fun setList(stList: List<Cocktails.Ingredients?>) {
        ingredients.addAll(stList)
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        ingredients.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.ingredients_item, parent, false)

        val viewHolder = ViewHolder(view)
        viewHolder.onItemClick = onRecyclerItemClick

        return viewHolder
    }

    override fun getItemCount(): Int {
        return ingredients.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
            holder.bind(ingredient)
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        lateinit var onItemClick: OnRecyclerItemClick

        fun bind(ingredients: Cocktails.Ingredients?) {
            itemView.setOnClickListener { onItemClick.onItemClick(adapterPosition) }

            itemView.ingredientName.text = ingredients?.name
        }
    }
}