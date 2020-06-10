package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coctails.R
import com.example.coctails.core.App
import com.example.coctails.interfaces.OnIngredientDataChanged
import com.example.coctails.interfaces.OnRecyclerIconClick
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.cocktaildetails.model.IngredientModelCD
import com.example.coctails.ui.screens.fragments.ingredients_details.IngredientDetailsFragment
import com.example.coctails.ui.screens.fragments.workspace.intefraces.OnSearchItemClick
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.adapters.AllIngredientRecyclerAdapter
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.adapters.SearchIngredientRecyclerView
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.model.IngredientModelSelection
import com.example.coctails.utils.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.fragment_ingredients_w.*
import kotlinx.android.synthetic.main.fragment_work_space.*

class IngredientsWSFragment :
    BaseFragment<IngredientsWSPresenter, IngredientsWSView>(),
    IngredientsWSView, OnRecyclerItemClick,
    OnSearchItemClick, OnRecyclerIconClick, OnIngredientDataChanged {

    private var activity: MainActivity? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var mSearchLayoutManager: LinearLayoutManager? = null
    private var adapter: AllIngredientRecyclerAdapter? = null
    private var searchAdapter: SearchIngredientRecyclerView? = null
    private var searchDialog: Dialog? = null
    private var sortDialog: Dialog? = null
    private var filterResult: TextView? = null
    private var filterResetLayout: LinearLayout? = null
    private var searchResList = mutableListOf<IngredientModelSelection>()

    private var fabOpenAnim: Animation? = null
    private var fabCloseAnim: Animation? = null
    private var forwardAnim: Animation? = null
    private var backwardAnim: Animation? = null

    private var isOpenFab = false

    private var selectedSort = 1
    private var selectedShown = 0

    private var count = 0

    override fun getLayoutId(): Int = R.layout.fragment_ingredients_w

    override fun providePresenter(): IngredientsWSPresenter = IngredientsWSPresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)
        presenter.getIngredientList()

        openSortDialog()
        openSearchDialog()

        setupRecycler()
        mainFab.setOnClickListener {
            openFabAnimation()
        }

        searchIngredients.setOnClickListener {
            presenter.getListForSearch()
        }

        sortIngredient.setOnClickListener {
            sortDialog?.show()
        }

        fabOpenAnim = AnimationUtils.loadAnimation(context, R.anim.fab_open)
        fabCloseAnim = AnimationUtils.loadAnimation(context, R.anim.fab_close)
        forwardAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_forward)
        backwardAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_backward)

        App.instanse?.subject?.listenChange()?.subscribe(getInputObserver())
    }

    private fun setupRecycler() {
        mLayoutManager = GridLayoutManager(context, 3)
        allIngredientsRecycler.layoutManager = mLayoutManager
        allIngredientsRecycler.setHasFixedSize(true)
        adapter = AllIngredientRecyclerAdapter(this, this)
        allIngredientsRecycler.adapter = adapter

        allIngredientsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (mainFab != null) {
                    if (dy > 0 && mainFab.isShown) {
                        mainFab?.hide()
                        if (isOpenFab) {
                            openFabAnimation()
                        }
                        mainFab.isClickable = false
                    } else {
                        mainFab.show()
                        mainFab.isClickable = true
                    }

                    super.onScrolled(recyclerView, dx, dy)
                }
            }
        })
    }

    private fun openFabAnimation() {
        isOpenFab = if (isOpenFab) {
            mainFab.startAnimation(backwardAnim)
            searchIngredients.startAnimation(fabCloseAnim)
            sortIngredient.startAnimation(fabCloseAnim)

            searchIngredients.visibility = View.GONE
            sortIngredient.visibility = View.GONE
            searchIngredients.isClickable = false
            sortIngredient.isClickable = false
            false
        } else {
            searchIngredients.visibility = View.VISIBLE
            sortIngredient.visibility = View.VISIBLE

            mainFab.startAnimation(forwardAnim)
            searchIngredients.startAnimation(fabOpenAnim)
            sortIngredient.startAnimation(fabOpenAnim)

            searchIngredients.isClickable = true
            sortIngredient.isClickable = true
            true
        }
    }

    private fun getInputObserver(): Observer<IngredientModelCD> {
        return object : Observer<IngredientModelCD> {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(ingredient: IngredientModelCD) {
                adapter?.getAdapterList()?.forEachIndexed { index, item ->
                    if (item.category == ingredient.category && item.ingredientId == ingredient.ingredientId) {
                        adapter?.resetDataItem(index, ingredient.isSelected)
                        adapter?.notifyItemChanged(index)
                        searchAdapter?.clearList()
                    }
                }
            }

            override fun onError(e: Throwable) {}
        }
    }

    override fun showResult(ingredientList: List<IngredientModelSelection>, size: Int) {
        adapter?.setList(ingredientList)
        mainFab.visibility = View.VISIBLE
        commonProgressBar.visibility = View.GONE
        count = size

        setBadgeNumber()

        if (selectedSort == 2) {
            adapter?.setSortedByAbvList()
        }

        filterResult?.text = ingredientList.size.toString()
        presenter.getSortItems(selectedShown, false)
    }

    override fun showSortResult(showResult: List<IngredientModelSelection>) {
        adapter?.setList(showResult)
        mainFab.visibility = View.VISIBLE
        commonProgressBar.visibility = View.GONE

        if (selectedSort == 2) {
            adapter?.setSortedByAbvList()
        }

        filterResult?.text = showResult.size.toString()
    }

    override fun onItemClick(position: Int) {
        startDetailsFragment(adapter?.getAdapterList()?.get(position)!!)
    }

    override fun onIconClick(position: Int, status: Boolean) {
        val ingredient = adapter?.getAdapterList()?.get(position)
        presenter.setIngredientToDB(ingredient?.ingredientId!!, ingredient.category)

        if (status) {
            count++
        } else {
            count--
        }

        setBadgeNumber()
    }

    private fun setBadgeNumber() {
        if (count > 0) {
            activity?.kitchenTabs?.getTabAt(0)?.orCreateBadge?.badgeTextColor =
                ContextCompat.getColor(context!!, R.color.white)
            activity?.kitchenTabs?.getTabAt(0)?.orCreateBadge?.number = count
        } else {
            activity?.kitchenTabs?.getTabAt(0)?.removeBadge()
        }
    }

    override fun successChanges() {
        App.instanse?.subject?.publish(CHANGED_FROM_ALL)
    }

    private fun openSearchDialog() {
        searchDialog = Dialog(context!!)
        searchDialog?.setContentView(R.layout.dialog_search)
        searchDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val searchEditText = searchDialog?.findViewById<EditText>(R.id.searchEditText)
        val searchRecyclerView = searchDialog?.findViewById<RecyclerView>(R.id.searchRecyclerView)
        searchFun(searchEditText!!, searchRecyclerView!!)
    }

    private fun openSortDialog() {
        sortDialog = Dialog(context!!, R.style.CustomDialogUp)
        sortDialog?.setContentView(R.layout.dialog_sort_ingredient)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        sortDialog?.window?.setLayout(width, height)
        sortDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val filterCancel = sortDialog?.findViewById(R.id.filterCancel) as ImageView
        val filterApply = sortDialog?.findViewById(R.id.filterApply) as ImageView
        val filterReset = sortDialog?.findViewById(R.id.filterReset) as TextView
        filterResult = sortDialog?.findViewById(R.id.filterResult) as TextView

        filterCancel.setOnClickListener {
            sortDialog?.dismiss()
        }

        filterApply.setOnClickListener {
            sortDialog?.dismiss()
        }

        val filterAlcohol = sortDialog?.findViewById(R.id.filterAlcohol) as TextView
        val filterDecoration = sortDialog?.findViewById(R.id.filterDecoration) as TextView
        val filterFruits = sortDialog?.findViewById(R.id.filterFruits) as TextView
        val filterLiqueur = sortDialog?.findViewById(R.id.filterLiqueur) as TextView
        val filterJuice = sortDialog?.findViewById(R.id.filterJuice) as TextView
        val filterOther = sortDialog?.findViewById(R.id.filterOther) as TextView

        var isSelectedAlc = false
        filterAlcohol.setOnClickListener {
            selectedView(filterAlcohol, 0, isSelectedAlc)
            isSelectedAlc = !isSelectedAlc
        }

        var isSelectedLiq = false
        filterLiqueur.setOnClickListener {
            selectedView(filterLiqueur, 1, isSelectedLiq)
            isSelectedLiq = !isSelectedLiq
        }

        var isSelectedFruit = false
        filterFruits.setOnClickListener {
            selectedView(filterFruits, 2, isSelectedFruit)
            isSelectedFruit = !isSelectedFruit
        }

        var isSelectedJuice = false
        filterJuice.setOnClickListener {
            selectedView(filterJuice, 3, isSelectedJuice)
            isSelectedJuice = !isSelectedJuice
        }

        var isSelectedDecoration = false
        filterDecoration.setOnClickListener {
            selectedView(filterDecoration, 4, isSelectedDecoration)
            isSelectedDecoration = !isSelectedDecoration
        }

        var isSelectedOther = false
        filterOther.setOnClickListener {
            selectedView(filterOther, 5, isSelectedOther)
            isSelectedOther = !isSelectedOther
        }

        val filterAllIngredient = sortDialog?.findViewById(R.id.filterAllIngredient) as LinearLayout
        val filterOnlyMy = sortDialog?.findViewById(R.id.filterOnlyMy) as LinearLayout
        val filterNotSelected = sortDialog?.findViewById(R.id.filterNotSelected) as LinearLayout
        filterResetLayout = sortDialog?.findViewById(R.id.filterResetLayout) as LinearLayout

        val filterAllCheck = sortDialog?.findViewById(R.id.filterAllCheck) as ImageView
        val filterMyCheck = sortDialog?.findViewById(R.id.filterMyCheck) as ImageView
        val filterNotSelectedCheck =
            sortDialog?.findViewById(R.id.filterNotSelectedCheck) as ImageView

        val showList = mutableMapOf<View, View>()
        showList[filterAllIngredient] = filterAllCheck
        showList[filterOnlyMy] = filterMyCheck
        showList[filterNotSelected] = filterNotSelectedCheck


        filterAllIngredient.setOnClickListener {
            sortSelected(showList, filterAllIngredient)
            selectedShown = 0
            presenter.getSortItems(selectedShown, false)
        }

        filterOnlyMy.setOnClickListener {
            sortSelected(showList, filterOnlyMy)
            selectedShown = 1
            presenter.getSortItems(selectedShown, false)

        }

        filterNotSelected.setOnClickListener {
            sortSelected(showList, filterNotSelected)
            selectedShown = 2
            presenter.getSortItems(selectedShown, false)
        }

        val filterSortName = sortDialog?.findViewById(R.id.filterSortName) as LinearLayout
        val filterSortAbv = sortDialog?.findViewById(R.id.filterSortAbv) as LinearLayout

        val sortNameCheck = sortDialog?.findViewById(R.id.sortNameCheck) as ImageView
        val sortAbvCheck = sortDialog?.findViewById(R.id.sortAbvCheck) as ImageView

        val sortList = mutableMapOf<View, View>()
        sortList[filterSortName] = sortNameCheck
        sortList[filterSortAbv] = sortAbvCheck

        filterSortName.setOnClickListener {
            sortSelected(sortList, filterSortName)
            adapter?.setSortedByNameList()
            selectedSort = 1
        }

        filterSortAbv.setOnClickListener {
            sortSelected(sortList, filterSortAbv)
            adapter?.setSortedByAbvList()
            selectedSort = 2
        }

        filterReset.setOnClickListener {
            resetView(filterAlcohol)
            isSelectedAlc = false

            resetView(filterDecoration)
            isSelectedDecoration = false

            resetView(filterFruits)
            isSelectedFruit = false

            resetView(filterLiqueur)
            isSelectedLiq = false

            resetView(filterJuice)
            isSelectedJuice = false

            resetView(filterOther)
            isSelectedOther = false

            sortSelected(sortList, filterSortName)
            selectedSort = 1

            sortSelected(showList, filterAllIngredient)
            selectedShown = 0

            presenter.removeCategorySort(6)

            filterResetLayout?.visibility = View.GONE
        }
    }

    private fun resetView(view: View) {
        view.background = activity?.getDrawable(R.drawable.white_borders)
    }

    private fun selectedView(view: View, position: Int, isSelected: Boolean) {
        if (!isSelected) {
            view.background = activity?.getDrawable(R.drawable.view_borders)
            presenter.addCategorySort(position)
            presenter.getSortItems(selectedShown, false)
        } else {
            view.background = activity?.getDrawable(R.drawable.white_borders)
            presenter.removeCategorySort(position)
            presenter.getSortItems(selectedShown, false)
        }

        filterResetLayout?.visibility = View.VISIBLE
    }

    private fun sortSelected(viewList: Map<View, View>, selectedView: View) {
        viewList.forEach { (view, image) ->
            if (view == selectedView) {
                view.background = activity?.getDrawable(R.drawable.view_borders)
                image.visibility = View.VISIBLE
            } else {
                view.background = null
                image.visibility = View.INVISIBLE
            }
        }

        filterResetLayout?.visibility = View.VISIBLE
    }


    private fun searchFun(editText: EditText, recyclerView: RecyclerView) {
        mSearchLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = mSearchLayoutManager
        recyclerView.setHasFixedSize(true)
        searchAdapter = SearchIngredientRecyclerView(this)
        recyclerView.adapter = searchAdapter

        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            @SuppressLint("DefaultLocale")
            override fun afterTextChanged(s: Editable) {
                if (s.length > 1) {
                    val query = TranslitUtils().cyr2lat(s.toString().toLowerCase().trim())
                    val searchList = mutableListOf<IngredientModelSelection>()
                    searchResList.forEach {
                        if (it.name.toLowerCase().trim().contains(query!!)) {
                            searchList.add(it)
                        }
                    }
                    searchAdapter?.setList(searchList)
                } else {
                    searchAdapter?.clearList()
                }
            }
        })
    }

    override fun showListForSearch(searchResult: List<IngredientModelSelection>) {
        searchResList.clear()
        searchResList.addAll(searchResult)
        searchDialog?.show()
    }

    override fun onSearchItemClick(position: Int) {
        startDetailsFragment(searchAdapter?.getAdapterList()?.get(position)!!)
        searchDialog?.dismiss()
    }

    private fun startDetailsFragment(ingredient: IngredientModelSelection) {
        val fragment = IngredientDetailsFragment(this)
        val bundle = Bundle()

        bundle.putString(INGREDIENT_CATEGORY, ingredient.category)
        bundle.putInt(INGREDIENT_ID, ingredient.ingredientId)
        fragment.arguments = bundle

        activity?.loadFragment(fragment, "IngredientDetails", true)
    }

    override fun onSearchIconClick(position: Int, status: Boolean) {
        val ingredient = searchAdapter?.getAdapterList()?.get(position)
        presenter.setIngredientToDB(ingredient?.ingredientId!!, ingredient.category)

        adapter?.getAdapterList()?.forEachIndexed { index, element ->
            if (ingredient.category == element.category && ingredient.ingredientId == element.ingredientId) {
                adapter?.resetDataItem(index, status)
                adapter?.notifyItemChanged(index)

                if (status) {
                    count++
                } else {
                    count--
                }

                setBadgeNumber()
            }
        }
    }

    override fun dataIsChanged(isChanged: Boolean, ingredientId: Int, category: String) {
        adapter?.getAdapterList()?.forEachIndexed { index, element ->
            if (category == element.category && ingredientId == element.ingredientId && element.isSelected != isChanged) {
                adapter?.resetDataItem(index, isChanged)
                adapter?.notifyItemChanged(index)

                App.instanse?.subject?.publish(CHANGED_FROM_ALL)

                if (isChanged) {
                    count++
                } else {
                    count--
                }

                setBadgeNumber()
            }
        }
    }

    fun newInstance(index: Int): IngredientsWSFragment {
        val fragment = IngredientsWSFragment()
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