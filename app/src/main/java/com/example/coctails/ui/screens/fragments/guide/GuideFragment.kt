package com.example.coctails.ui.screens.fragments.guide

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.core.room.entity.guide_data.GuideFirebaseData
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.guide.adapters.GuideRecyclerViewAdapter
import com.example.coctails.ui.screens.fragments.guide_detail.GuideDetailsFragment
import com.example.coctails.utils.GUIDE_ID
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_guide.*


class GuideFragment : BaseFragment<GuidePresenter, GuideView>(), GuideView, OnRecyclerItemClick {

    private var activity : MainActivity? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var adapter: GuideRecyclerViewAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_guide

    override fun providePresenter(): GuidePresenter = GuidePresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)
        presenter.getAllGuide()

        setupRecycler()
    }


    override fun onResume() {
        super.onResume()
        commonToolbarTitle.text = getString(R.string.guides)
        commonToolbarBackPress.setOnClickListener{ activity?.onBackPressed()}
        guideContainer.setOnClickListener(null)
    }

    private fun setupRecycler() {
        mLayoutManager = GridLayoutManager(context, 2)
        guideRecyclerView.layoutManager = mLayoutManager
        guideRecyclerView.setHasFixedSize(true)
        adapter = GuideRecyclerViewAdapter(this)
        guideRecyclerView.adapter = adapter
    }

    override fun showAllGuide(guideFirebaseData: List<GuideFirebaseData>) {
        commonProgressBar.visibility = View.GONE
        adapter?.setList(guideFirebaseData)
    }

    override fun onItemClick(position: Int) {
        val fragment = GuideDetailsFragment()
        val bundle = Bundle()

        bundle.putInt(GUIDE_ID, adapter?.getAdapterList()?.get(position)?.id!!)
        fragment.arguments = bundle

        activity?.loadFragment(fragment, "GuideDetails", true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
