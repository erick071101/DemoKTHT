package com.ebenezer.gana.shoppyv2.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebenezer.gana.shoppyv2.R
import com.ebenezer.gana.shoppyv2.databinding.ActivityCartListBinding
import com.ebenezer.gana.shoppyv2.firestore.FirestoreClass
import com.ebenezer.gana.shoppyv2.models.Cart
import com.ebenezer.gana.shoppyv2.models.Product
import com.ebenezer.gana.shoppyv2.ui.adapters.CartListAdapter

class CartListActivity : BaseActivity() {

    private lateinit var mProductList: ArrayList<Product>
    private lateinit var mCartListItems: ArrayList<Cart>


    lateinit var binding: ActivityCartListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_cart_list)
        binding = ActivityCartListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()


    }


    override fun onResume() {
        super.onResume()
        getCartItemsList()
//        getProductList()
    }

    fun itemUpdateSuccess() {
        getCartItemsList()
    }


    fun itemRemovedSuccess() {
        Toast.makeText(
            this@CartListActivity,
            resources.getString(R.string.msg_item_removed_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getCartItemsList()

    }



    private fun getCartItemsList() {
        //show progress dialog, hide when successCartItemList is called
        //showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this@CartListActivity)


    }


    private fun getProductList() {
        //show progress dialog, hide when successCartItemList is called
        FirestoreClass().getAllProductsList(this@CartListActivity)
    }

    fun successProductsListFromFireStore(productList: ArrayList<Product>) {
        mProductList = productList
        getCartItemsList()


    }

    fun successCartItemsList(cartList: ArrayList<Cart>) {
        //hide progress dialog, shown when getCartItemsList is called

//        for (product in mProductList) {
//            for (cartItem in cartList) {
//                if (product.productId == cartItem.product.productId) {
//
//                    cartItem.product.totalQuantity = product.totalQuantity
//
//                    if (product.totalQuantity.toInt() == 0) {
//                        cartItem.product.totalQuantity = product.totalQuantity
//                    }
//                }
//
//            }
//        }

        Log.e("sssasasa", cartList.toString() )
        mCartListItems = cartList

        /*for (items in cartList){
            Log.i("Cart Item Title", items.title)
        }*/

        if (mCartListItems.size > 0) {
            binding.rvCartItemsList.visibility = View.VISIBLE
            binding.tvNoCartItemFound.visibility = View.GONE
            binding.llCheckout.visibility = View.VISIBLE

            with(binding.rvCartItemsList) {
                layoutManager = LinearLayoutManager(this@CartListActivity)
                setHasFixedSize(true)
                val cartListAdapter = CartListAdapter(
                    this@CartListActivity,
                    cartListItems = mCartListItems, true
                )
                adapter = cartListAdapter
            }


            var subTotal: Double = 0.0
            var shippingCharge = 0

            for (item in mCartListItems) {
                val availableQuantity = item.product.totalQuantity.toInt()
                if (availableQuantity > 0) {
                    val price = item.product.price.toDouble()
                    val quantity = item.item.quantity.toInt()
                    shippingCharge = 15
                    subTotal += (price * quantity)
                }

            }

            binding.tvSubTotal.text = "$subTotal$"
            binding.tvShippingCharge.text = "$shippingCharge$"

            if (subTotal > 0) {
                binding.llCheckout.visibility = View.VISIBLE
                val total = subTotal + shippingCharge

                binding.tvTotalAmount.text = "$total$"
            } else {

                binding.llCheckout.visibility = View.GONE


            }

        } else {
            binding.rvCartItemsList.visibility = View.GONE
            binding.tvNoCartItemFound.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.GONE

        }


    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCartListActivity)
        val actionbar = supportActionBar
        actionbar?.let {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarCartListActivity.setNavigationOnClickListener { finish() }


    }
}