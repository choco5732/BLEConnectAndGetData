package com.example.protocol20datainfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.protocol20datainfo.databinding.DeviceItemBinding

class DeviceAdapter(
    private val onClickItem: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {

    var items = ArrayList<Device>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DeviceViewHolder(
            DeviceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class DeviceViewHolder(
        private val binding: DeviceItemBinding,
        private val onClickItem: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item : Device
        ) {

        }


    }
}
