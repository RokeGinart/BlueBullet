package com.example.coctails.ui.screens.fragments.cocktailscategory

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
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
import com.example.coctails.utils.TranslitUtils
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_cocktails_category.*


class CocktailsCategoryFragment : BaseFragment<CocktailsCategoryPresenter, CocktailsCategoryView>(),
    CocktailsCategoryView,
    OnRecyclerItemClick {

    private var mLayoutManager: GridLayoutManager? = null
    private var adapter: CocktailsRecyclerAdapter? = null
    private var cocktails: List<Cocktails>? = null
    private var activity: MainActivity? = null
    private var searchView: SearchView? = null
    private var category: String? = null
    private var titleTemp: String? = null
    private var listPopupWindow: ListPopupWindow? = null
    private var selectedSort = 0

    override fun getLayoutId(): Int = R.layout.fragment_cocktails_category

    override fun providePresenter(): CocktailsCategoryPresenter = CocktailsCategoryPresenterImpl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        category = bundle?.getString(CATEGORY)
        titleTemp = bundle?.getString(TOOLBAR_TITLE)

        category?.let { presenter.getCocktailsByCategory(it) }

        setupRecycler()
        setupPopupWindow()
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
        adapter?.setSortedByNameList()
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
        setHasOptionsMenu(true)
        activity?.supportActionBar?.title = null
        commonToolbarTitle.text = titleTemp
        commonToolbarBackPress.setOnClickListener { activity?.onBackPressed() }
        cocktailsCategoryLayout.setOnClickListener(null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val manager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val menuSearch = menu.findItem(R.id.commonToolbarSearch)
        val menuSort = menu.findItem(R.id.commonToolbarSort)

        menuSort.isVisible = true
        menuSearch.isVisible = true

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

            menuSort.setOnMenuItemClickListener {
                listPopupWindow?.show()
                return@setOnMenuItemClickListener true
            }


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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }

    private fun findCocktailByName(name: String) {
        if (name == "") {
            cocktails?.let { adapter?.setList(it) }
            setSortedList(selectedSort)
            errorMessage.visibility = View.GONE
        } else {
            val searchRes = mutableListOf<Cocktails>()
            val query = TranslitUtils().cyr2lat(name.toLowerCase().trim())
            cocktails?.forEach {
                if (it.name.toLowerCase().trim().contains(query.toString())) {
                    searchRes.add(it)
                }
                adapter?.setList(searchRes)
            }
        }
    }

    private fun setupPopupWindow() {
        listPopupWindow = activity?.let { ListPopupWindow(it) }

        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        listPopupWindow?.anchorView = commonToolbarPoint
        listPopupWindow?.width = metrics.widthPixels //sets width as per the screen.
        listPopupWindow?.height = ListPopupWindow.WRAP_CONTENT
        listPopupWindow?.isModal = true

        val filterLayout: View = layoutInflater.inflate(R.layout.custom_popup_menu, null)

        val nameSelected = filterLayout.findViewById(R.id.sortByName) as LinearLayout
        val alcSelected = filterLayout.findViewById(R.id.sortByAlcohol) as LinearLayout
        val timeSelected = filterLayout.findViewById(R.id.sortByTime) as LinearLayout

        val timeCheck = filterLayout.findViewById(R.id.timeCheck) as ImageView
        val nameCheck = filterLayout.findViewById(R.id.nameCheck) as ImageView
        val alcCheck = filterLayout.findViewById(R.id.alcCheck) as ImageView

     //   val apply = filterLayout.findViewById(R.id.sortApply) as TextView
      //  val sortCancel = filterLayout.findViewById(R.id.sortCancel) as TextView

        nameSelected.setOnClickListener {
            nameSelected.background = resources.getDrawable(R.drawable.view_borders)
            alcSelected.background = null
            timeSelected.background = null

            nameCheck.visibility = View.VISIBLE
            timeCheck.visibility = View.INVISIBLE
            alcCheck.visibility = View.INVISIBLE

            selectedSort = 0
            adapter?.setSortedByNameList()
        }

        alcSelected.setOnClickListener {
            alcSelected.background = resources.getDrawable(R.drawable.view_borders)
            nameSelected.background = null
            timeSelected.background = null

            alcCheck.visibility = View.VISIBLE
            nameCheck.visibility = View.INVISIBLE
            timeCheck.visibility = View.INVISIBLE

            selectedSort = 1
            adapter?.setSortedByAlcoholList()
        }

        timeSelected.setOnClickListener {
            timeSelected.background = resources.getDrawable(R.drawable.view_borders)
            alcSelected.background = null
            nameSelected.background = null

            timeCheck.visibility = View.VISIBLE
            nameCheck.visibility = View.INVISIBLE
            alcCheck.visibility = View.INVISIBLE

            selectedSort = 2
            adapter?.setSortedByTime()
        }

        listPopupWindow?.setPromptView(filterLayout)
    }

    private fun setSortedList(sort: Int) {
        when (sort) {
            0 -> {
                adapter?.setSortedByNameList()
            }
            1 -> {
                adapter?.setSortedByAlcoholList()
            }
            2 -> {
                adapter?.setSortedByTime()
            }
        }
    }
}
