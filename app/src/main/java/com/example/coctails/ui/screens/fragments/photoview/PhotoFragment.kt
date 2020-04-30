package com.example.coctails.ui.screens.fragments.photoview

import android.content.Context
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.coctails.R
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.utils.COCKTAIL_NAME
import com.example.coctails.utils.COCKTAIL_PHOTO
import kotlinx.android.synthetic.main.fragment_photo.*

class PhotoFragment : BaseFragment<PhotoFragmentPresenter, PhotoFragmentView>(), PhotoFragmentView{

    private var activity : MainActivity? = null
    private var cocktailPhoto : String? = null
    private var cocktailName: String? = null

    override fun getLayoutId(): Int = R.layout.fragment_photo

    override fun providePresenter(): PhotoFragmentPresenter = PhotoFragmentPresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)

        val bundle = arguments
        cocktailPhoto = bundle?.getString(COCKTAIL_PHOTO)
        cocktailName = bundle?.getString(COCKTAIL_NAME)

        Glide.with(this).load(cocktailPhoto).into(cocktailPhotoView)
        cocktailNameView.text = cocktailName

    }

    override fun onResume() {
        super.onResume()
        activity?.window?.navigationBarColor = resources.getColor(R.color.black)
        activity?.window?.statusBarColor = resources.getColor(R.color.black)
        exitFromImageView.setOnClickListener{
            activity?.window?.navigationBarColor = resources.getColor(R.color.blue)
            activity?.window?.statusBarColor = resources.getColor(R.color.blue)
            activity?.onBackPressed()
        }

        photoLayout.setOnClickListener(null)
    }

    override fun onDestroyView() {
        presenter.unbindView()
        super.onDestroyView()
    }
}