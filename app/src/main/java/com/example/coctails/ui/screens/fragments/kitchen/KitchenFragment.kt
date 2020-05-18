package com.example.coctails.ui.screens.fragments.kitchen

import android.content.Context
import android.os.Bundle
import android.view.View

import com.example.coctails.R
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.glass.GlassWSFragment
import com.example.coctails.ui.screens.fragments.workspace.WorkSpaceFragment
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_kitchen.*

class KitchenFragment : BaseFragment<KitchenPresenter, KitchenView>(),
    KitchenView, View.OnClickListener {

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

        glassWS.setOnClickListener(this)
        workspaceView.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        commonToolbarBackPress?.visibility = View.GONE
        commonToolbarTitle?.text = activity?.getString(R.string.kitchen)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.glassWS -> {
                val fragment = GlassWSFragment()
                activity?.loadFragment(fragment, "glass", true)
            }
            R.id.workspaceView -> {
                val fragment = WorkSpaceFragment()
                activity?.loadFragment(fragment, "workspace", true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
