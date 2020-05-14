package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.adapters

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.coctails.R
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.interfaces.OnSearchItemClick
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.model.IngredientModelSelection
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_search_ingredients.view.*

class SearchIngredientRecyclerView(private val onRecyclerItemClick: OnSearchItemClick) : RecyclerView.Adapter<SearchIngredientRecyclerView.ViewHolder>() {

    private val ingredientsList = ArrayList<IngredientModelSelection>()
    private var sparseArray : SparseBooleanArray? = null

    fun setList(stList: List<IngredientModelSelection>){
        ingredientsList.clear()
        ingredientsList.addAll(stList)
        sparseArray = SparseBooleanArray()
        notifyDataSetChanged()
    }

    fun getAdapterList() : List<IngredientModelSelection> = ingredientsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recycler_search_ingredients, parent, false)

        val viewHolder = ViewHolder(view)
        viewHolder.onItemClick = onRecyclerItemClick

        return viewHolder
    }

    override fun getItemCount(): Int = ingredientsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ingredientsList[position]

        if(item.isSelected){
            sparseArray?.put(position, true)
        }

        holder.bind(item, position, sparseArray?.get(position)!!)
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

        lateinit var onItemClick: OnSearchItemClick

        fun bind(ingredient: IngredientModelSelection, position: Int, isSelected: Boolean){
            itemView.setOnClickListener { onItemClick.onSearchItemClick(adapterPosition) }

            var status = isSelected

            selectView(status)

            itemView.addIngredientsSI.setOnClickListener{
                status = !status
                selectView(status)
                addToSpare(position, status)
                ingredient.isSelected = status
                onItemClick.onSearchIconClick(adapterPosition)
            }


            Glide.with(itemView.context)
                .load(ingredient.image)
                .apply(RequestOptions().circleCrop())
                .into(itemView.ingredientImageSI)

            itemView.ingredientNameSI.text = ingredient.name
        }

        private fun selectView(isSelected: Boolean) {
            if (isSelected) {
                itemView.addIngredientsSI.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_delete))
            } else {
                itemView.addIngredientsSI.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_add))
            }
        }
    }
}