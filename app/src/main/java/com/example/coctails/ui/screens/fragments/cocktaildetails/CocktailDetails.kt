package com.example.coctails.ui.screens.fragments.cocktaildetails

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.cocktaildetails.adapters.IngredientsRecyclerAdapter
import com.example.coctails.utils.COCKTAIL
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_cocktail_details.*

class CocktailDetails : BaseFragment<CocktailDetailsPresenter, CocktailDetailsView>(),
    CocktailDetailsView, OnRecyclerItemClick {

    override fun getLayoutId(): Int = R.layout.fragment_cocktail_details

    private var activity: MainActivity? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var adapter: IngredientsRecyclerAdapter? = null
    private var cocktails: Cocktails? = null

    override fun providePresenter(): CocktailDetailsPresenter = CocktailDetailsPresenterImpl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        cocktails = bundle?.getSerializable(COCKTAIL) as Cocktails?

        setHasOptionsMenu(true)

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

        cocktails?.let { setData(it) }
    }

    override fun onItemClick(position: Int) {

    }

    override fun onResume() {
        super.onResume()
        activity?.setSupportActionBar(commonToolbar)
        activity?.supportActionBar?.title = null
        commonToolbarBackPress.setOnClickListener { activity?.onBackPressed() }
    }

    override fun showResult(cocktails: Cocktails) {
        /* Glide.with(this).load(cocktails.image).centerCrop()
             .into(cocktailImage)
         cocktailName.text = cocktails.name
         cocktailCategoryCD.text = cocktails.category?.name
         cocktailGlassCD.text = cocktails.glass
         cocktailIbaCD.text = cocktails.iba
         instructionCD.text = cocktails.instruction
         cookTimeCD.text = cocktails.cooktime

         adapter?.setList(cocktails.ingredients!!.subList(1, cocktails.ingredients!!.size))*/
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuInfo = menu.findItem(R.id.commonToolbarInfo)
        val menuS = menu.findItem(R.id.commonToolbarSort)
        val menuF = menu.findItem(R.id.commonToolbarSearch)

        menuInfo.isVisible = true
        menuS.isVisible = false
        menuF.isVisible = false

        menuInfo.setOnMenuItemClickListener {
            Toast.makeText(activity, "Info", Toast.LENGTH_SHORT).show()
            return@setOnMenuItemClickListener true
        }

        super.onCreateOptionsMenu(menu, inflater)

    }

    private fun setData(cocktails: Cocktails) {
        Glide.with(this).load(cocktails.image).centerCrop()
            .into(cocktailImage)
        cocktailCategoryCD.text = cocktails.category?.name
        cocktailGlassCD.text = cocktails.glass
        cocktailIbaCD.text = cocktails.iba
        instructionCD.text = cocktails.instruction
        cookTimeCD.text = cocktails.cooktime

        commonToolbarTitle.text = cocktails.name

        adapter?.setList(cocktails.ingredients!!.subList(1, cocktails.ingredients!!.size))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}