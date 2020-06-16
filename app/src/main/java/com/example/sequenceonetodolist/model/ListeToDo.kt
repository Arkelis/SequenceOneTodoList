package com.example.sequenceonetodolist.model

import java.io.Serializable

class ListeToDo(val titre: String, private val items: MutableList<ItemToDo> = mutableListOf()) : Serializable {

    /**
     * Vérifie s'il existe un item de description `description` dans la liste
     * Renvoie true si c'est le cas, false sinon.
     * @param descriptionItem la description de l'item à chercher
     */
    private fun rechercherItem(descriptionItem: String): Boolean {
        for (item in this.items) {
            if (item.description == descriptionItem)
                return true
        }
        return false
    }

    /**
     * Ajoute l'item s'il n'est pas déjà présent dans la liste.
     * Si l'item est ajouté, renvoie true, sinon false
     * @param item l'item à ajouter
     */
    fun ajouterItem(id: Long, description: String): Boolean {
        if (this.rechercherItem(description)) return false
        this.items.add(ItemToDo(id, description))
        return true
    }

    /**
     * Renvoie une copie de la liste des items.
     */
    fun getItems(): MutableList<ItemToDo> = ArrayList(this.items)

    fun toggleItem(position: Int, boolean: Boolean) {
        this.items[position].fait = boolean
    }

    override fun toString(): String {
        var str = "["
        for (item in items) {
            str += "$item, "
        }
        return "$str]"
    }
}