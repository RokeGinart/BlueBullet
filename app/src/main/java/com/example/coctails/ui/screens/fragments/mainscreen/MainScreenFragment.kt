package com.example.coctails.ui.screens.fragments.mainscreen

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.coctails.R
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.cocktailscategory.CocktailsCategoryFragment
import com.example.coctails.utils.CATEGORY
import com.example.coctails.utils.CATEGORY_TYPE
import com.example.coctails.utils.TOOLBAR_TITLE
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_main_screen.*

class MainScreenFragment : BaseFragment<MainScreenPresenter, MainScreenView>(), MainScreenView,
    View.OnClickListener, View.OnLongClickListener {

    private var activity: MainActivity? = null

    override fun getLayoutId(): Int = R.layout.fragment_main_screen

    override fun providePresenter(): MainScreenPresenter = MainScreenPresenterImpl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        setupClicks()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.categoryShots -> startCocktailsFragment("shots", getString(R.string.shots), 0)
            R.id.categoryCocktails -> startCocktailsFragment("longs", getString(R.string.longs), 0)
            R.id.categoryNonAlcohol -> startCocktailsFragment("nonalcohol", getString(R.string.nonalcohol), 0)
            R.id.categoryShort -> startCocktailsFragment("shorts", getString(R.string.shorts), 0)

            R.id.allCocktails -> startCocktailsFragment("all", getString(R.string.cocktails), 0)

            R.id.categoryTequila -> startCocktailsFragment("tequila", getString(R.string.tequila), 1)
            R.id.categoryGin -> startCocktailsFragment("gin", getString(R.string.gin), 1)
            R.id.categoryWhiskey -> startCocktailsFragment("whiskey", getString(R.string.whiskey), 1)
            R.id.categoryRum -> startCocktailsFragment("rum", getString(R.string.rum), 1)
            R.id.categoryBrandy -> startCocktailsFragment("brandy", getString(R.string.brandy), 1)
            R.id.categoryVodka -> startCocktailsFragment("vodka", getString(R.string.vodka), 1)
        }
    }

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.categoryShots -> showDialog(getString(R.string.shot_desc))
            R.id.categoryCocktails -> showDialog(getString(R.string.long_desc))
            R.id.categoryShort -> showDialog(getString(R.string.short_desc))
            R.id.categoryNonAlcohol -> showDialog(getString(R.string.nonalco_desc))
        }

        return true
    }

    private fun showDialog(text: String) {

        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.custom_dialog_info)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )

      //  dialog.setCanceledOnTouchOutside(false)

        val dialogHome = dialog.findViewById<TextView>(R.id.dialogTextInfo)
        dialogHome.text = text
        dialogHome.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun startCocktailsFragment(category: String, title: String, type: Int) {
        val fragment = CocktailsCategoryFragment()
        val bundle = Bundle()

        bundle.putString(CATEGORY, category)
        bundle.putString(TOOLBAR_TITLE, title)
        bundle.putInt(CATEGORY_TYPE, type)
        fragment.arguments = bundle

        activity?.loadFragment(fragment, "Category", true)
    }

    override fun onResume() {
        super.onResume()
        activity?.setSupportActionBar(commonToolbar)
        activity?.supportActionBar?.title = null
        commonToolbarTitle.text = getString(R.string.bar)
        commonToolbarBackPress.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }

    private fun setupClicks(){
        categoryCocktails.setOnClickListener(this)
        categoryShots.setOnClickListener(this)
        categoryShort.setOnClickListener(this)
        categoryNonAlcohol.setOnClickListener(this)
        allCocktails.setOnClickListener(this)

        categoryCocktails.setOnLongClickListener(this)
        categoryShots.setOnLongClickListener(this)
        categoryShort.setOnLongClickListener(this)
        categoryNonAlcohol.setOnLongClickListener(this)

        categoryTequila.setOnClickListener(this)
        categoryGin.setOnClickListener(this)
        categoryWhiskey.setOnClickListener(this)
        categoryRum.setOnClickListener(this)
        categoryBrandy.setOnClickListener(this)
        categoryVodka.setOnClickListener(this)
    }
}

