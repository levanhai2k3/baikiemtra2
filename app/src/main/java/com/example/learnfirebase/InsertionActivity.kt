package com.example.learnfirebase

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_insertion.*
import kotlinx.android.synthetic.main.emp_list_item.*
import java.util.*

class InsertionActivity : AppCompatActivity() {

    var imgSP: String? = ""
    private lateinit var dbRef : DatabaseReference
    private lateinit var selectImg: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_insertion)

        dbRef = FirebaseDatabase.getInstance().getReference("Product")

        btnInsert.setOnClickListener {
            insertProduct()
        }

        btnUploadImg.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (data.data != null)
                selectImg = data.data!!
            ivImage.setImageURI(selectImg)
        }
    }


    private fun insertProduct() {
        //getting value
        val tenSP = edtTenSP.text.toString()
        val loaiSP = edtLoaiSP.text.toString()
        val giaSP = edtGiaSP.text.toString()

        //kiểm tra các ô nhập liệu đã có dữ liệu hay chưa
        if (tenSP.isEmpty()){
            edtTenSP.error = "Hãy nhập tên sản phẩm"
            return
        }
        if (loaiSP.isEmpty()){
            edtLoaiSP.error = "Hãy nhập loại sản phẩm"
            return
        }
        if (giaSP.isEmpty()){
            edtGiaSP.error = "Hãy nhập giá sản phẩm"
            return
        }
        if (selectImg == null){
            btnUploadImg.error = "Hãy chọn ảnh sản phẩm"
            return
        }

        //tạo đường dẫn lưu trữ hình ảnh trên Firebase Storage
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("images/${UUID.randomUUID()}")

        //tải lên hình ảnh lên Firebase Storage
        storageRef.putFile(selectImg!!)
            .addOnSuccessListener { taskSnapshot ->
                //lấy URL của hình ảnh đã tải lên
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()

                    //đẩy dữ liệu vào Firebase Realtime Database
                    val idSP = dbRef.push().key!!
                    val product = ProductModel(idSP, tenSP, loaiSP, giaSP, imageUrl)

                    dbRef.child(idSP).setValue(product)
                        .addOnCompleteListener {
                            Toast.makeText(this,"Thêm sản phẩm thành công",Toast.LENGTH_SHORT).show()
                            edtTenSP.setText("")
                            edtLoaiSP.setText("")
                            edtGiaSP.setText("")
                            imgSanPham.setImageResource(R.drawable.ic_launcher_background)
                        }
                        .addOnFailureListener { err ->
                            Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Lỗi khi tải ảnh lên Firebase Storage: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}