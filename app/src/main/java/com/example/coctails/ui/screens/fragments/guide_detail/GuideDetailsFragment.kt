package com.example.coctails.ui.screens.fragments.guide_detail


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coctails.R
import com.example.coctails.network.models.firebase.drink.Guide
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.guide_detail.adapters.GuideRecyclerAdapter
import com.example.coctails.utils.GUIDE_ID
import com.example.coctails.utils.clickWithDebounce
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_guide_details.*

class GuideDetailsFragment : BaseFragment<GuideDetailsPresenter, GuideDetailView>(), GuideDetailView {

    private var activity : MainActivity? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var adapter: GuideRecyclerAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_guide_details
    override fun providePresenter(): GuideDetailsPresenter = GuideDetailsPresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        val id = bundle?.getInt(GUIDE_ID)
        id?.let { presenter.getGuide(it) }

        setupRecycler()
    }

    private fun setupRecycler() {
        mLayoutManager = LinearLayoutManager(context)
        guideStepsRecycler.layoutManager = mLayoutManager
        guideStepsRecycler.setHasFixedSize(true)
        adapter = GuideRecyclerAdapter()
        guideStepsRecycler.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        commonToolbarBackPress.setOnClickListener{
            activity?.onBackPressed()
        }
        commonToolbarTitle.text = getString(R.string.guide)
    }

    override fun showGuide(guide: Guide?) {
        commonProgressBar.visibility = View.GONE
        guideScroll.visibility = View.VISIBLE
        
        val sourceName = SpannableString(guide?.source?.name)
        sourceName.setSpan(UnderlineSpan(), 0, sourceName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        guideTitle.text = guide?.title
        guideSource.text = sourceName
        adapter?.setList(guide?.steps?.subList(1, guide.steps?.size!!)!!)

        guideSource.clickWithDebounce {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(guide?.source?.link))
            startActivity(browserIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }
}
