package com.example.coctails.ui.screens.fragments.cocktaildetails

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.core.App
import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import com.example.coctails.interfaces.OnIngredientDataChanged
import com.example.coctails.interfaces.OnRecyclerIconClick
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.interfaces.OnRecyclerItemClickS
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.cocktail_info.CocktailsInfoFragment
import com.example.coctails.ui.screens.fragments.cocktaildetails.adapters.EquipmentsRecyclerAdapter
import com.example.coctails.ui.screens.fragments.cocktaildetails.adapters.IngredientsRecyclerAdapter
import com.example.coctails.ui.screens.fragments.cocktaildetails.model.IngredientModelCD
import com.example.coctails.ui.screens.fragments.equipment_details.EquipmentDetailsFragment
import com.example.coctails.ui.screens.fragments.favorites.model.FavoriteSubjectModel
import com.example.coctails.ui.screens.fragments.glassdetails.GlassFragment
import com.example.coctails.ui.screens.fragments.ingredients_details.IngredientDetailsFragment
import com.example.coctails.ui.screens.fragments.photoview.PhotoFragment
import com.example.coctails.utils.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_cocktail_details.*

class CocktailDetails : BaseFragment<CocktailDetailsPresenter, CocktailDetailsView>(),
    CocktailDetailsView, OnRecyclerItemClick, OnIngredientDataChanged, OnRecyclerItemClickS,
    OnRecyclerIconClick {

    override fun getLayoutId(): Int = R.layout.fragment_cocktail_details

    private var activity: MainActivity? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var msLayoutManager: LinearLayoutManager? = null
    private var adapter: IngredientsRecyclerAdapter? = null
    private var adapterEquipment: EquipmentsRecyclerAdapter? = null
    private var cocktails: CocktailFirebaseData? = null
    private var favorite = false
    private var ingredient: IngredientModelCD? = null

    override fun providePresenter(): CocktailDetailsPresenter = CocktailDetailsPresenterImpl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        cocktails = bundle?.getSerializable(COCKTAIL) as CocktailFirebaseData?

        cocktails?.let {
            presenter.getFavorite(it.id, it.category?.category!!)
            presenter.getIngredientDetail(it)
        }

        setupRecycler()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    private fun setupRecycler() {
        mLayoutManager = LinearLayoutManager(context)
        msLayoutManager = LinearLayoutManager(context)

        ingredientsRecyclerCD.layoutManager = mLayoutManager
        ingredientsRecyclerCD.setHasFixedSize(true)
        adapter = IngredientsRecyclerAdapter(this, this)
        ingredientsRecyclerCD.adapter = adapter

        equipmentRecyclerCD.layoutManager = msLayoutManager
        equipmentRecyclerCD.setHasFixedSize(true)
        adapterEquipment = EquipmentsRecyclerAdapter(this)
        equipmentRecyclerCD.adapter = adapterEquipment

        cocktails?.let { setData(it) }
    }

    override fun onItemClick(position: Int) {
        val fragment = IngredientDetailsFragment(this)
        val bundle = Bundle()

        val ingredient = adapter?.getAdapterList()?.get(position)

        bundle.putString(INGREDIENT_CATEGORY, ingredient?.category)
        bundle.putInt(INGREDIENT_ID, ingredient?.ingredientId!!)
        fragment.arguments = bundle

        activity?.loadFragment(fragment, "IngredientDetails", true)
    }

    override fun onIconClick(position: Int, status: Boolean) {
        ingredient = adapter?.getAdapterList()?.get(position)
        presenter.setIngredientToDB(ingredient?.ingredientId!!, ingredient?.category!!)
    }

    override fun onItemClickS(position: Int) {
        val fragment = EquipmentDetailsFragment()
        val bundle = Bundle()

        val equipment = adapterEquipment?.getAdapterList()?.get(position)

        bundle.putInt(EQUIPMENT_ID, equipment?.id!!)
        fragment.arguments = bundle

        activity?.loadFragment(fragment, "EquipmentDetails", true)
    }

    override fun showFavorite(inFavorite: Boolean) {
        if (inFavorite) {
            favImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_favorite_s))
            favorite = true
        }
    }

    override fun onResume() {
        super.onResume()
        commonToolbarBackPress.setOnClickListener { activity?.onBackPressed() }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(cocktails: CocktailFirebaseData) {
        Glide.with(this).load(cocktails.image).centerCrop()
            .into(cocktailImage)
        cocktailCategoryCD.text = cocktails.category?.name

        val glass = SpannableString(cocktails.glass?.name)
        glass.setSpan(UnderlineSpan(), 0, glass.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        cocktailGlassCD.text = glass

        if (cocktails.abv == 0) {
            cocktailIbaCD.text = cocktails.abv.toString() + getString(R.string.percent)
        } else {
            cocktailIbaCD.text =
                getString(R.string.tild) + cocktails.abv + getString(R.string.percent)
        }

        if (cocktails.equipment!![1].id == 0) {
            equipmentLayout.visibility = View.GONE
            instructionView.setBackgroundResource(R.drawable.instruction_background)
        } else {
            adapterEquipment?.setList(cocktails.equipment!!.subList(1, cocktails.equipment!!.size))
        }

        instructionCD.text = cocktails.instruction
        cookTimeCD.text = cocktails.cooktime.toString() + " " + getString(R.string.min)

        commonToolbarTitle.text = cocktails.name

        viewClicked()
    }

    private fun viewClicked() {
        cocktailImage.clickWithDebounce {
            val fragment = PhotoFragment()
            val bundle = Bundle()

            bundle.putString(COCKTAIL_PHOTO, cocktails?.image)
            bundle.putString(COCKTAIL_NAME, cocktails?.name)
            fragment.arguments = bundle

            activity?.loadPhotoFragment(fragment, "CocktailPhoto")
        }

        cocktailGlassCD.clickWithDebounce {
            val fragment = GlassFragment()
            val bundle = Bundle()

            bundle.putInt(GLASS_ID, cocktails?.glass?.id!!)
            fragment.arguments = bundle

            activity?.loadFragment(fragment, "GlassDetails", true)
        }

        infoButton.clickWithDebounce {
            val fragment = CocktailsInfoFragment()
            val bundle = Bundle()

            bundle.putSerializable(COCKTAIL_INFO, cocktails?.info)
            fragment.arguments = bundle

            activity?.loadFragment(fragment, "CocktailInfo", true)
        }

        favButton.clickWithDebounce {
            favorite = if (favorite) {
                favImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_favorite_ns))
                activity?.customToast(
                    getString(R.string.message_cocktail) + cocktails?.name + getString(
                        R.string.message_delete_cocktail
                    ), 2
                )
                false
            } else {
                favImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_favorite_s))
                activity?.customToast(
                    getString(R.string.message_cocktail) + cocktails?.name + getString(
                        R.string.message_added_cocktail
                    ), 1
                )
                true
            }

            cocktails?.let {
                presenter.saveCocktailToFavorite(
                    it.id,
                    it.name,
                    it.image,
                    it.category?.category!!,
                    it.abv,
                    it.category?.name!!,
                    favorite
                )
            }
        }
    }

    override fun success() {
        val favoriteSubjectModel = FavoriteSubjectModel(cocktails?.category?.category!!, cocktails?.id!!, favorite)

        App.instanse?.subject?.publishFavorite(favoriteSubjectModel)

        ingredient?.let { App.instanse?.subject?.publishIngredient(it) }
        App.instanse?.subject?.publish(CHANGED_FROM_INGREDIENT)
    }

    override fun showIngredientResult(ingredientsList: List<IngredientModelCD>) {
        adapter?.setList(ingredientsList)
    }

    override fun dataIsChanged(isChanged: Boolean, ingredientId: Int, category: String) {
        adapter?.getAdapterList()?.forEachIndexed { index, element ->
            if (category == element?.category && ingredientId == element.ingredientId && element.isSelected != isChanged) {
                adapter?.updateItem(index, isChanged)
                adapter?.notifyItemChanged(index)

                App.instanse?.subject?.publishIngredient(element)
                App.instanse?.subject?.publish(CHANGED_FROM_INGREDIENT)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}