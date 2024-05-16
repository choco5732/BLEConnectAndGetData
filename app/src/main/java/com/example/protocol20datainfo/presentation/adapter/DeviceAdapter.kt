package com.example.protocol20datainfo.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.protocol20datainfo.R
import com.example.protocol20datainfo.data.Device
import com.example.protocol20datainfo.databinding.DeviceItemBinding

class DeviceAdapter(
    private val deviceList: ArrayList<Device>,
    private val onClickItem: (Int, Device) -> Unit,
    private val onLongClickItem: (Int, Device) -> Unit
) : RecyclerView.Adapter<DeviceViewHolder>(
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(
            DeviceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem,
            onLongClickItem
        )
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val item = deviceList[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return deviceList.size
    }

    // 중복검사 및, 이름이 null아닌 디바이스만 추가
    fun addDevice(item: Device) {

        if(!deviceList.contains(item) && item.deviceName != ("null") && item.deviceName!!.contains("AGMS") ) {
            deviceList.add(item)
            notifyItemInserted(deviceList.size - 1)
        }
    }

    fun clearList() {
        deviceList.clear()
        notifyDataSetChanged()
    }

    fun reloadUi()  {
        notifyDataSetChanged()
    }

//    fun updateUiForConnect(mac: String) {
//        var findPosition = 0
//        for (i in 0 until deviceList.size) {
//            if (deviceList[i].deviceMac == mac) {
//                findPosition = i
//                Log.d("choco5732", "updateUI 안 : ${deviceList[i].toString()}")
//                break
//            }
//        }
//        deviceList[findPosition].isConnect = true
//        Log.d("choco5732", "updateUI 밖 : ${deviceList[findPosition].toString()}")
//        notifyDataSetChanged()
//    }
    fun updateUiForConnect(mac: String) {
        Log.d("choco5732", "받은 mac은 : $mac")
        var findPosition = 0
        for (i in 0 until deviceList.size) {
            if (deviceList[i].deviceMac == mac) {
                findPosition = i
                Log.d("choco5732", "updateUI 안 : ${deviceList[i].toString()}")
            }
        }

            deviceList[findPosition].isConnect = true
            Log.d("choco5732", "updateUI 밖 : ${deviceList[findPosition].toString()}")
            notifyDataSetChanged()

    }


    fun updateUiForDisconnect(mac: String) {
        var findPosition = 0
        for (i in 0 until deviceList.size){
            if (deviceList[i].deviceMac == mac) {
                findPosition = i
                break
            }
        }
        deviceList[findPosition].isConnect = false
        notifyDataSetChanged()
    }


}
class DeviceViewHolder(
    val binding: DeviceItemBinding,
    private val onClickItem: (Int, Device) -> Unit,
    private val onLongClickItem: (Int, Device) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Device) = with(binding) {
        deviceName.text = (item.deviceName)
        deviceMac.text = item.deviceMac
        if (item.isConnect){
            // true이면, 보라
            bluetoothImg.setImageResource(R.drawable.ic_bluetooth_purple)
        } else {
            // false이면, 파랑
            bluetoothImg.setImageResource(R.drawable.ic_bluetooth_blue)
        }

        binding.root.setOnClickListener {
//            bluetoothImg.setImageResource(R.drawable.ic_bluetooth_purple)
            onClickItem(
                position,
                item
            )
        }
        binding.root.setOnLongClickListener {
            onLongClickItem(
                position,
                item
            )
            return@setOnLongClickListener true
        }
    }
}
