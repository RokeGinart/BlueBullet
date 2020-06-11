package com.example.coctails.ui.screens.fragments.guide.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.core.room.entity.guide_data.GuideFirebaseData
import com.example.coctails.utils.clickWithDebounce
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_guide_item.view.*

class GuideRecyclerViewAdapter(private val onRecyclerItemClick: OnRecyclerItemClick) : RecyclerView.Adapter<GuideRecyclerViewAdapter.ViewHolder>(){

    private val guide = ArrayList<GuideFirebaseData>()

    fun setList(stList: List<GuideFirebaseData>){
        guide.clear()
        guide.addAll(stList)
        notifyDataSetChanged()
    }


    fun getAdapterList() : List<GuideFirebaseData> = guide

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recycler_guide_item, parent, false)

        val viewHolder = ViewHolder(view)
        viewHolder.onItemClick = onRecyclerItemClick

        return viewHolder
    }

    override fun getItemCount(): Int = guide.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = guide[position]
        holder.bind(item)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        lateinit var onItemClick: OnRecyclerItemClick

        fun bind(guideFirebaseData: GuideFirebaseData){
            itemView.clickWithDebounce { onItemClick.onItemClick(adapterPosition) }

            Glide.with(itemView.context).load(guideFirebaseData.image).into(itemView.guideImageGA)
            itemView.guideTitleGA.text = guideFirebaseData.title
        }
    }
}