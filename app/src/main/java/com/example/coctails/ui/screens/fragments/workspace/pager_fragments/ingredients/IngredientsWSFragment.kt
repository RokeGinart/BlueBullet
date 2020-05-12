package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.firebase.drink.IngredientsModel
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.ingredients_details.IngredientDetailsFragment
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.adapters.AllIngredientRecyclerAdapter
import com.example.coctails.utils.ARG_SECTION_NUMBER
import com.example.coctails.utils.INGREDIENT_CATEGORY
import com.example.coctails.utils.INGREDIENT_ID
import kotlinx.android.synthetic.main.fragment_ingredients_w.*


class IngredientsWSFragment : BaseFragment<IngredientsWSPresenter, IngredientsWSView>(),
    IngredientsWSView, OnRecyclerItemClick {

    private var activity: MainActivity? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var adapter: AllIngredientRecyclerAdapter? = null

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
    }

    private fun setupRecycler() {
        mLayoutManager = GridLayoutManager(context, 3)
        allIngredientsRecycler.layoutManager = mLayoutManager
        allIngredientsRecycler.setHasFixedSize(true)
        adapter = AllIngredientRecyclerAdapter(this)
        allIngredientsRecycler.adapter = adapter

        allIngredientsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && searchIngredients.isShown) {
                    searchIngredients.hide()
                } else {
                    searchIngredients.show()
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    override fun showResult(ingredientList: List<IngredientsModel>) {
        adapter?.setList(ingredientList)
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
