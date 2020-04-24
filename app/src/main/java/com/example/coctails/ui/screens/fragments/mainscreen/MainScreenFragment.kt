package com.example.coctails.ui.screens.fragments.mainscreen

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.CocktailsCategoryList
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.cocktaildetails.CocktailDetails
import com.example.coctails.ui.screens.fragments.mainscreen.adapters.CocktailsRecyclerAdapter
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_main_screen.*

class MainScreenFragment : BaseFragment<MainScreenPresenter, MainScreenView>(), MainScreenView,
    OnRecyclerItemClick {

    private var mLayoutManager: GridLayoutManager? = null
    private var adapter: CocktailsRecyclerAdapter? = null
    private var cocktails: List<CocktailsCategoryList.Drink>? = null
    private var searchRes: List<CocktailsCategoryList.Drink>? = null
    private var intentArray: List<CocktailsCategoryList.Drink>? = null
    private var activity: MainActivity? = null
    private var searchView : SearchView? = null

    override fun getLayoutId(): Int = R.layout.fragment_main_screen

    override fun providePresenter(): MainScreenPresenter = MainScreenPresenterImpl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)
        //presenter.getCocktailsList("Cocktail")

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        activity?.setSupportActionBar(commonToolbar)
        activity?.supportActionBar?.title = null
        commonToolbarTitle.text = "Cocktails"
        commonToolbarBackPress.visibility = View.GONE
    }

    override fun showAllCocktails(cocktailsCategoryList: List<CocktailsCategoryList.Drink>?) {
        if (cocktailsCategoryList != null) {
            adapter?.setList(cocktailsCategoryList)
            cocktails = cocktailsCategoryList
            intentArray = cocktailsCategoryList
        }
    }

    override fun showSearchResult(cocktailsSearch: List<CocktailsCategoryList.Drink>?) {
        if (cocktailsSearch != null) {
            adapter?.setList(cocktailsSearch)
            searchRes = cocktailsSearch
            intentArray = cocktailsSearch
            errorMessage.visibility = View.GONE
        } else {
            adapter?.clearAdapter()
            errorMessage.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val manager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val menuSearch = menu.findItem(R.id.commonToolbarSearch)

        if (menuSearch != null) {

            searchView = menuSearch.actionView as SearchView
            searchView?.setSearchableInfo(manager.getSearchableInfo(activity?.componentName))
            searchView?.queryHint = "Search..."

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

        bundle.putString("COCKTAIL", intentArray?.get(position)?.idDrink!!)
        fragment.arguments = bundle

        activity?.loadFragment(fragment, "Details", true)
        searchView?.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }

    private fun findCocktailByName(name : String){
        if(name == ""){
            adapter?.clearAdapter()
            adapter?.setList(cocktails!!)
            intentArray = cocktails
            errorMessage.visibility = View.GONE
        } else {
            adapter?.clearAdapter()
            presenter.getSearchCocktailsList(name)
        }
    }
}