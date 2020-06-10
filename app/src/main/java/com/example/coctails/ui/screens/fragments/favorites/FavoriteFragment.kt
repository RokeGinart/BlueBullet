package com.example.coctails.ui.screens.fragments.favorites

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coctails.R
import com.example.coctails.core.App
import com.example.coctails.core.room.entity.FavoriteModel
import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import com.example.coctails.interfaces.OnRecyclerIconClick
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.cocktaildetails.CocktailDetails
import com.example.coctails.ui.screens.fragments.favorites.adapters.FavoriteRecyclerAdapter
import com.example.coctails.ui.screens.fragments.favorites.model.FavoriteSubjectModel
import com.example.coctails.utils.COCKTAIL
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_favorite.*


class FavoriteFragment : BaseFragment<FavoritePresenter, FavoriteView>(), FavoriteView,
    OnRecyclerItemClick, OnRecyclerIconClick {

    private var activity: MainActivity? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var adapter: FavoriteRecyclerAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_favorite

    override fun providePresenter(): FavoritePresenter = FavoritePresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)
        presenter.getFavoriteList()

        setupRecycler()

        favoriteRefresh.setOnRefreshListener{
            presenter.getFavoriteList()
        }

        App.instanse?.subject?.listenFavoriteChange()?.subscribe(getInputObserver())
    }

    private fun setupRecycler() {
        mLayoutManager = LinearLayoutManager(context)
        favoriteRecycler.layoutManager = mLayoutManager
        favoriteRecycler.setHasFixedSize(true)
        adapter = FavoriteRecyclerAdapter(this, this)
        favoriteRecycler.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        commonToolbarBackPress?.visibility = View.GONE
        commonToolbarTitle?.text = getString(R.string.favorites)
    }

    private fun getInputObserver(): Observer<FavoriteSubjectModel> {
        return object : Observer<FavoriteSubjectModel> {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(action: FavoriteSubjectModel) {
                adapter?.getAdapterList()?.forEachIndexed{ index, item ->
                    if(item?.cocktailId == action.id && item?.category == action.category){
                        adapter?.resetDataItem(index, action.isSelected)
                        adapter?.notifyItemChanged(index)
                    }
                }
            }

            override fun onError(e: Throwable) {}
        }
    }

    override fun showFavoriteList(favoriteModel: List<FavoriteModel>) {
        commonProgressBar.visibility = View.GONE
        favoriteMessage.visibility = View.GONE
        favoriteRefresh.isRefreshing = false
        adapter?.setList(favoriteModel.reversed())
    }

    override fun showMessage() {
        commonProgressBar.visibility = View.GONE
        favoriteMessage.visibility = View.VISIBLE
        favoriteRefresh.isRefreshing = false
    }

    override fun onItemClick(position: Int) {
        commonProgressBar.visibility = View.VISIBLE
        presenter.getSelectedCocktail(
            adapter?.getAdapterList()?.get(position)?.category!!,
            adapter?.getAdapterList()?.get(position)?.cocktailId!!
        )
    }

    override fun onIconClick(position: Int, status: Boolean) {
        val favoriteModel = adapter?.getAdapterList()?.get(position)
        presenter.setFavoriteStatus(favoriteModel?.cocktailId!!, favoriteModel.name, favoriteModel.image, favoriteModel.category, favoriteModel.abv, favoriteModel.categoryName, favoriteModel.favorite)
    }

    override fun getCocktail(cocktails: CocktailFirebaseData) {
        commonProgressBar.visibility = View.GONE

        val fragment = CocktailDetails()
        val bundle = Bundle()

        bundle.putSerializable(COCKTAIL, cocktails)
        fragment.arguments = bundle

        activity?.loadFragment(fragment, "CocktailDetails", true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
