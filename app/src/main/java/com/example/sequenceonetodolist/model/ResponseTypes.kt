package com.example.sequenceonetodolist.model

data class ItemResponse(val id: Long, val label: String, val checked: Int)
data class TodoListResponse(val id: Long, val label: String)
data class AuthenticateResponse(val version: Int, val success: Boolean, val status: Int, val hash: String? = null)
data class UsersResponse(val version: Int, val success: Boolean, val status: Int, val users: List<User>)
data class ListsResponse(val version: Int, val success: Boolean, val status: Int, val lists: List<TodoListResponse>)
data class ListCreatedResponse(val version: Int, val success: Boolean, val status: Int, val list: TodoListResponse)
data class ItemCreatedResponse(val version: Int, val success: Boolean, val status: Int, val item :ItemResponse)
data class ItemsResponse(val version: Int, val success: Boolean, val status: Int, val items: List<ItemResponse>)
typealias ItemToggleResponse = ItemCreatedResponse