package com.example.protocol20datainfo

import android.Manifest
import android.animation.ObjectAnimator
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.protocol20datainfo.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: MainActivityBinding
    private var isBlue = true // 초기 색은 파란색

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                slideUp.duration = 200L

                // Call SplashScreenView.remove at the end of your custom animation.
                slideUp.doOnEnd { splashScreenView.remove() }

                // Run your animation.
                slideUp.start()
            }
        }


        binding.mainHelloTv.setOnClickListener {
            if (isBlue) {
                binding.mainBluetooth.setImageResource(R.drawable.ic_bluetooth_red)
            } else {
                binding.mainBluetooth.setImageResource(R.drawable.ic_bluetooth_blue)
            }
            isBlue = !isBlue
        }


        // Bluetooth매니저 : 블루투스어댑터를 만들수 있다.
        val bleManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        // Bluetooth어댑터 : 장치검색,
        val bluetoothAdapter = bleManager.adapter

//        val targetAddress = "60:C0:BF:ED:5E:DF"
//        val device : BluetoothDevice = bluetoothAdapter.getRemoteDevice(targetAddress)

        var device : BluetoothDevice? = null

        val callback : ScanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)

                device = result?.device

                Log.d("choco5732", "scanning : ${device.toString()}")
                Log.d("choco5732", "onScanResult")
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                super.onBatchScanResults(results)
                Log.d("choco5732", "onBatchScanResults")
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                Log.d("choco5732", "errorCode : ${errorCode}")
                Log.d("choco5732", "onScanFailed")
            }
        }


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            Log.d("choco5732", "not grantted")
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_SCAN),
                1
            )
            bluetoothAdapter.getBluetoothLeScanner().startScan(callback)

            return
        } else {
            Log.d("choco5732", "권한 있고, startScan시작")
            bluetoothAdapter.getBluetoothLeScanner().startScan(callback)

        }




    }
}
