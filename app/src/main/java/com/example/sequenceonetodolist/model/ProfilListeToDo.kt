package com.example.sequenceonetodolist.model

import java.io.Serializable

data class ProfilListeToDo(val login: String, val listesToDo: MutableList<ListeToDo> = mutableListOf()) : Serializable