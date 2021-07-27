package com.vanilla.munato.repository.localstore.localpaintings

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_paintings")
data class LocalPaintingsEntity(
    @PrimaryKey(autoGenerate = true) val uid: Long, // local int id
    @ColumnInfo(name="code") val code: String?, // javascript code for this scene
    @ColumnInfo(name="preview_base64") val previewBase64: String?, // javascript code for this scene
    @Nullable var paintingID: String = "", // presented if published
)
