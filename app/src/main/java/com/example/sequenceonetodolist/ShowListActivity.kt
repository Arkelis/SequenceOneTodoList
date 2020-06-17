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
import com.example.sequenceonetodolist.model.ItemToDo
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
        items = emptyList<ItemToDo>().toMutableList()
        recyclerView = findViewById(R.id.todo_list)
        recyclerView.adapter = TodoListAdapter(items, this@ShowListActivity)
        recyclerView.layoutManager = LinearLayoutManager(this@ShowListActivity)

        activityScope.launch {
            items.addAll(dataProvider.items(listId, userHash))
            // Création de la recycler view avec les items de la todo list
            // on passe l'activité à l'adapter pour que celui-ci ait une référence vers la méthode
            // toggleItem()
            recyclerView.adapter!!.notifyDataSetChanged()
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
            try {
                val itemId = dataProvider.addItem(listId, itemDescription, userHash)!!
                items.add(ItemToDo(itemId, itemDescription))
                recyclerView.adapter!!.notifyDataSetChanged()
            } catch (e: Exception) {
                when (e) {
                    is HttpException, is NullPointerException -> {
                        Log.e("App" , e.stackTrace.toString())
                        Log.e("App", e.message)
                        Toast.makeText(this@ShowListActivity, "Impossible d'ajouter cet item.", Toast.LENGTH_LONG)
                    }
                    else -> throw e
                }
            }
        }
    }

    /**
     * Coche ou décoche un item (change sa valeur de l'attribut "fait") et met à jour le fichier
     * json
     */
    fun toggleItem(position: Int, boolean: Boolean) {
        activityScope.launch {
            try {
                dataProvider.toggleItem(listId, items[position].id!!, boolean, userHash)
                items[position].fait = boolean
                recyclerView.adapter!!.notifyDataSetChanged()
            } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    Log.e("App" , e.stackTrace.toString())
                    Log.e("App", e.message)
                    Toast.makeText(this@ShowListActivity, "Impossible de modifier cet item.", Toast.LENGTH_LONG)
                }
                else -> throw e
            }
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