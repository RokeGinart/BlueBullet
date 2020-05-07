package com.example.coctails.ui.screens.fragments.workspace

import android.content.Context
import android.os.Bundle
import android.view.View

import com.example.coctails.R
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.glass.GlassWSFragment
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_workspace.*

class WorkspaceFragment : BaseFragment<WorkspacePresenter, WorkspaceView>(), WorkspaceView, View.OnClickListener {

    private var activity: MainActivity? = null

    override fun getLayoutId(): Int = R.layout.fragment_workspace

    override fun providePresenter(): WorkspacePresenter = WorkspacePresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        glassWS.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        commonToolbarBackPress.visibility = View.GONE
        commonToolbarTitle.text = activity?.getString(R.string.kitchen)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.glassWS -> {
                val fragment = GlassWSFragment()
                activity?.loadFragment(fragment, "glass", true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
