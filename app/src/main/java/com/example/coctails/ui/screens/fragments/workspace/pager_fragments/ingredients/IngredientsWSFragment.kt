package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.firebase.drink.IngredientsModel
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.ingredients_details.IngredientDetailsFragment
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.adapters.AllIngredientRecyclerAdapter
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.adapters.SearchIngredientRecyclerView
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.interfaces.OnSearchItemClick
import com.example.coctails.utils.*
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.fragment_ingredients_w.*

class IngredientsWSFragment : BaseFragment<IngredientsWSPresenter, IngredientsWSView>(),
    IngredientsWSView, OnRecyclerItemClick, OnSearchItemClick {

    private var activity: MainActivity? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var mSearchLayoutManager: LinearLayoutManager? = null
    private var adapter: AllIngredientRecyclerAdapter? = null
    private var searchAdapter: SearchIngredientRecyclerView? = null
    private var searchDialog: Dialog? = null

    private var fabOpenAnim: Animation? = null
    private var fabCloseAnim: Animation? = null
    private var forwardAnim: Animation? = null
    private var backwardAnim: Animation? = null

    private var isOpenFab = false

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

        setupRecycler()
        mainFab.setOnClickListener {
            openFabAnimation()
        }

        searchIngredients.setOnClickListener {
            openSearchDialog()
        }

        fabOpenAnim = AnimationUtils.loadAnimation(context, R.anim.fab_open)
        fabCloseAnim = AnimationUtils.loadAnimation(context, R.anim.fab_close)
        forwardAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_forward)
        backwardAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_backward)
    }

    private fun setupRecycler() {
        mLayoutManager = GridLayoutManager(context, 3)
        allIngredientsRecycler.layoutManager = mLayoutManager
        allIngredientsRecycler.setHasFixedSize(true)
        adapter = AllIngredientRecyclerAdapter(this)
        allIngredientsRecycler.adapter = adapter

        allIngredientsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && mainFab.isShown) {
                    mainFab.hide()
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

    override fun showResult(ingredientList: List<IngredientsModel>) {
        adapter?.setList(ingredientList)
        mainFab.visibility = View.VISIBLE
        commonProgressBar.visibility = View.GONE
    }

    override fun onItemClick(position: Int) {
        val fragment = IngredientDetailsFragment()
        val bundle = Bundle()

        val ingredient = adapter?.getAdapterList()?.get(position)

        bundle.putString(INGREDIENT_CATEGORY, ingredient?.category?.category)
        bundle.putInt(INGREDIENT_ID, ingredient?.id!!)

        fragment.arguments = bundle

        activity?.loadFragment(fragment, "IngredientDetails", true)
    }

    private fun openSearchDialog() {
        searchDialog = Dialog(context!!)
        searchDialog?.setContentView(R.layout.dialog_search)
        searchDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val searchEditText = searchDialog?.findViewById<EditText>(R.id.searchEditText)
        val searchRecyclerView = searchDialog?.findViewById<RecyclerView>(R.id.searchRecyclerView)
        searchFun(searchEditText!!, searchRecyclerView!!)
        searchDialog?.show()
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

            override fun afterTextChanged(s: Editable) {
                if (s.length > 1) {
                    val query = TranslitUtils().cyr2lat(s.toString().toLowerCase().trim())
                    val searchList = mutableListOf<IngredientsModel>()
                    adapter?.getAdapterList()?.forEach {
                        if (it.name.toLowerCase().trim().contains(query!!)) {
                            searchList.add(it)
                        }
                    }
                    searchAdapter?.setList(searchList)
                }
            }
        })
    }

    override fun onSearchItemClick(position: Int) {
        val fragment = IngredientDetailsFragment()
        val bundle = Bundle()

        val ingredient = searchAdapter?.getAdapterList()?.get(position)

        bundle.putString(INGREDIENT_CATEGORY, ingredient?.category?.category)
        bundle.putInt(INGREDIENT_ID, ingredient?.id!!)
        fragment.arguments = bundle

        activity?.loadFragment(fragment, "IngredientDetails", true)

        searchDialog?.dismiss()
    }

    override fun onSearchIconClick(position: Int) {

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
