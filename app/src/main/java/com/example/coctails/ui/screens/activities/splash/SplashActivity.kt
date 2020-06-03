package com.example.coctails.ui.screens.activities.splash

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.example.coctails.R
import com.example.coctails.core.Cocktails
import com.example.coctails.ui.screens.BaseActivity
import com.example.coctails.ui.screens.activities.main.MainActivity

class SplashActivity : BaseActivity<SplashPresenter, SplashView>(), SplashView {

    override fun providePresenter(): SplashPresenter = SplashPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setContentView(R.layout.activity_splash)
        presenter.bindView(this)

        if(Cocktails.getPref().isAdult()){
            startMainActivity()
        } else {
            showAdultDialog()
        }
    }

    private fun startMainActivity(){
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun showAdultDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_adult_check)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )

        val dialogConfirm = dialog.findViewById<TextView>(R.id.adultYes)
        val dialogNegative = dialog.findViewById<TextView>(R.id.adultNo)

        dialogConfirm.setOnClickListener {
            Cocktails.getPref().setAdult(true)
            startMainActivity()
            dialog.dismiss()
        }

        dialogNegative.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }
}
