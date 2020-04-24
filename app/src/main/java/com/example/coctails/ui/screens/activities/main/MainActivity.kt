package com.example.coctails.ui.screens.activities.main

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.coctails.R
import com.example.coctails.ui.screens.BaseActivity
import com.example.coctails.ui.screens.fragments.mainscreen.MainScreenFragment

class MainActivity : BaseActivity<MainPresenter, MainView>(), MainView {

    private var backPressedTime: Long = 0
    private var fragment: Fragment? = null

    override fun providePresenter(): MainPresenter = MainPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.bindView(this)

        loadFragment(MainScreenFragment(), "Main", false)
    }

    fun loadFragment(fragment: Fragment, name : String, addToBackStackBoo: Boolean){
        val fm = supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment, name)
        this.fragment = fragment
        if(addToBackStackBoo){
            fm.addToBackStack(null)
        }

        fm.commit()
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            fm.beginTransaction().remove(fragment!!).commit()
            fm.popBackStack()
        } else {
            val t = System.currentTimeMillis()
            if (t - backPressedTime > 2000) {
                backPressedTime = t
                Toast.makeText(this, "Press back to exit", Toast.LENGTH_SHORT).show()
            } else {
                super.onBackPressed()
                this.finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }
}


