package com.example.coctails.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.coctails.core.room.entity.Shopping
import io.reactivex.Single

@Dao
interface ShoppingDAO {

    @Query("SELECT * FROM shopping_data")
    fun getAllShoppingItem(): Single<List<Shopping>>

    @Query("SELECT * FROM shopping_data WHERE item_id = :item_id AND category = :category")
    fun getShoppingItem(item_id : Int, category : String): Single<Shopping>

    @Insert
    fun insert(shoppingItem: Shopping)

    @Delete
    fun delete(shoppingItem: Shopping)
}