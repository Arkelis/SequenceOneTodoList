package com.example.sequenceonetodolist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sequenceonetodolist.model.ListeToDo
import com.example.sequenceonetodolist.model.ProfilListeToDo
import com.google.gson.Gson
import java.io.File

/**
 * Activité affichant une todo list
 */
class ShowListActivity : BaseActivity() {

    lateinit var userFile: File
    lateinit var profil: ProfilListeToDo
    lateinit var recyclerView: RecyclerView
    lateinit var btnOkItem: Button
    lateinit var itemField: TextView
    lateinit var list: ListeToDo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)


        // Récupération du pseudo et de l'indice de la liste transmis par la ChooseListActivity
        val bundle = this.intent.extras
        val pseudo = bundle!!.getString("pseudo")
        val listPosition = bundle!!.getInt("list_position")
        val gson = Gson()

        // En se basant sur ces deux informations on récupère la bonne liste dans le fichier json
        userFile = File(this.filesDir, "$pseudo.json")
        profil = gson.fromJson(userFile.readText(), ProfilListeToDo::class.java)
        list = profil.listesToDo[listPosition]

        // Création de la recycler view avec les items de la todo list
        recyclerView = findViewById(R.id.todo_list)
        // on passe l'activité à l'adapter pour que celui-ci ait une référence vers la méthode
        // toggleItem()
        val adapter = TodoListAdapter(list, this)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Listener bouton
        btnOkItem = findViewById(R.id.btn_item_ok)
        btnOkItem.setOnClickListener { addNewItem() }

        itemField = findViewById(R.id.input_item)
    }

    /**
     * Ajoute l'item s'il n'existe pas déjà et met à jour le fichier json
     */
    fun addNewItem() {
        val itemDescription = itemField.text.toString()
        val ajouté: Boolean = list.ajouterItem(itemDescription)
        if (ajouté) {
            userFile.writeText(Gson().toJson(profil, ProfilListeToDo::class.java))
            recyclerView.adapter?.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "Ce nom est déjà pris.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Coche ou décoche un item (change sa valeur de l'attribut "fait") et met à jour le fichier
     * json
     */
    fun toggleItem(position: Int, boolean: Boolean) {
        list.toggleItem(position, boolean)
        userFile.writeText(Gson().toJson(profil, ProfilListeToDo::class.java))
        recyclerView.adapter?.notifyDataSetChanged()
        Log.e("App", list.toString())
    }
}

class TodoListAdapter(private val dataSet: ListeToDo, private val activity: ShowListActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.todo_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = dataSet.getItems().size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataSet.getItems()[position]
        (holder as ItemViewHolder).bind(item.description, item.fait, activity, position)
    }

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.todo_item)

        fun bind(description: String, fait: Boolean, activity: ShowListActivity, position: Int) {
            checkBox.text = description
            checkBox.isChecked = fait
            // appelle la méthode toggleItem() de l'activité en cas de (dé)cochage d'un item.
            checkBox.setOnCheckedChangeListener {_, boolean: Boolean -> activity.toggleItem(position, boolean) }
        }
    }

}