package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.firebase.drink.IngredientsModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_ingredients_ws.view.*

class AllIngredientRecyclerAdapter(private val onRecyclerItemClick: OnRecyclerItemClick) : RecyclerView.Adapter<AllIngredientRecyclerAdapter.ViewHolder>() {

    private val ingredientsList = ArrayList<IngredientsModel>()

    fun setList(stList: List<IngredientsModel>){
        ingredientsList.clear()
        ingredientsList.addAll(stList)
        setSortedByNameList()
    }

    private fun setSortedByNameList() {
        ingredientsList.sortWith(Comparator { firstName, secondName -> firstName.name.compareTo(secondName.name) })
        notifyDataSetChanged()
    }

    fun getAdapterList() : List<IngredientsModel> = ingredientsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recycler_ingredients_ws, parent, false)

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

        lateinit var onItemClick: OnRecyclerItemClick

        fun bind(ingredient: IngredientsModel){
            var status = true

            itemView.setOnClickListener { onItemClick.onItemClick(adapterPosition) }

            itemView.addIngredientsWS.setOnClickListener{
                status = if(status) {
                    itemView.ingredientSelectViewWS.setBackgroundDrawable(itemView.context.getDrawable(R.drawable.selected_view))
                    itemView.addIngredientsWS.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_delete))
                    false
                } else {
                    itemView.ingredientSelectViewWS.setBackgroundDrawable(itemView.context.getDrawable(R.drawable.unselected_view))
                    itemView.addIngredientsWS.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_add))
                    true
                }
            }


            Glide.with(itemView.context)
                .load(ingredient.image)
                .apply(RequestOptions().circleCrop())
                .into(itemView.ingredientImageWS)

            itemView.ingredientNameWS.text = ingredient.name
        }
    }
}