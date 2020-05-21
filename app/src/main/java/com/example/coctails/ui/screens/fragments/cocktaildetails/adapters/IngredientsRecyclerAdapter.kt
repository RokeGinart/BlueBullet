package com.example.coctails.ui.screens.fragments.cocktaildetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.ui.screens.fragments.cocktaildetails.model.IngredientModelCD
import com.example.coctails.utils.clickWithDebounce
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_ingredients_item.view.*

class IngredientsRecyclerAdapter(private val onRecyclerItemClick: OnRecyclerItemClick) :
    RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder>() {

    private val ingredients = ArrayList<IngredientModelCD?>()

    fun setList(stList: List<IngredientModelCD?>) {
        ingredients.addAll(stList)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, isSelection: Boolean){
        ingredients[position]?.isSelected = isSelection
    }

    fun getAdapterList() : ArrayList<IngredientModelCD?> = ingredients

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recycler_ingredients_item, parent, false)

        val viewHolder = ViewHolder(view)
        viewHolder.onItemClick = onRecyclerItemClick

        return viewHolder
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
            holder.bind(ingredient!!)
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        lateinit var onItemClick: OnRecyclerItemClick

        fun bind(ingredients: IngredientModelCD) {
            itemView.clickWithDebounce { onItemClick.onItemClick(adapterPosition) }

            if(ingredients.isSelected){
                itemView.ingredientImage.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_check_box_ch))
            }

            itemView.ingredientName.text = ingredients.name
        }
    }
}