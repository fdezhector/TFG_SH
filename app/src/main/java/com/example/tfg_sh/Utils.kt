package com.example.tfg_sh

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

object Utils {
    fun goToPreviousScreen(activity: Activity){
        activity.finish()
    }

    fun goToMainScreen(activity: Activity){
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        activity.startActivity(intent)
    }
}