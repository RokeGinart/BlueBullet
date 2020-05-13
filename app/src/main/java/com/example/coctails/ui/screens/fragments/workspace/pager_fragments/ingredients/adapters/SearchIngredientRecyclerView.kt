package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.coctails.R
import com.example.coctails.network.models.firebase.drink.IngredientsModel
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.interfaces.OnSearchItemClick
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_search_ingredients.view.*

class SearchIngredientRecyclerView(private val onRecyclerItemClick: OnSearchItemClick) : RecyclerView.Adapter<SearchIngredientRecyclerView.ViewHolder>() {

    private val ingredientsList = ArrayList<IngredientsModel>()

    fun setList(stList: List<IngredientsModel>){
        ingredientsList.clear()
        ingredientsList.addAll(stList)
        notifyDataSetChanged()
    }

    fun getAdapterList() : List<IngredientsModel> = ingredientsList

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
        holder.bind(item)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        lateinit var onItemClick: OnSearchItemClick

        fun bind(ingredient: IngredientsModel){
            var status = true

            itemView.setOnClickListener { onItemClick.onSearchItemClick(adapterPosition) }

            itemView.addIngredientsSI.setOnClickListener{
                status = if(status) {
                    itemView.addIngredientsSI.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_delete))
                    false
                } else {
                    itemView.addIngredientsSI.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_add))
                    true
                }
            }


            Glide.with(itemView.context)
                .load(ingredient.image)
                .apply(RequestOptions().circleCrop())
                .into(itemView.ingredientImageSI)

            itemView.ingredientNameSI.text = ingredient.name
        }
    }
}