package com.example.protocol20datainfo.prsentation

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnLongClickListener
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.protocol20datainfo.databinding.MainActivityBinding
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
    private var isBlue = true // 초기 색은 파란색
    private var services: MutableList<BluetoothGattService>? = null

    private var scanning = false
    private val SCAN_PERIOD: Long = 10000
    private val handler = android.os.Handler()
    lateinit var mGatt : BluetoothGatt

    private val deviceList = ArrayList<Device>()

    private val deviceAdapter by lazy {
        DeviceAdapter(
            deviceList,
            onClickItem = { position, item ->
                Log.d("choco5732", "클릭한 장치 name : ${item.deviceName}, mac : ${item.deviceMac}")
                // gatt 연결!
//                item.device!!.connectGatt(this, true, gattCallBack)
                mGatt = item.device!!.connectGatt(this, true, gattCallBack)
                Toast.makeText(this@MainActivity, "${item.deviceName}에 연결 중입니다...", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private val gattCallBack = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)



            when(newState){
                BluetoothProfile.STATE_CONNECTING -> {

//                    Toast.makeText(this@MainActivity, "연결 중입니다...", Toast.LENGTH_SHORT).show()
                    Log.d("choco5732", "gatt connecting~")
                }
                BluetoothProfile.STATE_CONNECTED -> {
//                    Toast.makeText(this@MainActivity, "${gatt?.device?.name}와(과) 연결 되었습니다!", Toast.LENGTH_SHORT).show()
                    Log.d("choco5732", "gatt connected!")

                    gatt?.discoverServices()

                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    gatt?.close()
                    Log.d("choco5732", "gatt disconnected!!!")
//                    Toast.makeText(this@MainActivity, "연결 실패", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    gatt?.close()
//                    Toast.makeText(this@MainActivity, "연결 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            if (status == BluetoothGatt.GATT_SUCCESS) {

                Log.d("choco5732", "가트 연결이 되었고, onServicesDiscovered에 진입!")

                // 서비스 조회
                val services = gatt?.services
                lateinit var service : BluetoothGattService

                services?.forEach { service ->
                    Log.d("choco5732", "Service UUID: ${service.uuid}")

                    val characteristics = service.characteristics
                    characteristics.forEach { characteristic ->
                        Log.d("choco5732", "Characteristic UUID: ${characteristic.uuid}")
                    }
                }


                val characteristicUUID = UUID.fromString(characteristicUuidReadT10)

                // 원하는 서비스 가져옴 ( 수정필요 )
//                var found = false
//                var i = 0
//                while (i < services!!.size && !found) {
//                    service = services[i]
//                    //                Log.e(TAG,"SERVICES <"+i+"> : "+service.getUuid().toString());
//                    if (service.uuid.toString().equals(serviceUuidT10, ignoreCase = true)) {
//                        found = true
//                    } else {
//                        i++
//                    }
//                }
//
//                if (found) {
//                    service = services[i]
//                }

                var found = false
                var i = 0
                while (i < services!!.size && !found) {
                    val currentService = services[i]
                    if (currentService.uuid.toString().equals(serviceUuidT10, ignoreCase = true)) {
                        service = currentService
                        found = true
                    }
                    i++
                }

                Log.d("choco5732", "찾은 서비스는 ${service.toString()}입니다.")

                val finalCharacteristic = service.getCharacteristic(characteristicUUID)
//                val finaldescriptor = finalCharacteristic.getDescriptor()
                gatt.setCharacteristicNotification(finalCharacteristic, true)

                Toast.makeText(this@MainActivity, "${gatt.device.name} 에 연결되었습니다!",Toast.LENGTH_SHORT).show()


            } else {
                Log.d("choco5732" ,"가트 진입 실패!")
            }


        }
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            val data = characteristic!!.value

            /**
             * stx 2바이트
             * productID 2바이트
             * command 1바이트
             * length 1바이트
             * data & time 6바이트
             * weo level n바이트
             * aeo level n바이트
             * wep level 2바이트
             * aep level 2바이트
             * temperature 2바이트
             * battery Level 2바이트
             * crc16 2바이트
             */

            val stx1 = data[0]
            val stx2 = data[1]
            Log.d("data", "stx1 : ${stx1}, stx2: ${stx2}")

            val productId1 = data[2]
            val productId2 = data[3]
            Log.d("data", "productId1 : ${productId1}, productId2 : ${productId2}")
//            printf("%02X\n", 10);   // 출력 (앞의 빈자리를 0으로 채우기): 0A
            Log.d("data", "productId1 converted : ${String.format("0x%02X ", productId1)}, productId2 : ${String.format("0x%02X ", productId2)}" )
            // 왜 0x%02X라는 작업을 포맷하는가? 오늘 파일이 16진수인가?

            Log.d("data", "data의 길이 : ${data.size}")
            val command = data[4]
            Log.d("data", "command : $command")

            Log.d("data", "command converted : ${String.format("0x%02X", command)}")

            val length = data[5]
            Log.d("data", "protocol length : $length")


            val time1 = data[6]
            val time2 = data[7]
            val time3 = data[8]
            val time4 = data[9]
            val time5 = data[10]
            val time6 = data[11]

            Log.d("data", "time : ${time1}년 ${time2}월 ${time3}일 ${time4}시 ${time5}분 ${time6}초 ")

//            data[0] :
//            data[1] :
//            data[2] :



//            Log.d("choco5732", data[0]
            var str = String(data)
            Log.d("choco5732", "불러온 데이터는 :$str")
//            Log.d("choco5732", "데이터 : ${data.contentToString()}")
        }

    }

    private val permssions = arrayOf (
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.SCHEDULE_EXACT_ALARM,
        Manifest.permission.USE_EXACT_ALARM
    )
    private val permssionsFor29 = arrayOf (
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        installSplashScreen() // 꼭 binding.root 위에 있어야 한다. 명심!
        setContentView(binding.root)

//
//        val bluetoothGatt : BluetoothGatt? = null
//        bluetoothGatt.setPreferredConnectionParameters
//        val gattt :BluetoothGatt? = null
////        gattt?.requestConnectionParameterUpdate()
        initView()
        initPermission()

//        val test : BluetoothGatt?
//        test.requestLeConnectionUpdate // 히든함수, 메소드 invoke 히든 접근법 다름 refresh gatt
//        val connectionParams = BluetoothGattConnectionParams.Builder()
//            .setMinInterval(minIntervalMillis)
//            .setMaxInterval(maxIntervalMillis)
//            .setMinLatency(minLatencyMillis)
//            .setMaxLatency(maxLatencyMillis)
//            .setDisconnectionTimeout(disconnectionTimeoutMillis)
//
//
//        val test2 : BluetoothDevice?
//        test2.setPreferredConn
        binding.mainHelloTv.setOnClickListener() {
            mGatt.disconnect()
        }


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

//        binding.searchBle.setOnLongClickListener(object : it.OnLongClickListener){
//            deviceAdapter.clearList()
//        }

        // BLE 리스트 초기화
        binding.searchBle.setOnLongClickListener(object : OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                deviceAdapter.clearList()
                Toast.makeText(this@MainActivity, "목록이 초기화 됩니다.", Toast.LENGTH_SHORT).show()
                return true
            }
        })


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