package com.example.coctails.ui.screens.fragments.cocktaildetails

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.cocktaildetails.adapters.IngredientsRecyclerAdapter
import com.example.coctails.ui.screens.fragments.mainscreen.adapters.CocktailsRecyclerAdapter
import com.example.coctails.utils.CATEGORY
import com.example.coctails.utils.COCKTAIL
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_cocktail_details.*
import kotlinx.android.synthetic.main.fragment_main_screen.*

class CocktailDetails : BaseFragment<CocktailDetailsPresenter, CocktailDetailsView>(),
    CocktailDetailsView, OnRecyclerItemClick{

    override fun getLayoutId(): Int = R.layout.fragment_cocktail_details

    private var activity: MainActivity? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var adapter: IngredientsRecyclerAdapter? = null

    private var cocktailId: String? = null
    private var category: String? = null

    override fun providePresenter(): CocktailDetailsPresenter = CocktailDetailsPresenterImpl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        cocktailId = bundle!!.getString(COCKTAIL)
        category = bundle.getString(CATEGORY)

        presenter.getCocktailDetails(category!!, cocktailId!!)

        setupRecycler()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    private fun setupRecycler() {
        mLayoutManager = LinearLayoutManager(context)
        ingredientsRecyclerCD.layoutManager = mLayoutManager
        ingredientsRecyclerCD.setHasFixedSize(true)
        adapter = IngredientsRecyclerAdapter(this)
        ingredientsRecyclerCD.adapter = adapter
    }

    override fun onItemClick(position: Int) {

    }

    override fun onResume() {
        super.onResume()
        commonToolbarBackPress.visibility = View.VISIBLE
        commonToolbarBackPress.setOnClickListener { activity?.onBackPressed() }
    }

    override fun showResult(cocktails: Cocktails) {
        Glide.with(this).load(cocktails.image).centerCrop()
            .into(cocktailImage)
        cocktailName.text = cocktails.name
        cocktailCategoryCD.text = cocktails.category?.name
        cocktailGlassCD.text = cocktails.glass
        cocktailIbaCD.text = cocktails.iba
        instructionCD.text = cocktails.instruction
        cookTimeCD.text = cocktails.cooktime

        adapter?.setList(cocktails.ingredients!!.subList(1, cocktails.ingredients!!.size))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}