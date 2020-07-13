package com.example.coctails.ui.screens.activities.main

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.coctails.R
import com.example.coctails.ui.screens.BaseActivity
import com.example.coctails.ui.screens.fragments.favorites.FavoriteFragment
import com.example.coctails.ui.screens.fragments.kitchen.KitchenFragment
import com.example.coctails.ui.screens.fragments.mainscreen.MainScreenFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainPresenter, MainView>(), MainView {

    private var backPressedTime: Long = 0
    private var fragmentList = mutableListOf<Fragment>()
    private val fragment1 = MainScreenFragment()
    private val fragment2 =
        KitchenFragment()
    private val fragment3 = FavoriteFragment()
    private var active = Fragment()
    private val fm = supportFragmentManager

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_bar -> {

                  /*  fm.beginTransaction().hide(active).show(fragment1).commit()
                    active = fragment1*/
                    fm.beginTransaction().replace(R.id.fragment_container, fragment1, "Main").commit()
                    return@OnNavigationItemSelectedListener true
                }


                R.id.action_workspace -> {
                   /* fm.beginTransaction().hide(active).show(fragment2).commit()
                    active = fragment2*/
                    fm.beginTransaction().replace(R.id.fragment_container, fragment2, "Workspace").commit()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.action_favorites -> {
              /*      fm.beginTransaction().hide(active).show(fragment3).commit()
                    active = fragment3*/
                    fm.beginTransaction().replace(R.id.fragment_container, fragment3, "Favorites").commit()
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

        fm.beginTransaction().replace(R.id.fragment_container, fragment1, "Main").commit()
    }

    fun loadFragment(fragment: Fragment, name: String, addToBackStackBoo: Boolean) {
        hideBottomNavigation()

        val fm = fm.beginTransaction()
            .setCustomAnimations(R.anim.right_in, R.anim.lift_in)
            .add(R.id.fragment_container, fragment, name)

        if (addToBackStackBoo) {
            fm.addToBackStack(null)
            fragmentList.add(fragment)
        }

        fm.commit()
    }

    fun loadPhotoFragment(fragment: Fragment, name: String) {
        val fm = fm.beginTransaction()
            .setCustomAnimations(R.anim.photo_anim, R.anim.lift_in)
            .add(R.id.fragment_container, fragment, name)
        fragmentList.add(fragment)
        fm.addToBackStack(null)
        fm.commit()
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            fm.beginTransaction().setCustomAnimations(R.anim.left_out, R.anim.right_out)
                .remove(fragmentList[fm.backStackEntryCount-1]).commit()
            fm.popBackStack()
            fragmentList.removeAt(fm.backStackEntryCount - 1)

            if(fm.backStackEntryCount == 1){
                showBottomNavigation()
            }

           window?.navigationBarColor = ContextCompat.getColor(this, R.color.blue)
           window?.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        } else {
            val t = System.currentTimeMillis()
            if (t - backPressedTime > 2000) {
                backPressedTime = t
                "Нажмите назад, чтобы выйти".customToast()
            } else {
                super.onBackPressed()
                this.finish()
            }
        }
    }

    private fun hideBottomNavigation() {
        with(bottom_navigation) {
            if (visibility == View.VISIBLE) {
                animate()
                    .translationY(0f)
                    .translationYBy(200f)
                    .withEndAction { visibility = View.GONE }
                    .duration = 300
            }
        }
    }

    private fun showBottomNavigation() {
        with(bottom_navigation) {
                visibility = View.VISIBLE
                animate()
                    .translationYBy(200f)
                    .translationY(0f)
                    .duration = 300
        }
    }

    private fun String.customToast() {
        val inflater = layoutInflater
        val layout: View =
            inflater.inflate(R.layout.custom_toast,
                findViewById(R.id.customToastLayout)
            )

        val tv = layout.findViewById(R.id.customToastMessage) as TextView

        tv.text = this
        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.BOTTOM, 0, 200)
        toast.view = layout
        toast.show()
    }

    fun customAddRemoveToast(text: String, action : Int){
        val inflater = layoutInflater
        val layout: View =
            inflater.inflate(R.layout.custom_toast, findViewById(R.id.customToastLayout))

        val ll = layout.findViewById(R.id.customToastLayout) as LinearLayout
        val tv = layout.findViewById(R.id.customToastMessage) as TextView

        if(action == 1){
            ll.setBackgroundResource(R.drawable.toast_background_green)
        } else if(action == 2){
            ll.setBackgroundResource(R.drawable.toast_background_red)
        }

        tv.text = text
        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.view = layout
        toast.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }
}


