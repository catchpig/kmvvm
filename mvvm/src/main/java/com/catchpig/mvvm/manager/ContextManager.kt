package com.catchpig.mvvm.manager

import android.content.Context
import android.content.SharedPreferences

class ContextManager {
    companion object {
        private lateinit var context: Context

        fun init(context: Context) {
            this.context = context
        }

        fun getContext(): Context {
            return context
        }

        fun getSharedPreferences(name: String, mode: Int): SharedPreferences {
            return context.getSharedPreferences(name, mode)
        }
    }
}