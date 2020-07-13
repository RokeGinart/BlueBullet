package com.example.coctails.ui.screens.fragments.equipment_details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.core.App
import com.example.coctails.core.room.entity.equipment_data.EquipmentFirebaseData
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

    override fun showEquipment(equipmentFirebaseData: EquipmentFirebaseData?, selected: Boolean) {
        commonProgressBar.visibility = View.GONE
        equipmentDetailScroll.visibility = View.VISIBLE
        Glide.with(this).load(equipmentFirebaseData?.image).into(imageED)
        commonToolbarTitle.text = equipmentFirebaseData?.name
        descriptionED.text = equipmentFirebaseData?.description

        if (selected) {
            trolleyImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_grocery_trolley_selected))
        }
        shoppingSelected = selected

        clickers(equipmentFirebaseData)
    }

    private fun clickers(equipmentFirebaseData: EquipmentFirebaseData?){
        equipmentShop.setOnClickListener {
            activity?.customAddRemoveToast(getString(R.string.clickToShop), 1)
        }

        equipmentShop.setOnLongClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(equipmentFirebaseData?.link))
            startActivity(browserIntent)
            return@setOnLongClickListener true
        }

        equipmentTrolley.setOnClickListener {
            shoppingSelected = if(shoppingSelected){
                trolleyImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_grocery_trolley))
                activity?.customAddRemoveToast(getString(R.string.shopping_start) + equipmentFirebaseData?.name + getString(R.string.shopping_delete), 2)
                false
            } else {
                trolleyImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_grocery_trolley_selected))
                activity?.customAddRemoveToast(getString(R.string.shopping_start) + equipmentFirebaseData?.name + getString(R.string.shopping_added), 1)
                true
            }

            presenter.updateShoppingStatus(equipmentFirebaseData?.id!!, equipmentFirebaseData.name,  equipmentFirebaseData.image, "equipment", "equipment")
        }

        guideIntent.clickWithDebounce {
            if (equipmentFirebaseData?.guide != 0) {
                val fragment = GuideDetailsFragment()

                val bundle = Bundle()

                bundle.putInt(GUIDE_ID, equipmentFirebaseData?.guide!!)
                fragment.arguments = bundle

                activity?.loadFragment(fragment, "GuideDetails", true)
            } else {
                activity?.customAddRemoveToast(
                    getString(R.string.no_guide) + equipmentFirebaseData.name + getString(R.string.no_guide_s_part),
                    2
                )
            }
        }
    }

    override fun changesSuccess(id : Int, selected: Boolean) {
        val item = ItemChange("equipment", id, selected)
        App.instance?.subject?.publishItem(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
