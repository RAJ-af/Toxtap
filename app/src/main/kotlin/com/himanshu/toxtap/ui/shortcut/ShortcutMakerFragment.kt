package com.himanshu.toxtap.ui.shortcut

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.himanshu.toxtap.R
import com.himanshu.toxtap.databinding.FragmentShortcutMakerBinding
import com.himanshu.toxtap.model.ActionType
import com.himanshu.toxtap.model.GestureAction
import com.himanshu.toxtap.util.ShortcutHelper

class ShortcutMakerFragment : Fragment() {
    private var _binding: FragmentShortcutMakerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShortcutMakerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAccessibilityActions()
        setupScreenShortcuts()
    }

    private fun setupAccessibilityActions() {
        val actions = listOf(
            ActionType.BACK, ActionType.HOME, ActionType.RECENTS,
            ActionType.NOTIFICATIONS, ActionType.LOCK_SCREEN
        )

        actions.forEach { actionType ->
            val label = actionType.name.replace("_", " ")
            val button = createShortcutButton(label) {
                ShortcutHelper.createPinnedShortcut(requireContext(), GestureAction(actionType, null, label))
            }
            binding.containerAccessibilityShortcuts.addView(button)
        }
    }

    private fun setupScreenShortcuts() {
        val screens = listOf(
            "Gesture Settings" to R.id.gestureSettingsFragment,
            "Settings Scanner" to R.id.settingsScannerFragment,
            "Stability Settings" to R.id.stabilityFragment
        )

        screens.forEach { (label, screenId) ->
            val button = createShortcutButton(label) {
                ShortcutHelper.createScreenShortcut(requireContext(), label, screenId)
            }
            binding.containerScreenShortcuts.addView(button)
        }
    }

    private fun createShortcutButton(label: String, onClick: () -> Unit): View {
        val card = com.google.android.material.card.MaterialCardView(requireContext()).apply {
            layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                setMargins(0, 8, 0, 8)
            }
            setPadding(16, 16, 16, 16)
            setOnClickListener { onClick() }

            val tv = TextView(requireContext()).apply {
                text = "Pin: $label"
                textSize = 16f
                setPadding(32, 32, 32, 32)
            }
            addView(tv)
        }
        return card
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
