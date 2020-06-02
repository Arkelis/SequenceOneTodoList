package com.example.sequenceonetodolist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sequenceonetodolist.model.ItemToDo
import com.example.sequenceonetodolist.model.ListeToDo
import com.example.sequenceonetodolist.model.ProfilListeToDo
import com.google.gson.Gson
import java.io.File

class ShowListActivity : AppCompatActivity() {

    lateinit var userFile: File
    lateinit var profil: ProfilListeToDo
    lateinit var recyclerView: RecyclerView
    lateinit var btnOkItem: Button
    lateinit var itemField: TextView
    lateinit var list: ListeToDo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)


        // Récupération du pseudo transmis par la MainActivity
        val bundle = this.intent.extras
        val pseudo = bundle!!.getString("pseudo")
        val listPosition = bundle!!.getInt("list_position")
        val gson = Gson()
        userFile = File(this.filesDir, "$pseudo.json")
        profil = gson.fromJson(userFile.readText(), ProfilListeToDo::class.java)
        list = profil.listesToDo[listPosition]
        Log.e("App", list.toString())

        // Création de la recycler view avec les items de l'utilisateur
        recyclerView = findViewById(R.id.todo_list)
        val adapter = TodoListAdapter(list, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Listener bouton
        btnOkItem = findViewById(R.id.btn_item_ok)
        btnOkItem.setOnClickListener { addNewItem() }

        itemField = findViewById(R.id.input_item)
    }

    fun addNewItem() {
        val itemDescription = itemField.text.toString()
        for (item in list.getItems()) {
            if (item.description == itemDescription) {
                Toast.makeText(this, "Ce nom est déjà pris.", Toast.LENGTH_SHORT).show()
                return
            }
        }
        list.ajouterItem(ItemToDo(itemDescription))
        userFile.writeText(Gson().toJson(profil, ProfilListeToDo::class.java))
        recyclerView.adapter?.notifyDataSetChanged()
    }

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
            checkBox.setOnCheckedChangeListener {_, boolean: Boolean -> activity.toggleItem(position, boolean) }
        }
    }

}