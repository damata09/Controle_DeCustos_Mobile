package com.example.costcontrol.model

import java.io.Serializable

data class Cost(
    val id: Int? = null,
    val descricao: String,
    val valor: Double,
    val data: String? = null,
    val categoria_id: Int,
    val categoria: Category? = null
) : Serializable
