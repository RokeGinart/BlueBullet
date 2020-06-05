package com.example.coctails.ui.screens.fragments.equipment_details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.core.App
import com.example.coctails.network.models.firebase.drink.Equipment
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.guide_detail.GuideDetailsFragment
import com.example.coctails.ui.screens.fragments.shopping.model.ItemChange
import com.example.coctails.utils.EQUIPMENT_ID
import com.example.coctails.utils.GUIDE_ID
import com.example.coctails.utils.clickWithDebounce
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_equipment_details.*

class EquipmentDetailsFragment : BaseFragment<EquipmentDetailsPresenter, EquipmentDetailView>(),
    EquipmentDetailView {

    private var activity: MainActivity? = null
    private var shoppingSelected = false

    override fun getLayoutId(): Int = R.layout.fragment_equipment_details

    override fun providePresenter(): EquipmentDetailsPresenter = EquipmentDetailsPresenterImpl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        val id = bundle?.getInt(EQUIPMENT_ID)
        id?.let { presenter.getEquipment(it) }
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

    override fun showEquipment(equipment: Equipment?, selected: Boolean) {
        commonProgressBar.visibility = View.GONE
        equipmentDetailScroll.visibility = View.VISIBLE
        Glide.with(this).load(equipment?.image).into(imageED)
        commonToolbarTitle.text = equipment?.name
        descriptionED.text = equipment?.description

        if (selected) {
            trolleyImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_grocery_trolley_selected))
        }
        shoppingSelected = selected

        clickers(equipment)
    }

    private fun clickers(equipment: Equipment?){
        equipmentShop.setOnClickListener {
            activity?.customToast(getString(R.string.clickToShop), 1)
        }

        equipmentShop.setOnLongClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(equipment?.link))
            startActivity(browserIntent)
            return@setOnLongClickListener true
        }

        equipmentTrolley.setOnClickListener {
            shoppingSelected = if(shoppingSelected){
                trolleyImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_grocery_trolley))
                activity?.customToast(getString(R.string.shopping_start) + equipment?.name + getString(R.string.shopping_delete), 2)
                false
            } else {
                trolleyImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_grocery_trolley_selected))
                activity?.customToast(getString(R.string.shopping_start) + equipment?.name + getString(R.string.shopping_added), 1)
                true
            }

            presenter.updateShoppingStatus(equipment?.id!!, equipment.name,  equipment.image, "equipment", "equipment")
        }

        guideIntent.clickWithDebounce {
            if (equipment?.guide != 0) {
                val fragment = GuideDetailsFragment()

                val bundle = Bundle()

                bundle.putInt(GUIDE_ID, equipment?.guide!!)
                fragment.arguments = bundle

                activity?.loadFragment(fragment, "GuideDetails", true)
            } else {
                activity?.customToast(
                    getString(R.string.no_guide) + equipment.name + getString(R.string.no_guide_s_part),
                    2
                )
            }
        }
    }

    override fun changesSuccess(id : Int, selected: Boolean) {
        val item = ItemChange("equipment", id, selected)
        App.instanse?.subject?.publishItem(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
