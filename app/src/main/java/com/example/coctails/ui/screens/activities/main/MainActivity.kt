package com.example.coctails.ui.screens.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coctails.R
import com.example.coctails.network.models.CocktailsSearch
import com.example.coctails.ui.screens.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainPresenter, MainView>(), MainView {

    override fun providePresenter(): MainPresenter = MainPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.bindView(this)

        click.setOnClickListener {
            presenter.getCocktail(nameCock.text.toString())
        }
    }

    override fun showCocktails(cocktailsSearch: CocktailsSearch) {
        result.text = cocktailsSearch.drinks?.get(0)?.strCategory + " " + cocktailsSearch.drinks?.get(0)?.strAlcoholic
    }
}
