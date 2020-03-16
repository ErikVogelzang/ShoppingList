package com.example.shoppinglist.database

import android.content.Context
import com.example.shoppinglist.model.ListItem

class ProductRepository(context: Context) {
    private val productDao: ProductDao

    init {
        val database =
            ShoppingListRoomDatabase.getDatabase(context)
        productDao = database!!.productDao()
    }

    suspend fun getAllProducts(): List<ListItem> = productDao.getAllProducts()

    suspend fun insertProduct(product: ListItem) = productDao.insertProduct((product))

    suspend fun deleteProduct(product: ListItem) = productDao.deleteProduct(product)

    suspend fun deleteAllProducts() = productDao.deleteAllProducts()
}