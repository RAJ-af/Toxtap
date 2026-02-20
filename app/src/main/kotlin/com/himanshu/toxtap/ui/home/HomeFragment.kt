package com.himanshu.toxtap.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.himanshu.toxtap.R
import com.himanshu.toxtap.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    private fun checkPermissions() {
        val hasOverlay = com.himanshu.toxtap.util.PermissionManager.hasOverlayPermission(requireContext())
        val hasAccessibility = com.himanshu.toxtap.util.PermissionManager.isAccessibilityServiceEnabled(requireContext())

        if (!hasOverlay || !hasAccessibility) {
            binding.tvHomeSubtitle.text = "Permissions required: ${if (!hasOverlay) "Overlay " else ""}${if (!hasAccessibility) "Accessibility" else ""}"
            binding.tvHomeSubtitle.setTextColor(android.graphics.Color.RED)
        } else {
            binding.tvHomeSubtitle.text = "Service is ready"
            binding.tvHomeSubtitle.setTextColor(android.graphics.Color.GREEN)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGestureControls.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_gestureSettingsFragment)
        }
        binding.btnShortcutMaker.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_shortcutMakerFragment)
        }
        binding.btnSettingsScanner.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsScannerFragment)
        }
        binding.btnStabilitySettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_stabilityFragment)
        }
        binding.btnAccessibilityActions.setOnClickListener {
            findNavController().navigate(R.id.shortcutMakerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
