package com.example.protocol20datainfo

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.protocol20datainfo.databinding.MainActivityBinding

@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity() {

    lateinit var binding: MainActivityBinding
    private var isBlue = true // 초기 색은 파란색

    private var scanning = false
    private val SCAN_PERIOD: Long = 10000
    private val handler = android.os.Handler()

    private val deviceList = ArrayList<Device>()

    private val deviceAdapter by lazy {
        DeviceAdapter(
            deviceList,
            onClickItem = { position, item ->
                Log.d("choco5732", "클릭한 장치 name : ${item.deviceName}, mac : ${item.deviceMac}")

                item.device?.connectGatt(this, true, gattCallBack)
            }
        )
    }

    private val gattCallBack = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)


        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
        }
    }

    private val permssions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.SCHEDULE_EXACT_ALARM,
        Manifest.permission.USE_EXACT_ALARM
    )
    private val permssionsFor29 = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        installSplashScreen() // 꼭 binding.root 위에 있어야 한다. 명심!
        setContentView(binding.root)

        initView()
        initPermission()

        binding.searchBle.setOnClickListener() {
            // 로티 애니메이션
            val animation = binding.searchBle
            animation.playAnimation()

            // 블루투스
            // Bluetooth매니저 : 블루투스 어댑터를 만들수 있다.
            val bleManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            // Bluetooth어댑터 : 장치검색,
            val bluetoothAdapter = bleManager.adapter

            val targetAddress = "60:C0:BF:ED:5E:DF"

            var device : BluetoothDevice? = null

            val callback : ScanCallback = object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    super.onScanResult(callbackType, result)

                    val serviceDataMap = result?.scanRecord?.serviceData
                    val deviceInfo = result?.device

                    Log.d("choco5732", "mac : ${deviceInfo?.address}, id : ${deviceInfo?.name}")
                    deviceAdapter.addDevice(Device("${deviceInfo?.name}", "${deviceInfo?.address}", deviceInfo))
                    device = result?.device

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
        }
    }


    private fun initView() = with(binding) {

        // 리사이클러뷰 어댑터 설정
        deviceRecyclerView.adapter = deviceAdapter
        deviceRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

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

    private fun initPermission() {
        // 블투 스캔 권한 체크. 없으면 요구.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                permssions,
                1
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                permssionsFor29,
                1
            )
        }
    }

    @SuppressLint("MissingPermission", "ResourceAsColor")
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