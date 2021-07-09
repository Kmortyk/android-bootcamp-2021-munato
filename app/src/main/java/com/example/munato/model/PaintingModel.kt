package com.example.munato.model

data class PaintingModel(
    val id: Int, // id for pagination
    val user: String, // username
    val name: String, // name of the picture
    val code: String, // javascript code for this scene
    val stars: Int, // number of stars for this painting
    val coverServerPath: String, // path for cover of this painting
)