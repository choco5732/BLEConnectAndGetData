package com.example.protocol20datainfo.prsentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.protocol20datainfo.R
import com.example.protocol20datainfo.databinding.FragmentBleListBinding

class BleListFragment : Fragment() {

    companion object {
        fun newInstance() = BleListFragment()
    }

    private var _binding: FragmentBleListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}