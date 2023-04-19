package com.example.learnfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnfirebase.adapter.ProductAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_fetching.*

class FetchingActivity : AppCompatActivity() {

    private lateinit var ds:ArrayList<ProductModel>
    private lateinit var dbRef :DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        rvSP.layoutManager = LinearLayoutManager(this)
        rvSP.setHasFixedSize(true)
        ds = arrayListOf()
        showProduct()
    }

    private fun showProduct() {
        dbRef = FirebaseDatabase.getInstance().getReference("Product")
        //Để đọc dữ liệu tại một đường dẫn và lắng nghe các thay đổi,
        //hãy sử dụng addValueEventListener()

        dbRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(dataSnapshot in snapshot.children){
                        val productData = dataSnapshot.getValue(ProductModel::class.java)
                        ds.add(productData!!)
                    }
                    val mAdapter = ProductAdapter(ds, this@FetchingActivity)
                    rvSP.adapter = mAdapter
                    //code lắng nghe sự kiện click lên item rv


                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}