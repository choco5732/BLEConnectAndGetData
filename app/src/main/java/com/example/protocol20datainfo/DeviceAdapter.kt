package com.example.protocol20datainfo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

}
class DeviceViewHolder(
    val binding: DeviceItemBinding,
    private val onClickItem: (Int, Device) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Device) = with(binding) {
        deviceName.setText((item.deviceName))
        deviceMac.setText(item.deviceMac)
        bluetoothImg.setImageResource(R.drawable.ic_bluetooth_blue)

        binding.root.setOnClickListener {
            onClickItem(
                position,
                item
            )
            Log.d("choco5732", "어댑터 : 리사이클러뷰 눌렀으예~?")
        }
    }
}
