package com.himanshu.toxtap

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.himanshu.toxtap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return

        val actionType = intent.getStringExtra("action_type")
        val actionData = intent.getStringExtra("action_data")
        val screenId = intent.getIntExtra("screen_id", -1)

        if (actionType != null) {
            val type = com.himanshu.toxtap.model.ActionType.valueOf(actionType)
            com.himanshu.toxtap.service.ActionExecutor.execute(this, com.himanshu.toxtap.model.GestureAction(type, actionData, "Shortcut"))
            finish()
        } else if (screenId != -1) {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navHostFragment.navController.navigate(screenId)
        }
    }
}
