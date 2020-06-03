package com.example.coctails.ui.screens.fragments.glassdetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide

import com.example.coctails.R
import com.example.coctails.network.models.firebase.drink.GlassDetails
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.utils.GLASS_ID
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_equipment_details.*
import kotlinx.android.synthetic.main.fragment_glass.*
import kotlinx.android.synthetic.main.fragment_glass.trolleyImage

class GlassFragment : BaseFragment<GlassPresenter, GlassView>(), GlassView {

    private var activity: MainActivity? = null
    private var selectedItem = false

    override fun getLayoutId(): Int = R.layout.fragment_glass

    override fun providePresenter(): GlassPresenter = GlassPresenterImpl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        val id = bundle?.getInt(GLASS_ID)
        id?.let { presenter.getGlass(it) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        commonToolbarBackPress.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun showGlass(glass: GlassDetails?, selected: Boolean) {
        commonProgressBar.visibility = View.GONE
        glassDetailScroll.visibility = View.VISIBLE
        Glide.with(this).load(glass?.image).into(imageGD)
        commonToolbarTitle.text = glass?.name
        descriptionGD.text = glass?.description

        if (selected) {
            trolleyImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_grocery_trolley_selected))
        }

        selectedItem = selected
        clickers(glass)
    }

    private fun clickers(glass: GlassDetails?) {
        glassShop.setOnClickListener {
            activity?.customToast(getString(R.string.clickToShop), 1)
        }

        glassShop.setOnLongClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(glass?.link))
            startActivity(browserIntent)
            return@setOnLongClickListener true
        }

        glassTrolley.setOnClickListener {
            selectedItem = if(selectedItem){
                trolleyImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_grocery_trolley))
                activity?.customToast(getString(R.string.shopping_start) + glass?.name + getString(R.string.shopping_delete), 2)
                false
            } else {
                trolleyImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_grocery_trolley_selected))
                activity?.customToast(getString(R.string.shopping_start) + glass?.name + getString(R.string.shopping_added), 1)
                true
            }

            presenter.updateShoppingStatus(glass?.id!!, glass.name,  glass.image, "glass", "glass")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
