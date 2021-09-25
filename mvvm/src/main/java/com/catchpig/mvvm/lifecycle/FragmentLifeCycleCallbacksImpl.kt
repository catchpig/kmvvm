package com.catchpig.mvvm.lifecycle

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.catchpig.utils.ext.logd

/**
 * fragment生命周期的监听
 */
open class FragmentLifeCycleCallbacksImpl : FragmentManager.FragmentLifecycleCallbacks() {
    companion object {
        private const val TAG = "FragmentLifeCycleCallbacksImpl"
    }

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        "${f::class.java.simpleName}->onPreAttached".logd(TAG)
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        "${f::class.java.simpleName}->onAttached".logd(TAG)
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        "${f::class.java.simpleName}->onPreCreated".logd(TAG)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        "${f::class.java.simpleName}->onCreated".logd(TAG)
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        "${f::class.java.simpleName}->onActivityCreated".logd(TAG)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        "${f::class.java.simpleName}->onViewCreated".logd(TAG)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        "${f::class.java.simpleName}->onStarted".logd(TAG)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        "${f::class.java.simpleName}->onResumed".logd(TAG)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        "${f::class.java.simpleName}->onPaused".logd(TAG)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        "${f::class.java.simpleName}->onStopped".logd(TAG)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        "${f::class.java.simpleName}->onSaveInstanceState".logd(TAG)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        "${f::class.java.simpleName}->onViewDestroyed".logd(TAG)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        "${f::class.java.simpleName}->onDestroyed".logd(TAG)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        "${f::class.java.simpleName}->onDetached".logd(TAG)
    }
}