package com.example.sequenceonetodolist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.sequenceonetodolist.data.DataProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Activité de base qui gère le menu Android
 */
open class BaseActivity : AppCompatActivity() {

    val activityScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    lateinit var dataProvider: DataProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataProvider = DataProvider(PreferenceManager.getDefaultSharedPreferences(this).getString("apiUrl", "http://tomnab.fr/todo-api/api/")!!)
    }


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