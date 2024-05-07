package com.example.doankotlin.Activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.doankotlin.Domain.AllMenu
import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.R
import com.example.doankotlin.databinding.ActivityAddItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : BaseActivity() {

    private lateinit var foodName: String
    private var foodPrice: Double = 0.0
    private lateinit var foodCategory: String
    private var foodCategoryId: Int = 0
    private var foodMinute: Int = 0
    private var foodImageUri: Uri? = null
    private lateinit var foodDescription: String
    private var lastItemId: Int = 0

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.AddItemButton.setOnClickListener {
            foodName = binding.foodName.text.toString().trim()
            foodPrice = binding.foodPrice.text.toString().trim().toDouble()
            foodCategory = binding.foodCategory.text.toString()
           /* when(foodCategory){
                 ->
            }*/
            foodMinute = binding.foodMinute.text.toString().trim().toInt()
            foodDescription = binding.description.text.toString().trim()

            if (!(foodName.isBlank() || foodPrice.isNaN() || foodCategory.isBlank() || foodPrice.isNaN() || foodDescription.isBlank())) {
                uploadData()
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Thêm lỗi", Toast.LENGTH_SHORT).show()
            }
        }

        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

    }

    private fun uploadData() {
        val myRef = database.getReference("Foods")

        val newItemKey = myRef.push().key

        if (foodImageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_image${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val newItem = AllMenu(
                        Title = foodName,
                        Price = foodPrice,
                        foodCategory = foodCategory,
                        TimeValue  = foodMinute,
                        ImagePath = downloadUrl.toString(),
                        Description = foodDescription
                    )
                    newItemKey?.let { key ->
                            myRef.child(key).setValue(newItem).addOnSuccessListener {
                                Toast.makeText(this, "Đã cập nhật dữ liệu", Toast.LENGTH_SHORT).show()
                            }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Cập nhật lỗi", Toast.LENGTH_SHORT).show()
                                }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Tải ảnh lỗi", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Vui lòng chọn 1 hình ảnh", Toast.LENGTH_SHORT).show()
        }
    }

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
        }
    }
}