package com.fcojaviergarciarodriguez.shoppinglistapp.framework.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["listId"])]
)
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val listId: Int,
    val name: String,
    val isChecked: Boolean = false
) 