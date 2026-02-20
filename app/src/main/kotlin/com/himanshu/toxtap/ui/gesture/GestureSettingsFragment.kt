package com.himanshu.toxtap.ui.gesture

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.himanshu.toxtap.databinding.FragmentGestureSettingsBinding
import com.himanshu.toxtap.databinding.ItemGestureConfigBinding
import com.himanshu.toxtap.model.ActionType
import com.himanshu.toxtap.model.GestureAction
import com.himanshu.toxtap.model.GestureType
import com.himanshu.toxtap.service.ToxTapForegroundService
import com.himanshu.toxtap.util.PermissionManager
import com.himanshu.toxtap.viewmodel.GestureViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GestureSettingsFragment : Fragment() {
    private var _binding: FragmentGestureSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GestureViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGestureSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isOverlayEnabled.collectLatest { enabled ->
                binding.switchOverlay.isChecked = enabled
            }
        }

        binding.switchOverlay.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!PermissionManager.hasOverlayPermission(requireContext())) {
                    PermissionManager.requestOverlayPermission(requireContext())
                    binding.switchOverlay.isChecked = false
                } else if (!PermissionManager.isAccessibilityServiceEnabled(requireContext())) {
                    Toast.makeText(context, "Please enable Accessibility Service", Toast.LENGTH_LONG).show()
                    PermissionManager.requestAccessibilityPermission(requireContext())
                    binding.switchOverlay.isChecked = false
                } else {
                    viewModel.setOverlayEnabled(true)
                    startForegroundService()
                }
            } else {
                viewModel.setOverlayEnabled(false)
            }
        }

        setupGestureList()
    }

    private fun startForegroundService() {
        val intent = Intent(requireContext(), ToxTapForegroundService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }

    private fun setupGestureList() {
        GestureType.values().forEach { type ->
            val itemBinding = ItemGestureConfigBinding.inflate(layoutInflater, binding.containerGestures, false)
            itemBinding.tvGestureName.text = type.name.replace("_", " ")

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getGestureAction(type).collectLatest { action ->
                    itemBinding.tvAssignedAction.text = "Assigned: ${action.label}"
                }
            }

            itemBinding.root.setOnClickListener {
                showActionPickerDialog(type)
            }

            binding.containerGestures.addView(itemBinding.root)
        }
    }

    private fun showActionPickerDialog(gestureType: GestureType) {
        val actions = ActionType.values().filter { it != ActionType.LAUNCH_APP && it != ActionType.OPEN_SETTINGS_SCREEN }
        val actionLabels = actions.map { it.name.replace("_", " ") }.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle("Select Action for ${gestureType.name}")
            .setItems(actionLabels) { _, which ->
                val selectedAction = actions[which]
                viewModel.saveGestureAction(gestureType, GestureAction(selectedAction, null, actionLabels[which]))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
