package com.example.protocol20datainfo.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.protocol20datainfo.databinding.FragmentBleInfoBinding
import com.example.protocol20datainfo.presentation.viewmodel.MainViewModel

class BleInfoFragment : Fragment() {

    companion object {
        fun newInstance() = BleInfoFragment()
    }

    private var _binding: FragmentBleInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
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
//        initViewModel()
        Log.d("choco5732","상세 프래그먼트 onResume")
    }

    override fun onPause() {
        super.onPause()
//        initViewModel()
        Log.d("choco5732","상세 프래그먼트 onPause")
    }

    private fun initViewModel() = with(viewModel) {
        data.observe(viewLifecycleOwner) { data ->
            Log.d("choco5732","상세 프래그먼트 initViewModel")
            Log.d("choco5732", "상세 프래그먼트! ${data.toString()}")
            binding.bleDetailDeviceName.text = data.deviceName
            binding.bleDetilReceivingData.text =

                "count : ${data.count}\nstx1 : ${String.format("0x%02X", data.stx1)}  stx2 : ${String.format("0x%02X", data.stx2)} \ncommandId : ${String.format("0x%02X", data.command)}\n" +
                        "status : ${String.format("0x%02X", data.status)}\n" +
                        "length : ${java.lang.Byte.toUnsignedInt(data.length!!.toByte()) + 8 } \nreversed : ${data.reversed} \n " +
                        "${data.time1?.plus(2000)}년 ${data.time2}월 ${data.time3}일 ${data.time4}시 ${data.time5}분 ${data.time6}초\n" +
                        "temperature : ${data.temperature}\nbattery level : ${data.battery}\n"
        }

        test.observe(viewLifecycleOwner) {
            Log.d("choco5732", "bleInfoFragment에서 테스트 뷰모델 관측 됨!")
            binding.bleDetailDeviceNameTitle.text = it
        }
    }
}