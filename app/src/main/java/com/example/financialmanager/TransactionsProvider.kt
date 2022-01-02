package com.example.financialmanager

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class TransactionsProvider : ContentProvider() {

    companion object {
        val PROVIDER_NAME: String = "com.example.financialmanager.TransactionsProvider"
        val URL = "content://$PROVIDER_NAME/${Constants.TABLE_NAME}"
        val CONTENT_URI = android.net.Uri.parse(URL)

        val _ID = "Id"
        val _TYPE = "Type"
        val _PLACE = "Place"
        val _AMOUNT = "Amount"
        val _DATE = "Date"
        val _CATEGORY = "Category"

    }

    lateinit var db: SQLiteDatabase

    override fun onCreate(): Boolean {
        var dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
        return db != null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return db.query(
            Constants.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.dir/vnd.example.${Constants.TABLE_NAME}"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        db.insert(Constants.TABLE_NAME, null, values)
        context?.contentResolver?.notifyChange(uri, null)
        return uri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        var count = db.delete(Constants.TABLE_NAME, selection, selectionArgs)
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        var count = db.update(Constants.TABLE_NAME, values, selection, selectionArgs)
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }
}