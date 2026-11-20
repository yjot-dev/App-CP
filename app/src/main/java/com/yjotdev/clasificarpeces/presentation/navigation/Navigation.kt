package com.yjotdev.clasificarpeces.presentation.navigation

import android.graphics.Color
import android.util.TypedValue
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.yjotdev.clasificarpeces.MainActivity
import com.yjotdev.clasificarpeces.R

/**
 * Configura la navegación de la actividad principal, incluyendo el soporte para
 * Edge-to-Edge en Android 15 (API 35+), el manejo de WindowInsets y la vinculación
 * de la Toolbar con el NavController.
 */
fun MainActivity.setupNavigation() {
    // 1. Configurar Edge-to-Edge e Insets (Unificado para Android 15 / API 35+)
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
        navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
    )
    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
        val safeInsets = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        val isToolbarVisible = binding.includeToolbar.toolbar.isVisible

        val tv = TypedValue()
        var actionBarHeight = 0
        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }

        if (isToolbarVisible) {
            // Cuando la Toolbar está visible, se extiende bajo la barra de estado
            v.setPadding(safeInsets.left, 0, safeInsets.right, safeInsets.bottom)
            binding.includeToolbar.toolbar.setPadding(0, safeInsets.top, 0, 0)
            binding.includeToolbar.toolbar.layoutParams.height = safeInsets.top + actionBarHeight
        } else {
            // Cuando la Toolbar no está visible (ej. StartFragment), la vista raíz aplica el padding superior
            v.setPadding(safeInsets.left, safeInsets.top, safeInsets.right, safeInsets.bottom)
            binding.includeToolbar.toolbar.setPadding(0, 0, 0, 0)
        }
        insets
    }
    // 2. Configurar el NavController
    val navHostFragment = supportFragmentManager
        .findFragmentById(R.id.fragmentNav) as NavHostFragment
    val navController = navHostFragment.navController
    // 3. Vincular Toolbar con Navigation
    val appBarConfiguration = AppBarConfiguration(navController.graph)
    binding.includeToolbar.toolbar.setupWithNavController(navController, appBarConfiguration)
    // 4. Visibilidad condicional del Toolbar y actualización de Insets
    navController.addOnDestinationChangedListener { _, destination, _ ->
        binding.includeToolbar.toolbar.isVisible = destination.id != R.id.startFragment
        ViewCompat.requestApplyInsets(binding.root)
    }
}