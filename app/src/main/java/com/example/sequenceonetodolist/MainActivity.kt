package com.example.sequenceonetodolist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var pseudoField: EditText
    private lateinit var btnPseudoOk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pseudoField = findViewById(R.id.input_pseudo)
        btnPseudoOk = findViewById(R.id.btn_pseudo_ok)
        pseudoField.setText(getDefaultSharedPreferences(this).getString("defaultUser", ""))
        btnPseudoOk.setOnClickListener { goToChooseListActivity() }
    }

    private fun goToChooseListActivity() {
        val pseudo = pseudoField.text.toString()

        // Add user to known users if unknown
        val pseudoFile = File(this.filesDir, getString(R.string.users_filename))
        val fileJustCreated: Boolean = pseudoFile.createNewFile()
        val lines = pseudoFile.readLines()
        var found = false
        if (!fileJustCreated) {
            for (line in lines) {
                if (pseudo == line) found = true
            }
        }
        if (!found) {
            pseudoFile.appendText("$pseudo\n")
        }

        // set user to default pseudo
        val prefs = getDefaultSharedPreferences(this)
        with (prefs.edit()) {
            putString("defaultUser", pseudo)
            commit()
        }

        // Go to next activity and provide user
        val b = Bundle()
        b.putString("pseudo", pseudo)
        val toChooseListActivity = Intent(this, ChooseListActivity::class.java)
        toChooseListActivity.putExtras(b)
        startActivity(toChooseListActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_item -> startActivity(Intent(this, SettingsActivity::class.java))
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}
