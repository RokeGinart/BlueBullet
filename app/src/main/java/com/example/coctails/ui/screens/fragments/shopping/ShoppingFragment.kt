package com.example.coctails.ui.screens.fragments.shopping

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coctails.R
import com.example.coctails.core.App
import com.example.coctails.core.room.entity.Shopping
import com.example.coctails.interfaces.OnIngredientDataChanged
import com.example.coctails.interfaces.OnRecyclerIconClick
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.firebase.drink.GlassDetails
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.equipment_details.EquipmentDetailsFragment
import com.example.coctails.ui.screens.fragments.glassdetails.GlassFragment
import com.example.coctails.ui.screens.fragments.guide_detail.GuideDetailsFragment
import com.example.coctails.ui.screens.fragments.ingredients_details.IngredientDetailsFragment
import com.example.coctails.ui.screens.fragments.shopping.adapters.ShoppingRecyclerViewAdapter
import com.example.coctails.ui.screens.fragments.shopping.model.ItemChange
import com.example.coctails.utils.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_shopping.*


class ShoppingFragment : BaseFragment<ShoppingPresenter, ShoppingView>(), ShoppingView,
    OnRecyclerItemClick, OnRecyclerIconClick, OnIngredientDataChanged {

    private var activity: MainActivity? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var adapter: ShoppingRecyclerViewAdapter? = null
    private var fragment: Fragment? = null

    override fun getLayoutId(): Int = R.layout.fragment_shopping

    override fun providePresenter(): ShoppingPresenter = ShoppingPresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)
        presenter.getAllShoppingItems()

        setupRecycler()

        shoppingRefresh.setOnRefreshListener{
            presenter.getAllShoppingItems()
        }

        App.instanse?.subject?.listenShoppingChange()?.subscribe(getInputObserver())
    }

    private fun setupRecycler() {
        mLayoutManager = LinearLayoutManager(context)
        shoppingRecyclerView.layoutManager = mLayoutManager
        shoppingRecyclerView.setHasFixedSize(true)
        adapter = ShoppingRecyclerViewAdapter(this, this)
        shoppingRecyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        commonToolbarTitle.text = getString(R.string.buy)
        commonToolbarBackPress.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun getInputObserver(): Observer<ItemChange> {
        return object : Observer<ItemChange> {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(item: ItemChange) {
               adapter?.getAdapterList()?.forEachIndexed{index, shopping ->
                   if(shopping.category == item.category && shopping.itemId == item.itemId){
                       adapter?.resetDataItem(index, item.selection)
                       adapter?.notifyItemChanged(index)
                   }
               }
            }

            override fun onError(e: Throwable) {}
        }
    }

    override fun showResult(shopping: List<Shopping>) {
        commonProgressBar.visibility = View.GONE
        shoppingMessage.visibility = View.GONE
        shoppingRefresh.isRefreshing = false
        adapter?.setList(shopping.reversed())
    }

    override fun showMessage() {
        commonProgressBar.visibility = View.GONE
        shoppingMessage.visibility = View.VISIBLE
        shoppingRefresh.isRefreshing = false
    }

    override fun onItemClick(position: Int) {
        val item = adapter?.getAdapterList()?.get(position)
        var tag = ""
        val bundle = Bundle()

        when (item?.mainCategory) {
            "glass" -> {
                fragment = GlassFragment()
                bundle.putInt(GLASS_ID, item.itemId)
                tag = "GlassDetails"
                fragment?.arguments = bundle
            }
            "equipment" -> {
                fragment = EquipmentDetailsFragment()
                bundle.putInt(EQUIPMENT_ID, item.itemId)
                tag = "EquipmentDetails"
                fragment?.arguments = bundle
            }
            "ingredient" -> {
                fragment = IngredientDetailsFragment(this)
                bundle.putString(INGREDIENT_CATEGORY, item.category)
                bundle.putInt(INGREDIENT_ID, item.itemId)
                tag = "IngredientDetails"
                fragment?.arguments = bundle
            }
        }

        fragment?.let { activity?.loadFragment(it, tag, true) }
    }

    override fun dataIsChanged(isChanged: Boolean, ingredientId: Int, category: String) {
        adapter?.getAdapterList()?.forEachIndexed { index, element ->
            if (category == element.category && ingredientId == element.itemId && element.selected != isChanged) {
                adapter?.resetDataItem(index, isChanged)
                adapter?.notifyItemChanged(index)
            }
        }
    }

    override fun onIconClick(position: Int, status: Boolean) {
        val item = adapter?.getAdapterList()?.get(position)
        presenter.updateShoppingStatus(item?.itemId!!, item.name, item.image, item.mainCategory, item.category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
