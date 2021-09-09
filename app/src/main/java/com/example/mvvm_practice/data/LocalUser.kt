package com.example.mvvm_practice.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TABLE_NAME)
data class LocalUser(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = COLUMN_NAME_ID) val id: Int,
    @ColumnInfo(name = COLUMN_NAME_NICKNAME) val nickname: String,
    @ColumnInfo(name = COLUMN_NAME_FIRST_NAME) val firstName: String?,
    @ColumnInfo(name = COLUMN_NAME_SECOND_NAME) val secondName: String?,
    val age: Int?
) {
    constructor(
        nickname: String,
        firstName: String? = null,
        secondName: String? = null,
        age: Int? = null
    ) : this(
        0,
        nickname,
        firstName,
        secondName,
        age
    )
}