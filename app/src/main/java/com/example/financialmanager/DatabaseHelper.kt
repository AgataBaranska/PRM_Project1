package com.example.financialmanager

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(
    context,
    Constants.DB_NAME,
    null,
    Constants.DB_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(Constants.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${Constants.TABLE_NAME}")
        onCreate(db)
    }

    fun insertTransaction(
        type: String?,
        place: String?,
        amount: Double?,
        date: String?,
        category: String?,
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(Constants.COLUMN_TYPE, type)
        values.put(Constants.COLUMN_PLACE, place)
        values.put(Constants.COLUMN_AMOUNT, amount)
        values.put(Constants.COLUMN_DATE, date)
        values.put(Constants.COLUMN_CATEGORY, category)

        val id = db.insert(Constants.TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun updateTransaction(
        id: Int,
        type: String?,
        place: String,
        amount: Double,
        date: String,
        category: String?,

        ): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constants.COLUMN_ID, id)
        values.put(Constants.COLUMN_TYPE, type)
        values.put(Constants.COLUMN_PLACE, place)
        values.put(Constants.COLUMN_AMOUNT, amount)
        values.put(Constants.COLUMN_DATE, date)
        values.put(Constants.COLUMN_CATEGORY, category)

        val rowsAffected = db.update(Constants.TABLE_NAME, values, "Id=$id", null)
        db.close()
        return rowsAffected
    }

    fun readAllData(): Cursor? {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db?.rawQuery(Constants.SELECT_ALL, null)

        return cursor;
    }

    fun deleteTransaction(transaction: Transaction) {
        var db = this.writableDatabase
        db.execSQL("DELETE FROM ${Constants.TABLE_NAME} WHERE Id = ${transaction.id}")
        db.close()
    }

    fun getMonthlyIncome(month: String): Double {
        val db = this.readableDatabase
        val query = "SELECT SUM(Amount) AS Total FROM Transactions_table " +
                "WHERE Date LIKE '%-${month}-%' AND Type = 'income' COLLATE NOCASE"
        val cursor = db.rawQuery(query, null)


        if (cursor.count != 0) {
            cursor.moveToFirst()
            return cursor.getDouble(0)
        }
        return -1.0
    }

    fun getMonthlyOutcome(month: String): Double {
        val db = this.readableDatabase
        val query = "SELECT SUM(Amount) AS Total FROM Transactions_table " +
                "WHERE Date LIKE '%-${month}-%' AND Type = 'outcome' COLLATE NOCASE"
        val cursor = db.rawQuery(query, null)

        if (cursor.count != 0) {
            cursor.moveToFirst()
            return -cursor.getDouble(0)
        }
        return -1.0
    }

    fun getMonthlyDataForChartView(year:String,month: String): LinkedHashMap<Int, Double>? {
        val db = this.readableDatabase
        val query =
            "select strftime('%d',date) as day,type,SUM(amount) as sum from Transactions_table\n" +
                    "group by date \n" +
                    "having date LIKE '$year-$month-%'"

        val chartViewDataMap: LinkedHashMap<Int, Double> = LinkedHashMap()
        val cursor = db.rawQuery(query, null)

        if (cursor.count <= 0) {
            cursor.close()
            return null
        }
        // cursor.moveToFirst()
        while (cursor.moveToNext()) {

            val day = cursor.getInt(0)
            val type = cursor.getString(1)
            var amount = cursor.getDouble(2)
            if (type == "OUTCOME") {
                amount = -amount
            }
            chartViewDataMap[day] = amount
        }
        return chartViewDataMap
    }

    fun getMonthsYears(): MutableList<String>? {
        val db = this.readableDatabase
        val query = "select DISTINCT strftime('%Y-%m',date) as month from Transactions_table"
        val list: MutableList<String> = mutableListOf<String>()
        val cursor = db.rawQuery(query, null)
        try {
            while (cursor.moveToNext()) {
                var month = if (cursor.getString(0) == null) {
                    "-1"
                } else {
                    cursor.getString(0)
                }
                list.add(month)
            }
        } finally {
            cursor.close()
        }
        return list
    }


}

