package com.example.sequenceonetodolist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import java.io.File

/**
 * Activité gérant les préférences de l'application.
 *
 * La méthode onCreate() et la classe SettingsFragment ont été générés automatiquement.
 * La méthodes onClearUsers gère la suppression des utilisateurs.
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    /**
     * Supprime les fichiers json contenant les données utilisateurs en se basant sur les pseudos
     * présents dans le fichier knownUsers.
     */
    fun onClearUsers(view: View) {
        // Récupère la liste des pseudos
        val userFile = File(filesDir, getString(R.string.users_filename))

        // Si le fichier n'existe pas, c'est qu'il n'y a pas de données donc rien à faire
        if (!userFile.exists()) return

        // Pour chaque pseudo
        for (line in userFile.readLines()) {
            // On supprime le fichier json correspondant
            val listFile = File(filesDir, "$line.json")
            if (listFile.exists())
                listFile.delete()
        }

        // Puis on supprime le fichier des utilisateurs connus
        userFile.delete()

        // On réinitialise le paramètre d'utilisateur par défaut.
        with (getDefaultSharedPreferences(this).edit()) {
            putString("defaultUser", "Pseudo")
            commit()
        }
    }
}