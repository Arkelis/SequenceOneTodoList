package com.example.sequenceonetodolist.model

import java.io.Serializable

data class ItemToDo(val description: String, var fait: Boolean = false) : Serializable