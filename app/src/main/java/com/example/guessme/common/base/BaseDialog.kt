package com.example.guessme.common.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

abstract class BaseDialog<T: ViewBinding> (private val layoutResId: Int) : DialogFragment() {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }

        _binding = getViewBinding()
        return binding.root
    }

    abstract fun getViewBinding(): T

    override fun onResume() {
        super.onResume()
        val display = resources.displayMetrics
        val window: Window = dialog?.window ?: return
        val params: WindowManager.LayoutParams = window.attributes
        params.width = (display.widthPixels * 0.8).toInt()

        dialog?.window!!.attributes = params
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}