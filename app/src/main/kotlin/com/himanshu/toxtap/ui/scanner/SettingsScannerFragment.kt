package com.himanshu.toxtap.ui.scanner

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.himanshu.toxtap.databinding.FragmentSettingsScannerBinding
import com.himanshu.toxtap.model.ActionType
import com.himanshu.toxtap.model.GestureAction
import com.himanshu.toxtap.util.ShortcutHelper
import com.himanshu.toxtap.viewmodel.ScannerViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsScannerFragment : Fragment() {
    private var _binding: FragmentSettingsScannerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScannerViewModel by viewModels()
    private lateinit var adapter: SettingsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SettingsAdapter(emptyList(), { setting ->
            val intent = Intent()
            intent.setClassName(setting.packageName, setting.activityName)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Cannot open activity", Toast.LENGTH_SHORT).show()
            }
        }, { setting ->
            ShortcutHelper.createPinnedShortcut(requireContext(), GestureAction(
                ActionType.OPEN_SETTINGS_SCREEN,
                setting.activityName,
                setting.label
            ))
        })

        binding.rvSettings.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.settings.collectLatest { settings ->
                adapter.updateData(settings)
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                val filtered = viewModel.settings.value.filter {
                    it.label.lowercase().contains(query) || it.activityName.lowercase().contains(query)
                }
                adapter.updateData(filtered)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        viewModel.scan()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
