package com.example.sequenceonetodolist

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

/**
 * Activité de base qui gère le menu Android
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    /**
     * Au clic sur l'item de menu, redirige vers l'écran de préférences.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_item -> startActivity(Intent(this, SettingsActivity::class.java))
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}