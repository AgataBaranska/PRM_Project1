package com.example.financialmanager

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.financialmanager.databinding.ActivityAddTransactionBinding
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionActivity : AppCompatActivity() {

    private var btnShare: AppCompatButton? = null
    private var editTextAmount: EditText? = null
    private var editTextPlace: EditText? = null
    private var btnSaveEditTransaction: Button? = null
    private lateinit var binding: ActivityAddTransactionBinding
    private var btnSelectDate: Button? = null
    private var spinnerTransactionTypes: Spinner? = null
    private var spinnerSelectedType: String? = null
    private var spinnerTransactionCategory: Spinner? = null
    private var spinnerSelectedCategory:String? = null

    private  var transactionEdited:Transaction? = null

    //database
    private lateinit var dbHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findId()

        dbHelper = DatabaseHelper(this)

        val adapterTransaction :ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,R.array.transaction_types,android.R.layout.simple_spinner_item)
        adapterTransaction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTransactionTypes?.adapter = adapterTransaction

        spinnerTransactionTypes?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               spinnerSelectedType = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val adapterCategory:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,R.array.transaction_category,android.R.layout.simple_spinner_item)
       adapterTransaction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTransactionCategory?.adapter = adapterCategory

        spinnerTransactionCategory?.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerSelectedCategory = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        btnSelectDate?.setOnClickListener { view ->
            val myCalendar = Calendar.getInstance()
            val year = myCalendar.get(Calendar.YEAR)
            val month = myCalendar.get(Calendar.MONTH)
            val day = myCalendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, day ->

                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val calendar = Calendar.getInstance()
                    calendar.set(year,month,day)
                    btnSelectDate?.text = dateFormat.format(calendar.time).toString()
                },
                year,
                month,
                day
            )
            datePicker.datePicker.maxDate = myCalendar.timeInMillis
            datePicker.show()
        }

        btnSaveEditTransaction?.setOnClickListener() {


            if(transactionEdited!=null){
                dbHelper.updateTransaction(
                    transactionEdited!!.id,
                    spinnerSelectedType,
                    editTextPlace?.text.toString().trim().uppercase(),
                    editTextAmount?.text.toString().trim().toDouble(),
                    btnSelectDate?.text.toString().trim(),
                    spinnerSelectedCategory?.uppercase(),

                )
            }else {
                val id = dbHelper.insertTransaction(
                    spinnerSelectedType,
                    editTextPlace?.text.toString().trim().uppercase(),
                    editTextAmount?.text.toString().trim().toDouble(),
                    btnSelectDate?.text.toString().trim(),
                    spinnerSelectedCategory?.uppercase(),

                )
                if (id.equals(-1)) {
                    Toast.makeText(this, "Transaction added!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Transaction added!", Toast.LENGTH_LONG).show()
                }

            }
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        btnShare?.setOnClickListener(){view->

          var sendIntent:Intent = Intent();
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my transaction details:\n" +
                    "Type: ${transactionEdited?.type},\n" +
                    "Place: ${transactionEdited?.place},\n" +
                    "Amount: ${transactionEdited?.amount} PLN \n" +
                    "Category: ${transactionEdited?.category} \n" +
                    "Img: ${transactionEdited?.image} \n")
            sendIntent.type = "text/plain"
            var shareIntent:Intent = Intent.createChooser(sendIntent,null)
            startActivity(shareIntent)

        }

        displayTransactionDetails()
    }

    private fun displayTransactionDetails() {
        if(intent.hasExtra("selectedTransaction")){
            transactionEdited = intent.getParcelableExtra<Transaction>("selectedTransaction")!!

            var typesArray = resources.getStringArray(R.array.transaction_types)
            var typePosition: Int= typesArray.indexOf(transactionEdited!!.type)
            spinnerTransactionTypes?.setSelection(typePosition)
            editTextAmount?.setText(transactionEdited?.amount.toString())
            editTextPlace?.setText(transactionEdited?.place)
            btnSelectDate?.text = transactionEdited?.date
            var categoryArray = resources.getStringArray(R.array.transaction_category)
            var categoryPosition:Int = categoryArray.indexOf(transactionEdited!!.category)
            spinnerTransactionCategory?.setSelection(categoryPosition)
        }

    }

    private fun findId() {
        btnShare = binding.btnShare
        spinnerTransactionTypes = binding.spinnerType
        btnSaveEditTransaction = binding.btnSaveEditTransaction
        btnSelectDate = binding.btnSelectDate
        editTextPlace = binding.editTextPlace
        editTextAmount = binding.editTextAmount
        spinnerTransactionCategory = binding.spinnerCategory

    }
}