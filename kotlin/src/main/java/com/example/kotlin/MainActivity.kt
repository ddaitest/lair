package com.example.kotlin

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hello.text = "COOL"
    }

    fun test() {
        var x: Long = 1L
        var a = 1L
        var xx = BallX(1L)
        var y = BallY(1L)
        JavaClass.run()
        val c = doSomething()
    }

    fun doSomething(){
    }
}
