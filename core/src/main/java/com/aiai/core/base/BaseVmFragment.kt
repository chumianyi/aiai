package com.aiai.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseVmFragment<VB : ViewBinding> : BaseFragment<VB>() {

    protected lateinit var viewModel: ViewModel
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    abstract fun getViewModelClass(): Class<out ViewModel>
}
