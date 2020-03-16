package com.example.shoppinglist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.*
import com.example.shoppinglist.R
import com.example.shoppinglist.database.ProductRepository
import com.example.shoppinglist.model.ListItem
import kotlinx.android.synthetic.main.activity_shopping_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShoppingListActivity : AppCompatActivity() {
    private val listItems = arrayListOf<ListItem>()
    private val listItemAdapter = ListItemAdapter(listItems)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private lateinit var productRepository: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)
        supportActionBar?.title = getString(R.string.app_name)
        productRepository = ProductRepository(this)
        initViewa()
    }

    private fun initViewa() {
        rvItems.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        rvItems.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        rvItems.adapter = listItemAdapter

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val productToDelete = listItems[position]

                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteProduct(productToDelete)
                    }
                    getShoppingListFromDatabase()
                }
            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rvItems)

        getShoppingListFromDatabase()
        btnAdd.setOnClickListener{ addProduct() }
    }

    private fun getShoppingListFromDatabase() {
        mainScope.launch {
            val shoppingList = withContext(Dispatchers.IO) {
                productRepository.getAllProducts()
            }
            this@ShoppingListActivity.listItems.clear()
            this@ShoppingListActivity.listItems.addAll(shoppingList)
            this@ShoppingListActivity.listItemAdapter.notifyDataSetChanged()
        }
    }

    private fun validateFields(): Boolean {
        return if (etName.text.toString().isNotBlank() && etCount.text.toString().isNotBlank()) {
            true
        }
        else
        {
            Toast.makeText(this, "Please fill in the fields", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun addProduct() {
        if (validateFields()) {
            mainScope.launch {
                val product = ListItem(
                    etName.text.toString(),
                    etCount.text.toString().toInt()
                )

                withContext(Dispatchers.IO) {
                    productRepository.insertProduct(product)
                }
                getShoppingListFromDatabase()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun deleteShoppingList() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                productRepository.deleteAllProducts()
            }
            getShoppingListFromDatabase()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_shopping_list -> {
                deleteShoppingList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
