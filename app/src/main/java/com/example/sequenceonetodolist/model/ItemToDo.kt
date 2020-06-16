package com.example.sequenceonetodolist.model

import java.io.Serializable

data class ItemToDo(val id: Long, val description: String, var fait: Boolean = false) : Serializable