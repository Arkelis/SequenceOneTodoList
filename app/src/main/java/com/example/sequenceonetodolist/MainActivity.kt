package com.example.sequenceonetodolist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.sequenceonetodolist.data.DataProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File

/**
 * Activité principale
 */
class MainActivity : BaseActivity() {
    private lateinit var pseudoField: EditText
    private lateinit var btnPseudoOk: Button

    val activityScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pseudoField = findViewById(R.id.input_pseudo)
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

        // Ajout de l'utilisateur à la liste des utilisateurs connus s'il n'est pas déjà présent
        val pseudoFile = File(this.filesDir, getString(R.string.users_filename))
        activityScope.launch {
            val hash = DataProvider.login("guillaume", "123")
            Log.e("App", hash.toString())
        }
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

        // On met à jour la préférence de pseudo par défaut
        val prefs = getDefaultSharedPreferences(this)
        with (prefs.edit()) {
            putString("defaultUser", pseudo)
            commit()
        }

        // On crée le bundle avec le pseudo puis on lance l'activité
        val b = Bundle()
        b.putString("pseudo", pseudo)
        val toChooseListActivity = Intent(this, ChooseListActivity::class.java)
        toChooseListActivity.putExtras(b)
        startActivity(toChooseListActivity)
    }
}
