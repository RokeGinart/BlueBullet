package com.example.coctails.ui.screens.fragments.cocktail_info

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import com.example.coctails.R
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.utils.COCKTAIL_INFO
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_cocktails_info.*


class CocktailsInfoFragment : BaseFragment<CocktailsInfoPresenter, CocktailsInfoView>(),
    CocktailsInfoView {

    private var activity: MainActivity? = null

    override fun getLayoutId(): Int = R.layout.fragment_cocktails_info

    private var information : Cocktails.Info? = null

    override fun providePresenter(): CocktailsInfoPresenter = CocktailsInfoPresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        information = bundle?.getSerializable(COCKTAIL_INFO) as Cocktails.Info?

        information?.let { setInfo(it) }
    }

    private fun setInfo(info : Cocktails.Info){
        val sourceName = SpannableString(info.source?.name)
        sourceName.setSpan(UnderlineSpan(), 0, sourceName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        authorInfo.text = info.author
        countryInfo.text = info.country
        yearInfo.text = info.year
        sourceInfo.text = sourceName
        historyInfo.text = info.history

        sourceInfo.setOnClickListener{
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(info.source?.link))
            startActivity(browserIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        commonToolbarTitle.text = getString(R.string.cocktailInfo)
        commonToolbarBackPress.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
