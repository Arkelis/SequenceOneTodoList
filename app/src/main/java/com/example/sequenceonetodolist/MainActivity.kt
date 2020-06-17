package com.example.sequenceonetodolist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import kotlinx.coroutines.launch

/**
 * Activité principale
 */
class MainActivity : BaseActivity() {
    private lateinit var pseudoField: EditText
    private lateinit var passwordField: EditText
    private lateinit var btnPseudoOk: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pseudoField = findViewById(R.id.input_pseudo)
        passwordField = findViewById(R.id.input_password)
        btnPseudoOk = findViewById(R.id.btn_pseudo_ok)

        // On préremplit le champs de texte avec la valeur des préférences
        pseudoField.setText(getDefaultSharedPreferences(this).getString("defaultUser", ""))
        btnPseudoOk.setOnClickListener { goToChooseListActivity() }
    }

    /**
     * Lance la ChooseListActivity en lui passant le pseudo renseigné.
     */
    private fun goToChooseListActivity() {
        val pseudo = pseudoField.text.toString()
        val password = passwordField.text.toString()

        activityScope.launch {
            val hash = dataProvider.login(pseudo, password)
            // On crée le bundle avec le pseudo puis on lance l'activité
            val b = Bundle()
            b.putString("userHash", hash)
            val toChooseListActivity = Intent(this@MainActivity, ChooseListActivity::class.java)
            toChooseListActivity.putExtras(b)
            startActivity(toChooseListActivity)
        }
//        val fileJustCreated: Boolean = pseudoFile.createNewFile()
//        val lines = pseudoFile.readLines()
//        var found = false
//        if (!fileJustCreated) {
//            for (line in lines) {
//                if (pseudo == line) found = true
//            }
//        }
//        if (!found) {
//            pseudoFile.appendText("$pseudo\n")
//        }

        // On met à jour la préférence de pseudo par défaut
        val prefs = getDefaultSharedPreferences(this)
        with (prefs.edit()) {
            putString("defaultUser", pseudo)
            commit()
        }

    }
}
