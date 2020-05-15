package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coctails.R
import com.example.coctails.interfaces.OnIngredientDataChanged
import com.example.coctails.interfaces.OnRecyclerIconClick
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.ingredients_details.IngredientDetailsFragment
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.adapters.AllIngredientRecyclerAdapter
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.adapters.SearchIngredientRecyclerView
import com.example.coctails.ui.screens.fragments.workspace.intefraces.OnSearchItemClick
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.model.IngredientModelSelection
import com.example.coctails.utils.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.fragment_ingredients_w.*

class IngredientsWSFragment(private val subject: PublisherSubject) :
    BaseFragment<IngredientsWSPresenter, IngredientsWSView>(),
    IngredientsWSView, OnRecyclerItemClick,
    OnSearchItemClick, OnRecyclerIconClick, OnIngredientDataChanged {

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

        subject.listen().subscribe(getInputObserver())
    }

    private fun setupRecycler() {
        mLayoutManager = GridLayoutManager(context, 3)
        allIngredientsRecycler.layoutManager = mLayoutManager
        allIngredientsRecycler.setHasFixedSize(true)
        adapter = AllIngredientRecyclerAdapter(this, this)
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

    private fun getInputObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: String) {
                if(t == CHANGED_FROM_INGREDIENT){
                    presenter.getIngredientListFromDB()
                }
            }

            override fun onError(e: Throwable) {}
        }
    }

    override fun showResult(ingredientList: List<IngredientModelSelection>) {
        adapter?.setList(ingredientList)
        mainFab.visibility = View.VISIBLE
        commonProgressBar.visibility = View.GONE
    }

    override fun onItemClick(position: Int) {
        startDetailsFragment(adapter?.getAdapterList()?.get(position)!!)
    }

    override fun onIconClick(position: Int, status: Boolean) {
        val ingredient = adapter?.getAdapterList()?.get(position)
        presenter.setIngredientToDB(ingredient?.ingredientId!!, ingredient.category)
    }

    override fun successChanges() {
        subject.publish(CHANGED_FROM_ALL)
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
                    val searchList = mutableListOf<IngredientModelSelection>()
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

    override fun onSearchIconClick(position: Int) {
        val ingredient = searchAdapter?.getAdapterList()?.get(position)
        presenter.setIngredientToDB(ingredient?.ingredientId!!, ingredient.category)
        adapter?.getAdapterList()?.forEachIndexed { index, element ->
            if (ingredient.category == element.category && ingredient.ingredientId == element.ingredientId) {
                adapter?.resetDataItem(index)
                adapter?.notifyItemChanged(index)
            }
        }
    }

    override fun dataIsChanged(isChanged: Boolean, ingredientId: Int, category: String) {
        adapter?.getAdapterList()?.forEachIndexed { index, element ->
            if (category == element.category && ingredientId == element.ingredientId && element.isSelected != isChanged) {
                adapter?.resetDataItemByInterface(index, isChanged)
                adapter?.notifyItemChanged(index)
            }
        }
    }

    fun newInstance(index: Int, subject: PublisherSubject): IngredientsWSFragment {
        val fragment = IngredientsWSFragment(subject)
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
