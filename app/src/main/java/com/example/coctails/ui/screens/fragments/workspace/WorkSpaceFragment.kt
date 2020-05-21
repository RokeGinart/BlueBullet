package com.example.coctails.ui.screens.fragments.workspace

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.coctails.R
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.workspace.adapters.PageAdapter
import com.example.coctails.utils.PublisherSubject
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_work_space.*

class WorkSpaceFragment : BaseFragment<WorkSpacePresenter, WorkSpaceView>(), WorkSpaceView{

    private var activity: MainActivity? = null

    override fun getLayoutId(): Int = R.layout.fragment_work_space

    override fun providePresenter(): WorkSpacePresenter = WorkSpacePresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)
    }

    override fun onResume() {
        super.onResume()
        commonToolbarTitle.text = getString(R.string.workspace)
        commonToolbarBackPress.setOnClickListener{
            activity?.onBackPressed()
        }


        kitchenViewPager.adapter = PageAdapter(context!!, childFragmentManager, PublisherSubject().getInstance()!!)
        kitchenTabs.setupWithViewPager(kitchenViewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
