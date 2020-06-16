package com.example.sequenceonetodolist.data

import com.example.sequenceonetodolist.model.ItemResponse
import com.example.sequenceonetodolist.model.ItemToDo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataProvider {
    // private const val API_URL = "http://tomnab.fr/todo-api/"
    // private const val API_URL = "http://todo-api.localhost/api/"
    private const val API_URL = "http://192.168.1.52/api/"

    private val service = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TodoApiService::class.java)

    suspend fun login(login: String, password: String) = service.authenticate(login, password).hash
    suspend fun users(hash: String) = service.users(hash).users
    suspend fun lists(hash: String) = service.lists(hash).lists
    suspend fun addList(name: String, hash: String): Long? = service.addList(name, hash).list?.id
    suspend fun items(listId: Long, hash: String) = service.items(listId, hash).items.toItemToDo()
    suspend fun addItem(listId: Long, name: String, hash: String): Long? = service.addItem(listId, name, hash).item?.id
    suspend fun toggleItem(listId: Long, itemId: Long, label: String, boolean: Boolean, hash: String): Boolean {
        val check = if (boolean) 1 else 0
        return service.toggleItem(listId, itemId, label, check, hash).success
    }

    private fun List<ItemResponse>.toItemToDo() = this.map { ItemToDo(it.id, it.label, it.check) }

}
