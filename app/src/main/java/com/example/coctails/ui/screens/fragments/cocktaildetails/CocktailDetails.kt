package com.example.coctails.ui.screens.fragments.cocktaildetails

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.cocktail_info.CocktailsInfoFragment
import com.example.coctails.ui.screens.fragments.cocktaildetails.adapters.IngredientsRecyclerAdapter
import com.example.coctails.ui.screens.fragments.glassdetails.GlassFragment
import com.example.coctails.ui.screens.fragments.photoview.PhotoFragment
import com.example.coctails.utils.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_cocktail_details.*


class CocktailDetails : BaseFragment<CocktailDetailsPresenter, CocktailDetailsView>(),
    CocktailDetailsView, OnRecyclerItemClick, View.OnClickListener{

    override fun getLayoutId(): Int = R.layout.fragment_cocktail_details

    private var activity: MainActivity? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var adapter: IngredientsRecyclerAdapter? = null
    private var cocktails: Cocktails? = null
    private var favorite = false

    override fun providePresenter(): CocktailDetailsPresenter = CocktailDetailsPresenterImpl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        cocktails = bundle?.getSerializable(COCKTAIL) as Cocktails?

        cocktails?.let { presenter.getFavorite(it.id, it.category?.category!! ) }

        setupRecycler()
        cocktailGlassCD.setOnClickListener(this)
        favButton.setOnClickListener(this)
        infoButton.setOnClickListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    private fun setupRecycler() {
        mLayoutManager = LinearLayoutManager(context)
        ingredientsRecyclerCD.layoutManager = mLayoutManager
        ingredientsRecyclerCD.setHasFixedSize(true)
        adapter = IngredientsRecyclerAdapter(this)
        ingredientsRecyclerCD.adapter = adapter

        cocktails?.let { setData(it) }
    }

    override fun onItemClick(position: Int) {

    }

    override fun showFavorite(inFavorite: Boolean) {
        if(inFavorite){
            favImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_favorite_s))
            favorite = true
        }
    }

    override fun onResume() {
        super.onResume()
        commonToolbarBackPress.setOnClickListener { activity?.onBackPressed() }
        cocktailImage.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setData(cocktails: Cocktails) {
        Glide.with(this).load(cocktails.image).centerCrop()
            .into(cocktailImage)
        cocktailCategoryCD.text = cocktails.category?.name

        val glass = SpannableString(cocktails.glass?.name)
        glass.setSpan(UnderlineSpan(), 0, glass.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        cocktailGlassCD.text = glass

        if(cocktails.abv == 0){
            cocktailIbaCD.text = cocktails.abv.toString() + getString(R.string.percent)
        }else{
            cocktailIbaCD.text = getString(R.string.tild) + cocktails.abv + getString(R.string.percent)
        }

        instructionCD.text = cocktails.instruction
        cookTimeCD.text = cocktails.cooktime.toString() + " " + getString(R.string.min)

        commonToolbarTitle.text = cocktails.name

        adapter?.setList(cocktails.ingredients!!.subList(1, cocktails.ingredients!!.size))
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cocktailImage -> {
                val fragment = PhotoFragment()
                val bundle = Bundle()

                bundle.putString(COCKTAIL_PHOTO, cocktails?.image)
                bundle.putString(COCKTAIL_NAME, cocktails?.name)
                fragment.arguments = bundle

                activity?.loadPhotoFragment(fragment, "CocktailPhoto")
            }
            R.id.cocktailGlassCD -> {
                val fragment = GlassFragment()
                val bundle = Bundle()

                bundle.putInt(GLASS_ID, cocktails?.glass?.id!!)
                fragment.arguments = bundle

                activity?.loadFragment(fragment, "GlassDetails", true)
            }
            R.id.infoButton -> {
                val fragment = CocktailsInfoFragment()
                val bundle = Bundle()

                bundle.putSerializable(COCKTAIL_INFO, cocktails?.info)
                fragment.arguments = bundle

                activity?.loadFragment(fragment, "CocktailInfo", true)
            }
            R.id.favButton -> {
                favorite = if(favorite){
                    favImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_favorite_ns))
                    false
                }else{
                    favImage.setImageDrawable(activity?.getDrawable(R.drawable.ic_favorite_s))
                    true
                }

                cocktails?.let { presenter.saveCocktailToFavorite(it.id, it.name, it.image, it.category?.category!!, favorite ) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}