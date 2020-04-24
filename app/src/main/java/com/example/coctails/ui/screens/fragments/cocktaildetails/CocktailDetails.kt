package com.example.coctails.ui.screens.fragments.cocktaildetails

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.network.models.CocktailsSearch
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_cocktail_details.*

class CocktailDetails : BaseFragment<CocktailDetailsPresenter, CocktailDetailsView>(),
    CocktailDetailsView {

    override fun getLayoutId(): Int = R.layout.fragment_cocktail_details

    private var activity: MainActivity? = null

    private var cocktailId: String? = null

    override fun providePresenter(): CocktailDetailsPresenter = CocktailDetailsPresenterImpl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        cocktailId = bundle!!.getString("COCKTAIL")

        presenter.getCocktailDetails(cocktailId!!)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        commonToolbarTitle.text = "Cocktail Details"
        commonToolbarBackPress.visibility = View.VISIBLE
        commonToolbarBackPress.setOnClickListener { activity?.onBackPressed() }
    }

    override fun showResult(cocktails: CocktailsSearch) {
        Glide.with(this).load(cocktails.drinks?.get(0)?.strDrinkThumb).centerCrop()
            .into(cocktailImage)
        cocktailName.text = cocktails.drinks?.get(0)?.strDrink
        cocktailData.text = cocktails.drinks?.get(0)?.strInstructions
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}