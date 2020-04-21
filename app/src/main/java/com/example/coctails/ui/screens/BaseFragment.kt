package com.example.coctails.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coctails.ui.BasePresenter
import com.example.coctails.ui.BaseView
import com.example.coctails.utils.inflate

abstract class BaseFragment <T : BasePresenter<V>, V : BaseView> : Fragment(), BaseView {

    abstract fun getLayoutId(): Int
    lateinit var presenter: T
    abstract fun providePresenter(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = providePresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (getLayoutId() == 0) {
            throw RuntimeException("Invalid Layout ID")
        }
        return container?.inflate(getLayoutId())
    }
}