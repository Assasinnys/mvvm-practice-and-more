package com.example.mvvm_practice.repository.room

import androidx.room.*

@Entity(tableName = "local_user_table")
data class LocalUser(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_id") val id: Int,
    @ColumnInfo(name = "nickname") val nickname: String,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "second_name") val secondName: String?,
    val age: Int?
) {
    constructor(nickname: String, firstName: String?, secondName: String?, age: Int?) : this(
        0,
        nickname,
        firstName,
        secondName,
        age
    )
}