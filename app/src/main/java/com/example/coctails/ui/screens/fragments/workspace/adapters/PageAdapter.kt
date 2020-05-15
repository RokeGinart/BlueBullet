package com.example.coctails.ui.screens.fragments.workspace.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.coctails.R
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.cocktails.CocktailsWSFragment
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.IngredientsWSFragment
import com.example.coctails.utils.PublisherSubject

class PageAdapter(private val context: Context, fm: FragmentManager, private val subject: PublisherSubject) : FragmentPagerAdapter(fm) {

    private val TAB_TITLES = arrayOf(
        R.string.ingredients,
        R.string.cocktails
    )

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            IngredientsWSFragment(subject).newInstance(position + 1, subject)
        } else {
            CocktailsWSFragment(subject).newInstance(position + 1, subject)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int = 2
}