package com.example.protocol20datainfo

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.protocol20datainfo.databinding.DeviceItemBinding
import com.example.protocol20datainfo.databinding.MainActivityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {

    lateinit var binding: MainActivityBinding
    private var isBlue = true // 초기 색은 파란색

    private var scanning = false
    private val handler = android.os.Handler()

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

//    val binding2: DeviceItemBinding = DeviceItemBinding.inflate(layoutInflater)

    private val deviceList = ArrayList<Device>()

    private val deviceAdapter by lazy {
        DeviceAdapter(
            deviceList,
            onClickItem = { position, item ->
                Log.d("choco5732", "메인 액티비티 : 내 불렀능교!")
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        deviceList.add(Device(deviceName = "아이폰14", deviceMac = "13:24:12:55"))
        deviceList.add(Device(deviceName = "아이폰15", deviceMac = "13:24:12:55"))
        deviceList.add(Device(deviceName = "AGMS", deviceMac = "16:DE:12:55"))
        deviceList.add(Device(deviceName = "AGMS2", deviceMac = "16:DE:12:55"))


        binding.deviceRecyclerView.adapter = deviceAdapter
        binding.deviceRecyclerView.layoutManager = LinearLayoutManager(this)


        // 블투 스캔 권한 체크. 없으면 요구.
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.BLUETOOTH_SCAN),
                1
            )
        }

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

        // 텍스트 눌러을 시, 블루투스 아이콘 색 바꾸기 (실험)
        binding.mainHelloTv.setOnClickListener {
            if (isBlue) {
                binding.mainBluetooth.setImageResource(R.drawable.ic_bluetooth_red)
            } else {
                binding.mainBluetooth.setImageResource(R.drawable.ic_bluetooth_blue)
            }
            isBlue = !isBlue
        }

        binding.mainBluetooth.setOnClickListener() {

            // 블루투스
            // Bluetooth매니저 : 블루투스 어댑터를 만들수 있다.
            val bleManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            // Bluetooth어댑터 : 장치검색,
            val bluetoothAdapter = bleManager.adapter

            val targetAddress = "60:C0:BF:ED:5E:DF"
//        val device : BluetoothDevice = bluetoothAdapter.getRemoteDevice(targetAddress)

            var device : BluetoothDevice? = null

            val callback : ScanCallback = object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    super.onScanResult(callbackType, result)

                    device = result?.device

//                if ( device?.address.toString() == targetAddress) {
//                    Log.d("choco5732", "find it! mac is : ${device?.address}")
//                }

                    Log.d("choco5732", "onScanResult")
                    Log.d("choco5732", "scanning : ${device.toString()}")

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

            scanLeDevice(callback,bluetoothAdapter.bluetoothLeScanner)

//            bluetoothAdapter.getBluetoothLeScanner().startScan(callback)
//            Toast.makeText(this, "스캔이 시작됩니다. ", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("MissingPermission")
    private fun scanLeDevice(callback: ScanCallback, scanner: BluetoothLeScanner) {
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                scanner.stopScan(callback)
                Toast.makeText(this, "스캔 종료 ", Toast.LENGTH_SHORT).show()
            }, SCAN_PERIOD)
            scanning = true
            scanner.startScan(callback)
            Toast.makeText(this, "스캔이 시작됩니다. ", Toast.LENGTH_SHORT).show()
        } else {
            scanning = false
            scanner.stopScan(callback)
            Toast.makeText(this, "스캔 종료 ", Toast.LENGTH_SHORT).show()
        }
    }
}