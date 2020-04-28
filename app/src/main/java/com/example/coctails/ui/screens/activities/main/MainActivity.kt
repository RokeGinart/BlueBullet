package com.example.coctails.ui.screens.activities.main

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.coctails.R
import com.example.coctails.interfaces.OnSearch
import com.example.coctails.ui.screens.BaseActivity
import com.example.coctails.ui.screens.fragments.favorites.FavoritesFragment
import com.example.coctails.ui.screens.fragments.mainscreen.MainScreenFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<MainPresenter, MainView>(), MainView {

    private var backPressedTime: Long = 0
    private var fragment: Fragment? = null
    private var active = Fragment()
    private val fragment1 = MainScreenFragment()
    private val fragment2 = FavoritesFragment()
    private val fm = supportFragmentManager

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_bar -> {

                    fm.beginTransaction().hide(active).show(fragment1).commit()
                    active = fragment1
                    return@OnNavigationItemSelectedListener true
                }


                R.id.action_favorites -> {
                    fm.beginTransaction().hide(active).show(fragment2).commit()
                    active = fragment2
                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_nearby -> {
                    /*   fm.beginTransaction().hide(active).show(fragment3).commit()

                       active = fragment3*/
                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_settings -> {
                    /*   fm.beginTransaction().hide(active).show(fragment3).commit()

                       active = fragment3*/
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun providePresenter(): MainPresenter = MainPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.bindView(this)

        bottom_navigation.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )

        active = fragment1

        fm.beginTransaction().add(R.id.fragment_container, fragment1, "Main").commit()
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "Favorites").hide(fragment2)
            .commit()
    }

    fun loadFragment(fragment: Fragment, name: String, addToBackStackBoo: Boolean) {
        val fm = fm.beginTransaction()
            .setCustomAnimations(R.anim.right_in, R.anim.lift_in)
            .add(R.id.fragment_container, fragment, name)
        this.fragment = fragment

        if (addToBackStackBoo) {
            fm.addToBackStack(null)
        }

        fm.commit()
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            fm.beginTransaction().setCustomAnimations(R.anim.left_out, R.anim.right_out)
                .remove(fragment!!).commit()
            fm.popBackStack()
        } else {
            val t = System.currentTimeMillis()
            if (t - backPressedTime > 2000) {
                backPressedTime = t
                customToast("Нажмите назад, чтобы выйти")
            } else {
                super.onBackPressed()
                this.finish()
            }
        }
    }

    fun customToast(text: String) {
        val inflater = layoutInflater
        val layout: View =
            inflater.inflate(R.layout.custom_toast, findViewById(R.id.customToastLayout))
        val tv = layout.findViewById(R.id.customToastMessage) as TextView
        tv.text = text
        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.BOTTOM, 0, 200)
        toast.view = layout
        toast.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }
}


