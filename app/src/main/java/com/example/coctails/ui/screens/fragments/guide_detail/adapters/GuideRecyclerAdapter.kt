package com.example.coctails.ui.screens.fragments.guide_detail.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.network.models.firebase.drink.Guide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_steps_item.view.*


class GuideRecyclerAdapter : RecyclerView.Adapter<GuideRecyclerAdapter.ViewHolder>() {

    private val steps = ArrayList<Guide.Steps>()
    private val left = 0
    private val right = 1

    fun setList(stList: List<Guide.Steps>) {
        steps.clear()
        steps.addAll(stList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        view = if (viewType == left) {
            layoutInflater.inflate(R.layout.recycler_steps_item, parent, false)
        } else {
            layoutInflater.inflate(R.layout.recycler_steps_reverse_item, parent, false)
        }

        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position.rem(2) == 0) {
            left
        } else {
            right
        }
    }


    fun getAdapterList(): ArrayList<Guide.Steps> = steps

    override fun getItemCount(): Int = steps.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = steps[position]
        holder.bind(favorite)
    }


    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView


        fun bind(step: Guide.Steps) {
            Glide.with(itemView.context)
                .load(step.image)
                .into(itemView.stepImage)

            itemView.stepDescription.text = step.description
        }
    }
}