package com.example.coctails.ui.screens.activities.splash

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.coctails.R
import com.example.coctails.core.Cocktails
import com.example.coctails.services.MyWorker
import com.example.coctails.ui.screens.BaseActivity
import com.example.coctails.ui.screens.activities.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit


class SplashActivity : BaseActivity<SplashPresenter, SplashView>(), SplashView {

    override fun providePresenter(): SplashPresenter = SplashPresenterImpl()

    private var timer : CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_splash)
        presenter.bindView(this)

        Handler().postDelayed({
            progressLayout.visibility = View.VISIBLE
            startCountDown()

            if (isInternetAvailable(this)) {
                presenter.downloadData()
            } else {
                presenter.checkData()
              //  App.instanse?.database?.cocktailFB()?.deleteAllData()
            }
        }, 1000)


        /*  if(Cocktails.getPref().isAdult()){
              startMainActivity()
          } else {
              showAdultDialog()
          }*/


    }

    private fun startCountDown(){
        timer = object : CountDownTimer(5000, 1000){
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                with(splashMessage){
                    visibility = View.VISIBLE
                    animate()
                        .translationYBy(-100f)
                        .translationY(0f)
                        .duration = 300
                }
            }
        }

        timer?.start()
    }

    @Suppress("DEPRECATION")
    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    private fun startMainActivity() {
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun showAdultDialog() {
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

    override fun message() {
        timer?.cancel()
        splashMessage.visibility = View.GONE

        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }
}
