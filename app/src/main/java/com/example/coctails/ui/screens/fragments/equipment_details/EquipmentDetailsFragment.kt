package com.example.coctails.ui.screens.fragments.equipment_details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.network.models.firebase.drink.Equipment
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.guide_detail.GuideDetailsFragment
import com.example.coctails.utils.EQUIPMENT_ID
import com.example.coctails.utils.GUIDE_ID
import com.example.coctails.utils.clickWithDebounce
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_equipment_details.*

class EquipmentDetailsFragment : BaseFragment<EquipmentDetailsPresenter, EquipmentDetailView>(),
    EquipmentDetailView {

    private var activity : MainActivity? = null

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
        commonToolbarBackPress.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    override fun showEquipment(equipment: Equipment?) {
        commonProgressBar.visibility = View.GONE
        equipmentDetailScroll.visibility = View.VISIBLE
        Glide.with(this).load(equipment?.image).into(imageED)
        commonToolbarTitle.text = equipment?.name
        descriptionED.text = equipment?.description

        equipmentShop.setOnClickListener{
            activity?.customToast(getString(R.string.clickToShop), 1)
        }

        equipmentShop.setOnLongClickListener{
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(equipment?.link))
            startActivity(browserIntent)
            return@setOnLongClickListener true
        }

        equipmentTrolley.setOnClickListener{
            trolleyImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_grocery_trolley_selected))
        }

        guideIntent.clickWithDebounce {
            val fragment = GuideDetailsFragment()

            val bundle = Bundle()

            bundle.putInt(GUIDE_ID, equipment?.guide!!)
            fragment.arguments = bundle

            activity?.loadFragment(fragment, "GuideDetails", true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
