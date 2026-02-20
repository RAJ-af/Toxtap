package com.himanshu.toxtap.ui.stability

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.himanshu.toxtap.databinding.FragmentStabilityBinding

class StabilityFragment : Fragment() {
    private var _binding: FragmentStabilityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStabilityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOpenBatterySettings.setOnClickListener {
            com.himanshu.toxtap.util.PermissionManager.requestIgnoreBatteryOptimization(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
