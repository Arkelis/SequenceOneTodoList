package com.example.sequenceonetodolist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var pseudoField: EditText
    private lateinit var btnPseudoOk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pseudoField = findViewById(R.id.input_pseudo)
        btnPseudoOk = findViewById(R.id.btn_pseudo_ok)
        btnPseudoOk.setOnClickListener { goToChooseListActivity() }
    }

    private fun goToChooseListActivity() {
        val pseudo = pseudoField.text.toString()

        // Add user to known users if unknown
        val pseudoFile = File(this.filesDir, "knownUsers")
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

        // Go to next activity and provide user
        val b = Bundle()
        b.putString("pseudo", pseudo)
        val toChooseListActivity = Intent(this, ChooseListActivity::class.java)
        toChooseListActivity.putExtras(b)
        startActivity(toChooseListActivity)
    }
}
