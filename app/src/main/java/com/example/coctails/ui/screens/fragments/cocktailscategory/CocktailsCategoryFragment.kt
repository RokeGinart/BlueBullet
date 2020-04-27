package com.example.coctails.ui.screens.fragments.cocktailscategory

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.cocktaildetails.CocktailDetails
import com.example.coctails.ui.screens.fragments.cocktailscategory.adapters.CocktailsRecyclerAdapter
import com.example.coctails.utils.CATEGORY
import com.example.coctails.utils.COCKTAIL
import com.example.coctails.utils.TOOLBAR_TITLE
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_cocktails_category.*

class CocktailsCategoryFragment : BaseFragment<CocktailsCategoryPresenter, CocktailsCategoryView>(), CocktailsCategoryView,
    OnRecyclerItemClick {

    private var mLayoutManager: GridLayoutManager? = null
    private var adapter: CocktailsRecyclerAdapter? = null
    private var cocktails: List<Cocktails>? = null
    private var activity: MainActivity? = null
    private var searchView: SearchView? = null
    private var category: String? = null
    private var titleTemp: String? = null

    override fun getLayoutId(): Int = R.layout.fragment_cocktails_category

    override fun providePresenter(): CocktailsCategoryPresenter = CocktailsCategoryPresenterImpl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        category = bundle?.getString(CATEGORY)
        titleTemp = bundle?.getString(TOOLBAR_TITLE)

        category?.let { presenter.getCocktailsByCategory(it) }

        setHasOptionsMenu(true)

        setupRecycler()
    }

    private fun setupRecycler() {
        mLayoutManager = GridLayoutManager(context, 2)
        cocktailRecycler.layoutManager = mLayoutManager
        cocktailRecycler.setHasFixedSize(true)
        adapter = CocktailsRecyclerAdapter(this)
        cocktailRecycler.adapter = adapter
    }

    override fun showCocktailsCategory(cocktailsList: List<Cocktails>) {
        adapter?.setList(cocktailsList)
        cocktails = cocktailsList

        progressCategory.visibility = View.GONE
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        activity?.setSupportActionBar(commonToolbar)
        activity?.supportActionBar?.title = null
        commonToolbarTitle.text = titleTemp
        commonToolbarBackPress.setOnClickListener { activity?.onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val manager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val menuSearch = menu.findItem(R.id.commonToolbarSearch)
        val menuInfo = menu.findItem(R.id.commonToolbarInfo)

        menuInfo.isVisible = false

        if (menuSearch != null) {

            searchView = menuSearch.actionView as SearchView
            searchView?.setSearchableInfo(manager.getSearchableInfo(activity?.componentName))
            searchView?.queryHint = "Поиск..."

            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView?.clearFocus()
                    searchView?.setQuery("", false)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    findCocktailByName(newText)
                    return true
                }
            })

            super.onCreateOptionsMenu(menu, inflater)
        }
    }

    override fun onItemClick(position: Int) {
        val fragment = CocktailDetails()
        val bundle = Bundle()

        bundle.putSerializable(COCKTAIL, adapter?.getAdapterList()?.get(position))
        fragment.arguments = bundle

        activity?.loadFragment(fragment, "CocktailDetails", true)
        searchView?.clearFocus()
        setHasOptionsMenu(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }

    private fun findCocktailByName(name: String) {
        adapter?.clearAdapter()

        if (name == "") {
            cocktails?.let { adapter?.setList(it) }
            errorMessage.visibility = View.GONE
        } else {
            val searchRes = mutableListOf<Cocktails>()
            val query = name.toLowerCase().trim()
            cocktails?.forEach {
                if (it.name.toLowerCase().trim().contains(query)) {
                    searchRes.add(it)
                }
                adapter?.setList(searchRes)
            }
        }
    }
}


