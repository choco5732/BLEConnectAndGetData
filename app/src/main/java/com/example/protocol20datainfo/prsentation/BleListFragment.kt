package com.example.protocol20datainfo.prsentation

import android.Manifest
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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.protocol20datainfo.R
import com.example.protocol20datainfo.databinding.FragmentBleListBinding
import java.util.UUID

@SuppressLint("MissingPermission")
class BleListFragment : Fragment() {

    private var services: MutableList<BluetoothGattService>? = null

    private var scanning = false
    private val SCAN_PERIOD: Long = 10000
    private val handler = android.os.Handler()
    lateinit var mGatt : BluetoothGatt

    var count = 1

    private val deviceList = ArrayList<Device>()
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val deviceAdapter by lazy {
        DeviceAdapter(
            deviceList,
            onClickItem = { position, item ->
                Log.e("choco5732", "클릭한 장치 name : ${item.deviceName}, mac : ${item.deviceMac}")
                // gatt 연결!
                item.device!!.connectGatt(requireContext(), true, gattCallBack)
//                mGatt = item.device!!.connectGatt(this, true, gattCallBack)
                Toast.makeText(requireContext(), "${item.deviceName}에 연결 중입니다...", Toast.LENGTH_SHORT).show()
            }
        )
    }


    private val gattCallBack = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
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

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Toast.makeText(requireContext(),"가트 연결이 되었고, onServicesDiscovered()에 진입!", Toast.LENGTH_SHORT).show()
//                Log.d("choco5732", "가트 연결이 되었고, onServicesDiscovered에 진입!")
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

                val characteristicUUID = UUID.fromString(MainActivity.characteristicUuidReadT10)
                // 원하는 서비스 가져옴 ( 수정필요 )
//                var found = false
//                var i = 0
//                while (i < services!!.size && !found) {
//                    service = services[i]
//                                    Log.e(TAG,"SERVICES <"+i+"> : "+service.getUuid().toString());
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
                    if (currentService.uuid.toString().equals(MainActivity.serviceUuidT10, ignoreCase = true)) {
                        service = currentService
                        found = true
                    }
                    i++
                }

                Log.d("choco5732", "찾은 서비스는 ${service.toString()}입니다.")

                val finalCharacteristic = service.getCharacteristic(characteristicUUID)
//                val finaldescriptor = finalCharacteristic.getDescriptor()
                gatt.setCharacteristicNotification(finalCharacteristic, true)

                Toast.makeText(requireContext(), "${gatt.device.name} 에 연결되었습니다! \n데이터를 가져오는 중입니다.. \n잠시만 기다려주세요..",Toast.LENGTH_SHORT).show()
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

            val deviceName = gatt?.device?.name
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

            Log.d("data", "time : 20${time1}년 ${time2}월 ${time3}일 ${time4}시 ${time5}분 ${time6}초 ")

            val temperature1 = data[data.size - 6]
            val temperature2 = data[data.size - 5]

//            val temp = Byte.toUnsin
            // 온도
            val nTemperature =
                java.lang.Byte.toUnsignedInt(data[data.size - 6]) * 256 + java.lang.Byte.toUnsignedInt(
                    data[data.size - 5]
                )
            val temparature = nTemperature.toDouble() / 100.0f


            Log.d("data", "temperature : $temparature")

            // 배터리
            val nBatLevel =
                java.lang.Byte.toUnsignedInt(data[data.size - 4]) * 256 + java.lang.Byte.toUnsignedInt(
                    data[data.size - 3]
                )
            val battery = nBatLevel.toDouble() / 10000.0f

            Log.d("data", "battery : $battery")


//            Log.d("choco5732", data[0]
            var str = String(data)
            Log.d("data", "${count} 번째 불러온 데이터는 :$str")
            count++
            val finalData = ProtocolData(
                stx1 = stx1, stx2 = stx2, command = command, productId1 = productId1, productId2 = productId2,
                convertedProductId1 = String.format("0x%02X ", productId1),
                convertedProductId2 = String.format("0x%02X ", productId2),
                time1 = time1, time2 = time2, time3 = time3, time4 = time4, time5 = time5, time6 = time6,
                temperature = temparature, battery = battery, count = count,
                deviceName = deviceName
                )
            // 뷰모델에 데이터 업데이트
            viewModel.updateData(finalData)

        }
    }

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

        initView()
        initPermission()

        binding.mainHelloTv.setOnClickListener() {

        }


        binding.searchBle.setOnClickListener() {
            // 로티 애니메이션
            val animation = binding.searchBle
            animation.playAnimation()

            // 블루투스
            // Bluetooth매니저 : 블루투스 어댑터를 만들수 있다.
            val bleManager = requireActivity().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            // Bluetooth어댑터 : 장치검색,
            val bluetoothAdapter = bleManager.adapter

            // val targetAddress = "60:C0:BF:ED:5E:DF"

            var device : BluetoothDevice? = null

            val callback : ScanCallback = object : ScanCallback() {
                @SuppressLint("MissingPermission")
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
//            return true
//        }



//         BLE 리스트 초기화
        binding.searchBle.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                deviceAdapter.clearList()
                Toast.makeText(requireContext(), "목록이 초기화 됩니다.", Toast.LENGTH_SHORT).show()
                return true
            }
        })
    }

    private fun initPermission() {
        // 블투 스캔 권한 체크. 없으면 요구.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permssions,
                1
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permssionsFor29,
                1
            )
        }
    }

    private fun initView() = with(binding) {
        deviceRecyclerView.adapter = deviceAdapter
        deviceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


    @SuppressLint("MissingPermission", "ResourceAsColor")
    private fun scanLeDevice(callback: ScanCallback, scanner: BluetoothLeScanner) {
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                scanner.stopScan(callback)
                Toast.makeText(requireContext(), "스캔 종료 ", Toast.LENGTH_SHORT).show()
            }, SCAN_PERIOD)
            scanning = true
            scanner.startScan(callback)
            Toast.makeText(requireContext(), "스캔이 시작됩니다. ", Toast.LENGTH_SHORT).show()
        } else {
            scanning = false
            scanner.stopScan(callback)
            Toast.makeText(requireContext(), "스캔 종료 ", Toast.LENGTH_SHORT).show()
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
}
