package com.example.costcontrol.model

import java.io.Serializable

data class Category(
    val id: Int? = null,
    val nome: String,
    val descricao: String? = null
) : Serializable {
    override fun toString(): String {
        return nome
    }
}
