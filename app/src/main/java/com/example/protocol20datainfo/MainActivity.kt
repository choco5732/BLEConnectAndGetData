package com.example.protocol20datainfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.protocol20datainfo.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: MainActivityBinding
    private var isBlue = true // 초기 색은 파란색

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainHelloTv.setOnClickListener {
            if (isBlue) {
                binding.mainBluetooth.setImageResource(R.drawable.ic_bluetooth_red)
            } else {
                binding.mainBluetooth.setImageResource(R.drawable.ic_bluetooth_blue)
            }
            isBlue = !isBlue
        }
    }
}
