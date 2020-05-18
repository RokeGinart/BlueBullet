package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.cocktails

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.cocktaildetails.CocktailDetails
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.cocktails.adapters.ReadyCocktailsRecyclerAdapter
import com.example.coctails.utils.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_cocktails_ws.*
import kotlinx.android.synthetic.main.fragment_work_space.*

class CocktailsWSFragment(private val subject: PublisherSubject) :
    BaseFragment<CocktailsWSPresenter, CocktailsWSView>(), CocktailsWSView,
    OnRecyclerItemClick {

    private var activity: MainActivity? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var adapter: ReadyCocktailsRecyclerAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_cocktails_ws

    override fun providePresenter(): CocktailsWSPresenter = CocktailsWSPresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)
        presenter.getAllCocktails()

        subject.listen().subscribe(getInputObserver())

        setupRecycler()
    }

    private fun setupRecycler() {
        mLayoutManager = GridLayoutManager(context, 2)
        readyCocktailRecycler.layoutManager = mLayoutManager
        readyCocktailRecycler.setHasFixedSize(true)
        adapter = ReadyCocktailsRecyclerAdapter(this)

        readyCocktailRecycler.adapter = adapter
    }

    override fun showResult(cocktails: List<Cocktails>) {
        readyCocktailMessage.visibility = View.GONE
        adapter?.setList(cocktails)
        if (cocktails.isNotEmpty()) {
            activity?.kitchenTabs?.getTabAt(1)?.orCreateBadge?.badgeTextColor = getColor(context!!, R.color.white)
            activity?.kitchenTabs?.getTabAt(1)?.orCreateBadge?.number = cocktails.size
        } else {
            activity?.kitchenTabs?.getTabAt(1)?.removeBadge()
        }
    }

    override fun showMessage() {
        readyCocktailMessage.visibility = View.VISIBLE
    }

    override fun onItemClick(position: Int) {
        val fragment = CocktailDetails()
        val bundle = Bundle()

        bundle.putSerializable(COCKTAIL, adapter?.getAdapterList()?.get(position))
        bundle.putSerializable(INGREDIENT_INTERFACE, subject)
        fragment.arguments = bundle

        activity?.loadFragment(fragment, "CocktailDetails", true)
    }

    private fun getInputObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: String) {
                if (t == CHANGED_FROM_ALL || t == CHANGED_FROM_INGREDIENT) {
                    presenter.showReadyCocktails()
                }
            }

            override fun onError(e: Throwable) {}
        }
    }

    fun newInstance(index: Int, subject: PublisherSubject): CocktailsWSFragment {
        val fragment = CocktailsWSFragment(subject)
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
