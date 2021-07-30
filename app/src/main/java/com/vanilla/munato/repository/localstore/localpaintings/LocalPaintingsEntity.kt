package com.vanilla.munato.repository.localstore.localpaintings

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_paintings")
data class LocalPaintingsEntity(
    @PrimaryKey(autoGenerate = true) val uid: Long, // local int id
    @ColumnInfo(name="code") val code: String?, // javascript code for this scene
    @ColumnInfo(name="preview") val preview: ByteArray?, // javascript code for this scene
    @Nullable var paintingID: String = "", // presented if published
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocalPaintingsEntity

        if (uid != other.uid) return false
        if (code != other.code) return false
        if (preview != null) {
            if (other.preview == null) return false
            if (!preview.contentEquals(other.preview)) return false
        } else if (other.preview != null) return false
        if (paintingID != other.paintingID) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + (code?.hashCode() ?: 0)
        result = 31 * result + (preview?.contentHashCode() ?: 0)
        result = 31 * result + paintingID.hashCode()
        return result
    }
}
