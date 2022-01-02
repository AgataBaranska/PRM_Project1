package com.example.financialmanager

object Constants {
    const val DB_NAME = "Transactions.db"
    const val DB_VERSION = 1
    const val TABLE_NAME = "Transactions_table"
    const val COLUMN_ID = "Id"
    const val COLUMN_TYPE = "Type"
    const val COLUMN_PLACE = "Place"
    const val COLUMN_AMOUNT = "Amount"
    const val COLUMN_DATE = "Date"
    const val COLUMN_CATEGORY = "Category"

    const val CURRENCY = "PLN"

    const val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME " +
            "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$COLUMN_TYPE TEXT," +
            "$COLUMN_PLACE TEXT," +
            "$COLUMN_AMOUNT REAL," +
            "$COLUMN_DATE TEXT," +
            "$COLUMN_CATEGORY TEXT);"
            )
    const val SELECT_ALL = ("SELECT * FROM $TABLE_NAME")
}
