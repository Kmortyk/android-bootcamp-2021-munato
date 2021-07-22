package com.vanilla.munato.repository.localstore.localpaintings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_paintings")
data class LocalPaintingsEntity(
    @PrimaryKey val uid: Int, // local int id
    @ColumnInfo(name="code") val code: String?, // javascript code for this scene
    @ColumnInfo(name="paintingID") var paintingID: String = "", // presented if published
)
