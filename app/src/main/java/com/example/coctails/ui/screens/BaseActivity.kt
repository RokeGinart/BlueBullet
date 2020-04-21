package com.example.coctails.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coctails.ui.BasePresenter
import com.example.coctails.ui.BaseView

abstract class BaseActivity<T : BasePresenter<V>, V : BaseView> : AppCompatActivity(), BaseView {

    lateinit var presenter: T
    abstract fun providePresenter(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = providePresenter()
    }
}