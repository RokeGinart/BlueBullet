package com.example.coctails.ui.screens.fragments.ingredients_details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.network.models.firebase.drink.IngredientsModel
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.photoview.PhotoFragment
import com.example.coctails.utils.COCKTAIL_NAME
import com.example.coctails.utils.COCKTAIL_PHOTO
import com.example.coctails.utils.INGREDIENT_CATEGORY
import com.example.coctails.utils.INGREDIENT_ID
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_ingredient_details.*

class IngredientDetailsFragment : BaseFragment<IngredientDetailsPresenter, IngredientDetailsView>(), IngredientDetailsView, View.OnClickListener{

    private var activity: MainActivity? = null
    private var ingCategory: String? = null
    private var ingId: Int? = null
    private var ingredient : IngredientsModel? = null

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
        }
    }

    override fun onResume() {
        super.onResume()
        commonToolbarBackPress.setOnClickListener{activity?.onBackPressed()}

        ingredientImageID.setOnClickListener(this)
        ingredientShopID.setOnClickListener(this)
        ingredientShopID.setOnLongClickListener{
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(ingredient?.link))
            startActivity(browserIntent)
            return@setOnLongClickListener true
        }
    }

    @SuppressLint("SetTextI18n")
    override fun showIngredientResult(ingredientModel: IngredientsModel) {
        commonProgressBar.visibility = View.GONE
        ingredientScrollID.visibility = View.VISIBLE

        ingredient = ingredientModel

        commonToolbarTitle.text = ingredientModel.name
        ingredientCategoryID.text = ingredientModel.category?.name
        ingredientABVID.text = ingredientModel.abv.toString() + getString(R.string.percent)
        ingredientCountryID.text = ingredientModel.country
        ingredientDescriptionID.text = ingredientModel.description

        Glide.with(this).load(ingredientModel.image).into(ingredientImageID)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ingredientImageID -> {
                val fragment = PhotoFragment()
                val bundle = Bundle()

                bundle.putString(COCKTAIL_PHOTO, ingredient?.image)
                bundle.putString(COCKTAIL_NAME, ingredient?.name)
                fragment.arguments = bundle

                activity?.loadPhotoFragment(fragment, "IngredientPhoto")
            }

            R.id.ingredientShopID -> {
                activity?.customToast(getString(R.string.clickToShop), 1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
