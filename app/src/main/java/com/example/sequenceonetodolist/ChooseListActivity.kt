package com.example.sequenceonetodolist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sequenceonetodolist.model.TodoListResponse
import kotlinx.coroutines.launch

/**
 * Activité permettant à l'utilisateur de créer ou d'afficher une liste.
 */
class ChooseListActivity : BaseActivity() {

    lateinit var btnOkList: Button
    lateinit var listField: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var userLists: MutableList<TodoListResponse>
    lateinit var userHash: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_list)

        // Récupération du pseudo transmis par la MainActivity
        val bundle = this.intent.extras
        userHash = bundle!!.getString("userHash")!!
        recyclerView = findViewById(R.id.list)
        userLists = emptyList<TodoListResponse>().toMutableList()
        recyclerView.adapter = ListsAdapter(userLists, this@ChooseListActivity)
        recyclerView.layoutManager = LinearLayoutManager(this@ChooseListActivity)

        activityScope.launch {
            userLists.addAll(dataProvider.lists(userHash))
            // Création de la recycler view avec les items de l'utilisateur
            recyclerView.adapter!!.notifyDataSetChanged()
        }


        // Listener bouton
        btnOkList = findViewById(R.id.btn_list_ok)
        btnOkList.setOnClickListener { addNewList() }

        listField = findViewById(R.id.input_list)
    }

    /**
     * Ajoute la liste au profil de l'utilisateur si une liste du même nom n'existe pas déjà
     * puis met à jour le fichier json
     */
    private fun addNewList() {
        val listName = listField.text.toString()
        activityScope.launch {
            val listId = dataProvider.addList(listName, userHash)
            if (listId != null) {
                userLists.add(TodoListResponse(listId, listName))
                recyclerView.adapter!!.notifyDataSetChanged()
            } else {
                Toast.makeText(this@ChooseListActivity, "Impossible d'ajouter cette liste.", Toast.LENGTH_LONG)
            }
        }
    }

    /**
     * Ouvre l'activité ShowListActivity en passant le pseudo et le numéro de liste à afficher.
     */
    fun openList(listId: Long) {
        val b = Bundle()
        b.putString("userHash", userHash)
        b.putLong("list_id", listId)
        val toShowListActivity = Intent(this, ShowListActivity::class.java)
        toShowListActivity.putExtras(b)
        startActivity(toShowListActivity)
    }
}

class ListsAdapter(private val dataSet: List<TodoListResponse>, private val activity: ChooseListActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataSet[position]
        (holder as ItemViewHolder).bind(item.label)
        holder.itemView.setOnClickListener { activity.openList(item.id) }
    }

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.list_item_label)

        fun bind(label: String) {
            textView.text = label
        }
    }

}