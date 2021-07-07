package com.example.calculator

import Calculator
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.lang.Exception

val c = Calculator()

class MainActivity : AppCompatActivity() {
    var b = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val display : TextView = findViewById(R.id.textDisplay)
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                b = !b
                display.setText(c.getDisplay(b))
                handler.postDelayed(this, 500)//1 sec delay
            }
        }, 0)
    }

    fun buttonPress(v: View){
        val but = v as Button
        val text = but.text.toString()
        if (text == "→") c.moveRight()
        else if (text == "←") c.moveLeft()
        else if (text == "=" || text[0] == '→') {
            try{
                if (text[0] == '→') c.eval(key=text[1].toString())
                else c.eval()
            }
            catch (e: Exception){
                val builder = AlertDialog.Builder(this)
                with (builder){
                    setTitle("Error")
                    setMessage(e.message)
                    setPositiveButton("OK", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    })
                    show()
                }
                return
            }
        }
        else if (text == "C") c.clear()
        else if (text == "del") c.del()
        else c.type(text)
        val display : TextView = findViewById(R.id.textDisplay)
        display.setText(c.getDisplay(b))
    }
}