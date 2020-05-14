package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.adapters

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerIconClick
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.model.IngredientModelSelection
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_ingredients_ws.view.*

class AllIngredientRecyclerAdapter(private val onRecyclerItemClick: OnRecyclerItemClick,
                                   private val onRecyclerIconClick: OnRecyclerIconClick
) :
    RecyclerView.Adapter<AllIngredientRecyclerAdapter.ViewHolder>() {

    private val ingredientsList = ArrayList<IngredientModelSelection>()

    private val sparseArray = SparseBooleanArray()

    fun setList(stList: List<IngredientModelSelection>) {
        ingredientsList.clear()
        ingredientsList.addAll(stList)
        setSortedByNameList()
    }


    private fun setSortedByNameList() {
        ingredientsList.sortWith(Comparator { firstName, secondName ->
            firstName.name.compareTo(
                secondName.name
            )
        })
        notifyDataSetChanged()
    }

    fun resetDataItem(position: Int){
        sparseArray.put(position, ingredientsList[position].isSelected)
    }

    fun resetDataItemByInterface(position: Int, isSelected: Boolean){
        ingredientsList[position].isSelected = isSelected
        sparseArray.put(position, ingredientsList[position].isSelected)
    }

    fun getAdapterList(): List<IngredientModelSelection> = ingredientsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recycler_ingredients_ws, parent, false)

        val viewHolder = ViewHolder(view)
        viewHolder.onItemClick = onRecyclerItemClick
        viewHolder.onIconClick = onRecyclerIconClick

        return viewHolder
    }

    override fun getItemCount(): Int = ingredientsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ingredientsList[position]

        if(item.isSelected){
            sparseArray.put(position, true)
        }

        holder.bind(item, sparseArray.get(position))
    }

    private fun addToSpare(position: Int, isSelected: Boolean) {
        if (isSelected) {
            sparseArray.put(position, !sparseArray.get(position))
        } else {
            sparseArray.put(position, isSelected)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        lateinit var onItemClick: OnRecyclerItemClick
        lateinit var onIconClick: OnRecyclerIconClick

        fun bind(ingredient: IngredientModelSelection, isSelected: Boolean) {
            itemView.setOnClickListener { onItemClick.onItemClick(adapterPosition) }
            var status = isSelected
            selectView(status)

            itemView.addIngredientsWS.setOnClickListener {
                status = !status
                selectView(status)
                addToSpare(adapterPosition, status)
                ingredient.isSelected = status
                onIconClick.onIconClick(adapterPosition, status)
            }

            Glide.with(itemView.context)
                .load(ingredient.image)
                .apply(RequestOptions().circleCrop())
                .into(itemView.ingredientImageWS)

            itemView.ingredientNameWS.text = ingredient.name
        }

        private fun selectView(isSelected: Boolean) {
            if (isSelected) {
                itemView.ingredientSelectViewWS.setBackgroundDrawable(itemView.context.getDrawable(R.drawable.selected_view))
                itemView.addIngredientsWS.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_delete))
            } else {
                itemView.ingredientSelectViewWS.setBackgroundDrawable(itemView.context.getDrawable(R.drawable.unselected_view))
                itemView.addIngredientsWS.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_add))
            }
        }
    }
}