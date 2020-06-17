package com.example.sequenceonetodolist.data

import android.util.Log
import com.example.sequenceonetodolist.model.ItemResponse
import com.example.sequenceonetodolist.model.ItemToDo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataProvider(val apiUrl: String) {
    // private val API_URL = "http://tomnab.fr/todo-api/"
    // private val API_URL = "http://todo-api.localhost/api/"
    // private val API_URL = "http://192.168.1.52/api/"

    private val service = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TodoApiService::class.java)

    suspend fun login(login: String, password: String) = service.authenticate(login, password).hash
    suspend fun users(hash: String) = service.users(hash).users
    suspend fun lists(hash: String) = service.lists(hash).lists
    suspend fun addList(name: String, hash: String): Long? = service.addList(name, hash).list?.id
    suspend fun items(listId: Long, hash: String) = service.items(listId, hash).items.toItemToDo()
    suspend fun addItem(listId: Long, name: String, hash: String): Long? = service.addItem(listId, name, hash).item?.id
    suspend fun toggleItem(listId: Long, itemId: Long, boolean: Boolean, hash: String): Boolean {
        val check = if (boolean) 1 else 0
        Log.e("App", "Valeur de check : $check")

        return service.toggleItem(listId = listId, itemId = itemId, check = check, hash = hash).success
    }

    private fun List<ItemResponse>.toItemToDo() = this.map { ItemToDo(it.id, it.label, it.checked == 1) }

}