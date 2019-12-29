package com.roacult.kero.team7.backdropapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.search.*
import kotlinx.android.synthetic.main.search_product.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)
//        container.setOnBackdropChangeStateListener {
//            Toast.makeText(this,"backdrop change state to : $it ",Toast.LENGTH_SHORT).show()
//        }
        bla.setOnClickListener {
            Toast.makeText(this,"textView is Clicked",Toast.LENGTH_SHORT).show()
        }
    }
}