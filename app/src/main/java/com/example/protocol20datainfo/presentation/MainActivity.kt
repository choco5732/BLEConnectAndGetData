package com.example.protocol20datainfo.presentation

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.protocol20datainfo.databinding.MainActivityBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.UUID

@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity() {

    companion object {
        /**
         * Service UUID: e093f3b5-00a3-a9e5-9eca-40016e0edc24
         *                  * Characteristic UUID: e093f3b5-00a3-a9e5-9eca-40026e0edc24
         *                  * Characteristic UUID: e093f3b5-00a3-a9e5-9eca-40036e0edc24
         */
        const val serviceUuidT10 = "e093f3b5-00a3-a9e5-9eca-40016e0edc24"
        const val characteristicUuidWriteT10 = "e093f3b5-00a3-a9e5-9eca-40036e0edc24"
        const val characteristicUuidReadT10 = "e093f3b5-00a3-a9e5-9eca-40026e0edc24"

        private const val UUID_CONNECTION_SERVICE_T01 = "e1b40000-ffc4-4daa-a49b-1c92f99072ab"
        private const val UUID_CONNECTION_CHARACTERISTIC_WRITE_T01 = "e1b40002-ffc4-4daa-a49b-1c92f99072ab"
        private const val UUID_CONNECTION_CHARACTERISTIC_READ_T01 = "e1b40001-ffc4-4daa-a49b-1c92f99072ab"
    }

    lateinit var binding: MainActivityBinding
    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen() // 꼭 binding.root 위에 있어야 한다. 명심!
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }


    private fun initView() = with(binding) {

        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
        }.attach()


        // 스플래쉬 API 애니메이션 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                // Create your custom animation.
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 600L

                // Call SplashScreenView.remove at the end of your custom animation.
                slideUp.doOnEnd { splashScreenView.remove() }

                // Run your animation.
                slideUp.start()
            }
        }
    }
}