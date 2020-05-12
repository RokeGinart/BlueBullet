package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.cocktails

import android.content.Context
import android.os.Bundle
import android.view.View

import com.example.coctails.R
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.utils.ARG_SECTION_NUMBER


class CocktailsWSFragment : BaseFragment<CocktailsWSPresenter, CocktailsWSView>(), CocktailsWSView {

    private var activity: MainActivity? = null

    override fun getLayoutId(): Int = R.layout.fragment_cocktails_ws

    override fun providePresenter(): CocktailsWSPresenter = CocktailsWSPresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)
    }

    fun newInstance(index: Int): CocktailsWSFragment {
        val fragment = CocktailsWSFragment()
        val bundle = Bundle()
        bundle.putInt(ARG_SECTION_NUMBER, index)
        fragment.arguments = bundle
        return fragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
