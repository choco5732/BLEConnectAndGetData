package com.example.protocol20datainfo.prsentation

import android.os.Bundle
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

    }

    private fun initViewModel() = with(viewModel) {
        data.observe(viewLifecycleOwner) {
            binding.bleDetailDeviceName.text = it.deviceName
            binding.bleDetilReceivingData.text = it.toString()
        }
    }
}