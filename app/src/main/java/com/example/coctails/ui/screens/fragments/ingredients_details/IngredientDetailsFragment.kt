package com.example.coctails.ui.screens.fragments.ingredients_details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.interfaces.OnIngredientDataChanged
import com.example.coctails.core.room.entity.ingredients_data.IngredientsFirebaseData
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.photoview.PhotoFragment
import com.example.coctails.utils.*
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_ingredient_details.*

class IngredientDetailsFragment(private val onIngredientDataChanged: OnIngredientDataChanged) : BaseFragment<IngredientDetailsPresenter, IngredientDetailsView>(), IngredientDetailsView{

    private var activity: MainActivity? = null
    private var ingCategory: String? = null
    private var ingId: Int? = null
    private var ingredient : IngredientsFirebaseData? = null
    private var isSelected = false
    private var shopSelected = false

    override fun getLayoutId(): Int = R.layout.fragment_ingredient_details

    override fun providePresenter(): IngredientDetailsPresenter = IngredientDetailsPresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        ingCategory = bundle?.getString(INGREDIENT_CATEGORY)
        ingId = bundle?.getInt(INGREDIENT_ID)

        if(ingCategory != null && ingId != null){
            presenter.getIngredientsData(ingCategory!!, ingId!!)
            presenter.getIngredientFromDB(ingCategory!!, ingId!!)
        }
    }

    override fun onResume() {
        super.onResume()
        commonToolbarBackPress.setOnClickListener{activity?.onBackPressed()}

        ingredientShopID.setOnLongClickListener{
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(ingredient?.link))
            startActivity(browserIntent)
            return@setOnLongClickListener true
        }
    }

    @SuppressLint("SetTextI18n")
    override fun showIngredientResult(ingredientFirebaseData: IngredientsFirebaseData) {
        commonProgressBar.visibility = View.GONE
        ingredientScrollID.visibility = View.VISIBLE

        ingredient = ingredientFirebaseData

        commonToolbarTitle.text = ingredientFirebaseData.name
        ingredientCategoryID.text = ingredientFirebaseData.category?.name
        ingredientABVID.text = ingredientFirebaseData.abv.toString() + getString(R.string.percent)
        ingredientCountryID.text = ingredientFirebaseData.country
        ingredientDescriptionID.text = ingredientFirebaseData.description

        Glide.with(this).load(ingredientFirebaseData.image).into(ingredientImageID)

        viewClicked()
    }

    override fun showDatabaseResult(isSelected: Boolean, shoppingSelected : Boolean) {
        favoriteSelection(isSelected)
        shoppingSelection(shoppingSelected)
    }

    private fun favoriteSelection(selection: Boolean){
        isSelected = if(selection){
            favoriteIngredient.setImageDrawable(activity?.getDrawable(R.drawable.ic_delete))
            false
        } else {
            favoriteIngredient.setImageDrawable(activity?.getDrawable(R.drawable.ic_add_white))
            true
        }
    }

    private fun shoppingSelection(selection: Boolean){
        shopSelected = if(selection){
            trolleyImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_grocery_trolley_selected))
            false
        } else {
            trolleyImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_grocery_trolley))
            true
        }
    }

    private fun viewClicked(){
        ingredientImageID.clickWithDebounce {
            val fragment = PhotoFragment()
            val bundle = Bundle()

            bundle.putString(COCKTAIL_PHOTO, ingredient?.image)
            bundle.putString(COCKTAIL_NAME, ingredient?.name)
            fragment.arguments = bundle

            activity?.loadPhotoFragment(fragment, "IngredientPhoto")
        }

        ingredientShopID.clickWithDebounce {
            activity?.customAddRemoveToast(getString(R.string.clickToShop), 1)
        }

        favoriteIngredient.clickWithDebounce {
            favoriteSelection(isSelected)
            presenter.setIngredientToDB(ingCategory!!, ingId!!)
        }

        glassTrolley.clickWithDebounce {
            shoppingSelection(shopSelected)
            presenter.updateShoppingStatus(ingredient?.id!!, ingredient?.name!!,  ingredient?.image!!, "ingredient", ingredient?.category?.category!!)
        }
    }

    override fun successChange() {
        onIngredientDataChanged.dataIsChanged(!isSelected, ingredient?.id!!, ingredient?.category?.category!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
