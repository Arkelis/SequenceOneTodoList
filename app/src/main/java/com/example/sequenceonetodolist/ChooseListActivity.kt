package com.example.sequenceonetodolist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sequenceonetodolist.model.ListeToDo
import com.example.sequenceonetodolist.model.ProfilListeToDo
import com.google.gson.Gson
import java.io.File

/**
 * Activité permettant à l'utilisateur de créer ou d'afficher une liste.
 */
class ChooseListActivity : BaseActivity() {

    lateinit var btnOkList: Button
    lateinit var listField: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var profil: ProfilListeToDo
    lateinit var userFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_list)

        // Récupération du pseudo transmis par la MainActivity
        val bundle = this.intent.extras
        var pseudo = bundle?.getString("pseudo")
        if (pseudo == null) {
            pseudo = "defaultUser"
        }
        val gson = Gson()
        userFile = File(this.filesDir, "$pseudo.json")
        val fileCreated = userFile.createNewFile()
        if (!fileCreated) {
            profil = gson.fromJson(userFile.readText(), ProfilListeToDo::class.java)
        } else {
            profil = ProfilListeToDo(pseudo)
            userFile.writeText(gson.toJson(profil, ProfilListeToDo::class.java))
        }
        Log.e("App", profil.toString())

        // Création de la recycler view avec les items de l'utilisateur
        recyclerView = findViewById(R.id.list)
        val dataSet = profil.listesToDo
        val adapter = ListsAdapter(dataSet, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Listener bouton
        btnOkList = findViewById(R.id.btn_list_ok)
        btnOkList.setOnClickListener { addNewList() }

        listField = findViewById(R.id.input_list)
    }

    /**
     * Ajoute la liste au profil de l'utilisateur si une liste du même nom n'existe pas déjà
     * puis met à jour le fichier json
     */
    fun addNewList() {
        val listName = listField.text.toString()
        for (liste in profil.listesToDo) {
            if (liste.titre == listName) {
                Toast.makeText(this, "Ce nom est déjà pris.", Toast.LENGTH_SHORT).show()
                return
            }
        }
        profil.listesToDo.add(ListeToDo(listName))
        userFile.writeText(Gson().toJson(profil, ProfilListeToDo::class.java))
        recyclerView.adapter?.notifyDataSetChanged()
    }

    /**
     * Ouvre l'activité ShowListActivity en passant le pseudo et le numéro de liste à afficher.
     */
    fun openList(position: Int) {
        val b = Bundle()
        b.putString("pseudo", profil.login)
        b.putInt("list_position", position)
        val toShowListActivity = Intent(this, ShowListActivity::class.java)
        toShowListActivity.putExtras(b)
        startActivity(toShowListActivity)
    }
}

class ListsAdapter(private val dataSet: List<ListeToDo>, private val activity: ChooseListActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataSet[position]
        (holder as ItemViewHolder).bind(item.titre)
        holder.itemView.setOnClickListener { activity.openList(position) }
    }

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.list_item_label)

        fun bind(label: String) {
            textView.text = label
        }
    }

}