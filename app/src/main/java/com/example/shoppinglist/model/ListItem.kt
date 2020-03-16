package com.example.shoppinglist.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "productTable")
data class ListItem(
    @PrimaryKey
    @ColumnInfo(name = "productName")
    var name: String,
    @ColumnInfo(name = "productCount")
    var count: Int
) : Parcelable