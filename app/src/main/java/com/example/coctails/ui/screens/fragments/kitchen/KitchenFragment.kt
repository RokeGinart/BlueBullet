package com.example.coctails.ui.screens.fragments.kitchen

import android.content.Context
import android.os.Bundle
import android.view.View

import com.example.coctails.R
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.equipment.EquipmentFragment
import com.example.coctails.ui.screens.fragments.glass.GlassWSFragment
import com.example.coctails.ui.screens.fragments.workspace.WorkSpaceFragment
import com.example.coctails.utils.clickWithDebounce
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_kitchen.*

class KitchenFragment : BaseFragment<KitchenPresenter, KitchenView>(),
    KitchenView {

    private var activity: MainActivity? = null

    override fun getLayoutId(): Int = R.layout.fragment_kitchen

    override fun providePresenter(): KitchenPresenter =
        KitchenPresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        viewClicked()
    }

    private fun viewClicked(){
        glassWS.clickWithDebounce{
            val fragment = GlassWSFragment()
            activity?.loadFragment(fragment, "Glass", true)
        }

        workspaceView.clickWithDebounce{
            val fragment = WorkSpaceFragment()
            activity?.loadFragment(fragment, "Workspace", true)
        }

        equipmentWS.clickWithDebounce{
            val fragment = EquipmentFragment()
            activity?.loadFragment(fragment, "Equipment", true)
        }
    }

    override fun onResume() {
        super.onResume()
        commonToolbarBackPress?.visibility = View.GONE
        commonToolbarTitle?.text = activity?.getString(R.string.kitchen)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
