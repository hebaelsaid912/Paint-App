package com.example.android.simplepaintapp

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.android.simplepaintapp.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButtonToggleGroup

class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding
    companion object {
         val TYPE_PATH = "path"
         val TYPE_ARROW = "arrow"
         val TYPE_RECT = "rectangle"
         val TYPE_ELLIPSE = "ellipse"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        currentColor(Color.BLACK)
        binding.toggleBtnGroup.addOnButtonCheckedListener{ toggleBtnGroup: MaterialButtonToggleGroup, buttonId: Int, isChecked: Boolean ->
            if(isChecked){
                when(buttonId){
                    R.id.pencil_btn -> {
                        currentType(TYPE_PATH)
                    }
                    R.id.arrow_btn ->{
                        currentType(TYPE_ARROW)
                    }
                    R.id.rectangle_btn ->{
                        currentType(TYPE_RECT)
                    }
                    R.id.ellipse_btn ->{
                        currentType(TYPE_ELLIPSE)
                    }
                    R.id.color_palette_btn ->{
                        if (binding.toggleColorBtnGroup.isVisible) {
                            binding.toggleColorBtnGroup.visibility = View.GONE
                        } else {
                            binding.toggleColorBtnGroup.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        binding.toggleColorBtnGroup.addOnButtonCheckedListener { toggleColorBtnGroup: MaterialButtonToggleGroup, buttonId: Int, isChecked: Boolean ->
            if (isChecked) {
                when (buttonId) {
                    R.id.red_btn ->{
                        currentColor(Color.RED)
                    }
                    R.id.green_btn ->{
                        currentColor(Color.GREEN)
                    }
                    R.id.yellow_btn ->{
                        currentColor(Color.YELLOW)
                    }
                    R.id.blue_btn ->{
                        currentColor(Color.BLUE)
                    }
                    R.id.black_btn ->{
                    currentColor(Color.BLACK)
                }

                }
            }
        }
    }
    private fun currentColor(color:Int){
        Canvas.current_brush = color
        Canvas.paintForPencil.color = color
        Canvas.paintForArrow.color = color
        Canvas.paintForEllipse.color = color
        Canvas.paintForRect.color = color

    }
    private fun currentType(type:String){
        Canvas.type = type
    }
}