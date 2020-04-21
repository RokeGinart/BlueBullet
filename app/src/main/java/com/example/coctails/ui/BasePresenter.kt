package com.example.coctails.ui

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<V : BaseView> {

    var screenView : V? = null

    private var compositeDisposable: CompositeDisposable? = null

    fun addToDispose(disposable: Disposable?){
        if(disposable != null) {
            compositeDisposable?.add(disposable)
        }
    }

    fun bindView(view: V){
        this.screenView = view
        compositeDisposable = CompositeDisposable()
    }

    fun unbindView(){
        this.screenView = null
        compositeDisposable?.dispose()
        compositeDisposable = null
    }
}
