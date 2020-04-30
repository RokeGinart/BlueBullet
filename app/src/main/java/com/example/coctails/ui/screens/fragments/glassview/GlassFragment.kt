package com.example.coctails.ui.screens.fragments.glassview

import android.content.Context
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide

import com.example.coctails.R
import com.example.coctails.network.models.firebase.drink.GlassDetails
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.utils.GLASS_ID
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_glass.*


class GlassFragment : BaseFragment<GlassPresenter, GlassView>(), GlassView{

    private var activity : MainActivity? = null

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
        commonToolbarBackPress.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    override fun showGlass(glass: GlassDetails?) {
        Glide.with(this).load(glass?.image).into(imageGD)
        commonToolbarTitle.text = glass?.name
        descriptionGD.text = glass?.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
