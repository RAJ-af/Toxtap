package com.himanshu.toxtap.ui.scanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.himanshu.toxtap.databinding.ItemSettingBinding
import com.himanshu.toxtap.model.ScannedSetting

class SettingsAdapter(
    private var settings: List<ScannedSetting>,
    private val onItemClick: (ScannedSetting) -> Unit,
    private val onShortcutClick: (ScannedSetting) -> Unit
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSettingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val setting = settings[position]
        holder.binding.tvLabel.text = setting.label
        holder.binding.tvActivity.text = setting.activityName
        holder.binding.root.setOnClickListener { onItemClick(setting) }
        holder.binding.btnShortcut.setOnClickListener { onShortcutClick(setting) }
    }

    override fun getItemCount() = settings.size

    fun updateData(newSettings: List<ScannedSetting>) {
        settings = newSettings
        notifyDataSetChanged()
    }
}
