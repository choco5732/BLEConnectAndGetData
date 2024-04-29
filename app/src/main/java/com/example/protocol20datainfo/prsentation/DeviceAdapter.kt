package com.example.protocol20datainfo.prsentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.protocol20datainfo.R
import com.example.protocol20datainfo.databinding.DeviceItemBinding

class DeviceAdapter(
    private val deviceList: ArrayList<Device>,
    private val onClickItem: (Int, Device) -> Unit
) : RecyclerView.Adapter<DeviceViewHolder>(
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(
            DeviceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem
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

        if(!deviceList.contains(item) && item.deviceName != ("null")) {
            deviceList.add(item)
            notifyItemInserted(deviceList.size - 1)
        }
    }

    fun clearList() {
        deviceList.clear()
        notifyDataSetChanged()
    }


}
class DeviceViewHolder(
    val binding: DeviceItemBinding,
    private val onClickItem: (Int, Device) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Device) = with(binding) {
        deviceName.text = (item.deviceName)
        deviceMac.text = item.deviceMac
        bluetoothImg.setImageResource(R.drawable.ic_bluetooth_blue)

        binding.root.setOnClickListener {
            onClickItem(
                position,
                item
            )
        }
    }
}
