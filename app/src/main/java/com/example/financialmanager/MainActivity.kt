package com.example.financialmanager

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financialmanager.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), RecyclerAdapter.OnItemClickListener,
    RecyclerAdapter.OnItemLongClickListener {

    private lateinit var btnMonthlyStatement: Button
    private lateinit var outcomeAmount: TextView
    private lateinit var incomeAmount: TextView
    private lateinit var btnAddTransaction: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper
    private var transactions: ArrayList<Transaction> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findIds()
        dbHelper = DatabaseHelper(this)

        displayDbDataInRecyclerView()

        displayMonthlyIncomeOutcome()

        btnAddTransaction.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        btnMonthlyStatement.setOnClickListener() {
            startActivity((Intent(this, MonthlyStatement::class.java)))
        }

    }

    private fun findIds() {
        btnAddTransaction = binding.btnAddTransaction
        recyclerView = binding.recyclerView
        incomeAmount = binding.incomeAmount
        outcomeAmount = binding.outcomeAmount
        btnMonthlyStatement = binding.btnMonthlyStatement

    }

    private fun displayDbDataInRecyclerView() {
        val cursor = dbHelper.readAllData()
        if (cursor?.count == 0) {
            Toast.makeText(this, "No data!", Toast.LENGTH_LONG).show()
        } else {
            while (cursor?.moveToNext() == true) {
                var img: Int
                if (cursor.getStringOrNull(1).equals("INCOME", ignoreCase = true)) {
                    img = R.drawable.ic_income
                } else {
                    img = R.drawable.ic_outcome
                }
                transactions.add(
                    Transaction(
                        cursor.getInt(0),
                        img,
                        cursor.getStringOrNull(1),
                        cursor.getStringOrNull(2),
                        cursor.getDouble(3),
                        cursor.getStringOrNull(4),
                        cursor.getStringOrNull(5)
                    )
                )
            }
        }
        transactions.sort()
        recyclerView.adapter = RecyclerAdapter(transactions, this, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        cursor?.close()
    }

    private fun displayMonthlyIncomeOutcome() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        val dateStr= dateFormat.format(calendar.time).toString()
        val splitTab = dateStr.split('-')
        val month = splitTab[1]

        "${dbHelper.getMonthlyIncome(month)} ${Constants.CURRENCY}".also { incomeAmount.text = it }
        outcomeAmount.text = "${dbHelper.getMonthlyOutcome(month)}  ${Constants.CURRENCY}"
    }


    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onItemClick(position: Int) {
        val clickedItem = transactions[position]
        val intent = Intent(this, AddTransactionActivity::class.java)
        intent.putExtra("selectedTransaction", clickedItem)
        startActivity(intent)
    }

    override fun onItemLongClicked(position: Int): Boolean {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure?")
        builder.setMessage("Do you want to delete this item?")
        builder.setPositiveButton(
            "Yes"
        ) { _: DialogInterface?, _: Int ->

            val transactionToDelete = transactions[position]
            transactions.removeAt(position)
            recyclerView.adapter?.notifyItemRemoved(position)
            dbHelper.deleteTransaction(transactionToDelete)
            displayMonthlyIncomeOutcome()
        }
        builder.setNegativeButton("No") { _: DialogInterface, _: Int -> }
        builder.show()
        return true
    }
}