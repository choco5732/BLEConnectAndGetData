package com.example.protocol20datainfo.prsentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.protocol20datainfo.R
import com.example.protocol20datainfo.databinding.FragmentBleInfoBinding
import com.example.protocol20datainfo.databinding.FragmentBleListBinding

class BleInfoFragment : Fragment() {

    companion object {
        fun newInstance() = BleInfoFragment()
    }

    private var _binding: FragmentBleInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBleInfoBinding.inflate(inflater, container, false)
        Log.d("choco5732","상세 프래그먼트 onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("choco5732","상세 프래그먼트 onViewCreated")
        initViewModel()

    }

    override fun onResume() {
        super.onResume()
        initViewModel()
        Log.d("choco5732","상세 프래그먼트 onResume")

    }

    override fun onPause() {
        super.onPause()
        initViewModel()
        Log.d("choco5732","상세 프래그먼트 onPause")
    }

    private fun initViewModel() = with(viewModel) {
        data.observe(viewLifecycleOwner) {
            Log.d("choco5732","상세 프래그먼트 initViewModel")
            Log.d("choco11111", "상세 프래그먼트! ${it.toString()}")
            binding.bleDetailDeviceName.text = it.deviceName
            binding.bleDetilReceivingData.text = it.toString()
        }
    }
}