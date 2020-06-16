package com.example.sequenceonetodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sequenceonetodolist.data.DataProvider
import com.example.sequenceonetodolist.model.ItemToDo
import kotlinx.coroutines.launch

/**
 * Activité affichant une todo list
 */
class ShowListActivity : BaseActivity() {

    lateinit var userHash: String
    lateinit var recyclerView: RecyclerView
    lateinit var btnOkItem: Button
    lateinit var itemField: TextView
    lateinit var items: MutableList<ItemToDo>
    private var listId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)


        // Récupération du pseudo et de l'indice de la liste transmis par la ChooseListActivity
        val bundle = this.intent.extras
        userHash = bundle!!.getString("userHash")!!
        listId = bundle.getLong("list_id")

        activityScope.launch {
            items = DataProvider.items(listId, userHash).toMutableList()
            // Création de la recycler view avec les items de la todo list
            recyclerView = findViewById(R.id.todo_list)
            // on passe l'activité à l'adapter pour que celui-ci ait une référence vers la méthode
            // toggleItem()
            val adapter = TodoListAdapter(items, this@ShowListActivity)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@ShowListActivity)
        }

        // Listener bouton
        btnOkItem = findViewById(R.id.btn_item_ok)
        btnOkItem.setOnClickListener { addNewItem() }

        itemField = findViewById(R.id.input_item)
    }

    /**
     * Ajoute l'item s'il n'existe pas déjà et met à jour le fichier json
     */
    private fun addNewItem() {
        val itemDescription = itemField.text.toString()
        activityScope.launch {
            val itemId = DataProvider.addItem(listId, itemDescription, userHash)
            if (itemId != null) {
                items.add(ItemToDo(itemId, itemDescription))
                recyclerView.adapter!!.notifyDataSetChanged()
            } else {
                Toast.makeText(this@ShowListActivity, "Impossible d'ajouter cet item.", Toast.LENGTH_LONG)
            }
        }
    }

    /**
     * Coche ou décoche un item (change sa valeur de l'attribut "fait") et met à jour le fichier
     * json
     */
    fun toggleItem(position: Int, boolean: Boolean) {
        activityScope.launch {
            val success = DataProvider.toggleItem(listId, items[position].id!!, items[position].description, boolean, userHash)
            if (success) {
                items[position].fait = boolean
                recyclerView.adapter!!.notifyDataSetChanged()
            } else {
                Toast.makeText(this@ShowListActivity, "Impossible de modifier cet item.", Toast.LENGTH_LONG)
            }
        }
    }
}

class TodoListAdapter(private val dataSet: List<ItemToDo>, private val activity: ShowListActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.todo_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataSet[position]
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