package com.example.coffeeordingappbscs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //get the references of views
        val quantityTextView: EditText = findViewById(R.id.quantityEditText)
        val whippedCreamCheckBox: CheckBox = findViewById(R.id.whippedCreamCheckBox)
        val chocolateCheckBox: CheckBox = findViewById(R.id.chocolateCheckBox)
        val orderButton: Button = findViewById(R.id.orderButton)
        val orderSummaryTextView: TextView = findViewById(R.id.orderSummaryTextView)

        //Handle order Button click

        orderButton.setOnClickListener{
            val quantityText = quantityTextView.text.toString()

            //check if quantity input is not empty
            if(quantityText.isNotEmpty()) {
                val quantity = quantityText.toInt()
                val hasWhippedCream = whippedCreamCheckBox.isChecked
                val haschocolate = chocolateCheckBox.isChecked
                //calculate price and update the order summary
                var price = calculatePrice(quantity, hasWhippedCream,haschocolate)

                displayOrderSummary(orderSummaryTextView, quantity, hasWhippedCream, haschocolate, price)
            } else {
                Toast.makeText(this, "Enter Quantity", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun displayOrderSummary(
        orderSummaryTextView: TextView,
        quantity: Int,
        hasWhippedCream: Boolean,
        haschocolate: Boolean,
        price: Int
    ) {
        val whippedCreamText = if(hasWhippedCream) "Yes" else "No"
        val chocolateText = if(haschocolate) "Yes" else "No"

        val orderSummary:String = "Quantity: " + quantity +
                           "\nWhipped Cream: " + whippedCreamText +
                           "\nChocolate: " + chocolateText +
                           "\nTotal Price: " + price +
                           "\nThank you!"
        orderSummaryTextView.text = orderSummary

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("hamadqur447@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Coffee Order Summary")
                putExtra(Intent.EXTRA_TEXT, orderSummary)
            }
            if(emailIntent.resolveActivity(packageManager) != null) {
                startActivity(emailIntent)
            } else {
                Toast.makeText(this, "No email app found", Toast.LENGTH_LONG).show()
            }


        }
    }


    private fun calculatePrice(quantity:Int, hasWhippedCream: Boolean, hasChocolate: Boolean): Int {

        val basePricePerCup = 10
        var totalPricePerCup = basePricePerCup

        if(hasWhippedCream) {
            totalPricePerCup = totalPricePerCup + 1
        }

        if(hasChocolate) {
            totalPricePerCup += 2;
        }

        return quantity * totalPricePerCup
    }
