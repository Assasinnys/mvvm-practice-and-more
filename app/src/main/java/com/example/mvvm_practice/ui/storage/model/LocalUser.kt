package com.example.mvvm_practice.ui.storage.model

import android.location.Address
import androidx.room.*

// До лучших времён
//data class Address(
//    val country: String?,
//    val street: String?,
//    val state: String?,
//    val city: String?,
//    @ColumnInfo(name = "post_code") val postCode: Int?
//)

@Entity(tableName = "local_user_table")
data class LocalUser(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_id") val id: Int,
    @ColumnInfo(name = "nickname") val nickname: String,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "second_name") val secondName: String?,
    val age: Int?,
    //@Embedded val address: Address?
) {
    constructor(nickname: String, firstName: String?, secondName: String?, age: Int?) : this(
        0,
        nickname,
        firstName,
        secondName,
        age
    )
}