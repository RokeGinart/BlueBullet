package com.example.coctails.ui.screens.fragments.glass

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager

import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.firebase.drink.GlassDetails
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.glass.adapters.GlassRecyclerViewAdapter
import com.example.coctails.ui.screens.fragments.glassdetails.GlassFragment
import com.example.coctails.utils.GLASS_ID
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_glass_w.*


class GlassWSFragment : BaseFragment<GlassWSPresenter, GlassWSView>(), GlassWSView, OnRecyclerItemClick{

    private var activity : MainActivity? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var adapter: GlassRecyclerViewAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_glass_w

    override fun providePresenter(): GlassWSPresenter = GlassWSPresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)
        presenter.getGlassList()

        setupRecycler()
    }

    override fun onResume() {
        super.onResume()
        commonToolbarTitle.text = getString(R.string.glass_ws)
        commonToolbarBackPress.setOnClickListener{ activity?.onBackPressed()}
    }

    private fun setupRecycler() {
        mLayoutManager = GridLayoutManager(context, 2)
        glassRecyclerView.layoutManager = mLayoutManager
        glassRecyclerView.setHasFixedSize(true)
        adapter = GlassRecyclerViewAdapter(this)
        glassRecyclerView.adapter = adapter
    }

    override fun showGlassList(glassList: List<GlassDetails>) {
        adapter?.setList(glassList)
    }


    override fun onItemClick(position: Int) {
        val fragment = GlassFragment()

        val bundle = Bundle()

        bundle.putInt(GLASS_ID, adapter?.getAdapterList()?.get(position)?.id!!)
        fragment.arguments = bundle

        activity?.loadFragment(fragment, "GlassDetails", true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }

}
