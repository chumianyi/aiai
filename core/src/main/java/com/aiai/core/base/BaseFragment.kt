package com.aiai.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.aiai.common.util.other.Logger

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    @Suppress("UNCHECKED_CAST")
    protected val binding: VB get() = _binding!!

    private var isLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
        observeData()
        Logger.d("${javaClass.simpleName} onViewCreated")
    }

    abstract fun initView(view: View, savedInstanceState: Bundle?)
    open fun observeData() {}
    open fun lazyLoad() {}

    override fun onResume() {
        super.onResume()
        if (!isLoaded) { isLoaded = true; lazyLoad() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isLoaded = false
    }
}
