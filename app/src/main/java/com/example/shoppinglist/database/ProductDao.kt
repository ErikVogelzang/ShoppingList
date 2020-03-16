package com.example.shoppinglist.database

import androidx.room.*
import com.example.shoppinglist.model.ListItem

@Dao
interface ProductDao {
    @Query("SELECT * FROM productTable")
    suspend fun getAllProducts() : List<ListItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ListItem)

    @Delete
    suspend fun deleteProduct(product: ListItem)

    @Query("DELETE FROM productTable")
    suspend fun deleteAllProducts()
}