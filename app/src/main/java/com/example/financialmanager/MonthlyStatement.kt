package com.example.financialmanager

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.financialmanager.databinding.ActivityMonthlyStatementBinding

class MonthlyStatement : AppCompatActivity() {
    private lateinit var binding: ActivityMonthlyStatementBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var chartView: ChartView
    private lateinit var spinnerMonth: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_statement)
        binding = ActivityMonthlyStatementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DatabaseHelper(this)
        chartView = binding.chartView

        spinnerMonth = binding.spinnerMonth
        val listMonthsYears = dbHelper.getMonthsYears()
        if (listMonthsYears != null) {
            val adapterMonth: ArrayAdapter<String> =
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMonthsYears!!)
            adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerMonth?.adapter = adapterMonth
        }

        spinnerMonth.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val monthYear = parent?.getItemAtPosition(position).toString().trim()
                val tab = monthYear.split("-")
                val year = tab[0]
                val month = tab[1]

                val dataMap = dbHelper.getMonthlyDataForChartView(
                    year, month
                )

                if (dataMap?.isNotEmpty() == true) {
                    chartView.dataMap = dataMap
                    chartView.invalidate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}