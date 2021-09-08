package com.example.mvvm_practice.data.cursor

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteTransactionListener
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.example.mvvm_practice.data.LocalUser
import com.example.mvvm_practice.data.room.LocalUserDao
import com.example.mvvm_practice.extras.TAG
import com.example.mvvm_practice.extras.getIntValue
import com.example.mvvm_practice.extras.getStringValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LocalUserCursorDatabase(context: Context, private val scope: CoroutineScope) : LocalUserDao,
    SQLiteTransactionListener, SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    private val _currentDatabaseContent: MutableLiveData<List<LocalUser>> =
        MutableLiveData(getContentASC())
    val content: LiveData<List<LocalUser>> = _currentDatabaseContent

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        Log.i(TAG, "Cursor DB onCreate")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //if (oldVersion != newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        //}
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun getCursor() = writableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)

    private fun getContentASC(): List<LocalUser> {
        val cursor: Cursor = writableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val list = mutableListOf<LocalUser>()

        cursor.use {
            it.apply {
                if (moveToFirst()) {
                    do {
                        list.add(
                            LocalUser(
                                getInt(getColumnIndex(COLUMN_NAME_ID)),
                                getString(getColumnIndex(COLUMN_NAME_NICKNAME)),
                                getStringValue(COLUMN_NAME_FIRST_NAME),
                                getStringValue(COLUMN_NAME_SECOND_NAME),
                                getIntValue(COLUMN_NAME_AGE)
                            )
                        )
                    } while (moveToNext())
                }
                close()
            }
        }
        return list
    }

    fun updateDbState() {
        _currentDatabaseContent.value = getContentASC()
    }

    override fun getLocalUsersASC(): Flow<List<LocalUser>> {
//        return flow<List<LocalUser>> {
//            val cursor: Cursor = writableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
//            val list = mutableListOf<LocalUser>()
//
//            cursor.use {
//                it.apply {
//                    if (moveToFirst()) {
//                        do {
//                            list.add(
//                                LocalUser(
//                                    getInt(getColumnIndex(COLUMN_NAME_ID)),
//                                    getString(getColumnIndex(COLUMN_NAME_NICKNAME)),
//                                    getStringValue(COLUMN_NAME_FIRST_NAME),
//                                    getStringValue(COLUMN_NAME_SECOND_NAME),
//                                    getIntValue(COLUMN_NAME_AGE)
//                                )
//                            )
//                        } while (moveToNext())
//                    }
//                    close()
//                }
//            }
//            emit(list)
//            Log.i(TAG, "getLocalUsersASC emit: ${list.last()}")
//        }.stateIn(
//            scope = scope,
//            started = SharingStarted.WhileSubscribed(5000L),
//            initialValue = emptyList()
//        )

        return flow {
            emitAll(content.asFlow())
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

        //Log.i(TAG, "Get user list. CURSOR ${list.last()}")
        //return flowOf(list)
    }

    override fun getLocalUsersDESC(): Flow<List<LocalUser>> {
        val cursor: Cursor = writableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val list = mutableListOf<LocalUser>()

        cursor.use {
            it.apply {
                if (moveToLast()) {
                    do {
                        list.add(
                            LocalUser(
                                getInt(getColumnIndex(COLUMN_NAME_ID)),
                                getString(getColumnIndex(COLUMN_NAME_NICKNAME)),
                                getStringValue(COLUMN_NAME_FIRST_NAME),
                                getStringValue(COLUMN_NAME_SECOND_NAME),
                                getIntValue(COLUMN_NAME_AGE)
                            )
                        )
                    } while (moveToPrevious())
                }
                close()
            }
        }
        Log.i(TAG, "Get user list. CURSOR")
        return flowOf(list)
    }

    override suspend fun updateLocalUser(user: LocalUser) {
        // New value for one column
        val values = ContentValues().apply {
            put(COLUMN_NAME_ID, user.id)
            put(COLUMN_NAME_NICKNAME, user.nickname)
            put(COLUMN_NAME_FIRST_NAME, user.firstName)
            put(COLUMN_NAME_SECOND_NAME, user.secondName)
            put(COLUMN_NAME_AGE, user.age)
        }

        // Which row to update, based on the title
        val selection = "$COLUMN_NAME_ID LIKE ?"

        // You may include ?s in the where clause, which will be replaced by the values from whereArgs.
        // The values will be bound as Strings.
        val selectionArgs = arrayOf("${user.id}")
        val updateRows = writableDatabase.use {
            it.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs
            )
        }

        if (updateRows != 0) Log.i(TAG, "Update user complete. CURSOR")
        else Log.e(TAG, "ERROR update user!!!")

        updateDbState()
    }

    override suspend fun insert(user: LocalUser) {
        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(COLUMN_NAME_NICKNAME, user.nickname)
            put(COLUMN_NAME_FIRST_NAME, user.firstName)
            put(COLUMN_NAME_SECOND_NAME, user.secondName)
            put(COLUMN_NAME_AGE, user.age)
        }
        // Insert the new row, returning the primary key value of the new row
        val newRowId = writableDatabase.use {
            it.insert(TABLE_NAME, null, values)
        }

        if (newRowId != 0L) Log.i(TAG, "Insert user complete. CURSOR")
        else Log.e(TAG, "ERROR insert user!!!")

        updateDbState()
    }

    override suspend fun delete(user: LocalUser) {
        // Define 'where' part of query.
        val selection = "$COLUMN_NAME_ID LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf("${user.id}")
        // Issue SQL statement.
        val deletedRows = writableDatabase.use {
            it.delete(TABLE_NAME, selection, selectionArgs)
        }

        if (deletedRows != 0) Log.i(TAG, "Delete user complete. CURSOR")
        else Log.e(TAG, "ERROR delete user!!!")

        updateDbState()
    }

    override suspend fun deleteById(id: Int) {
        // Define 'where' part of query.
        val selection = "$COLUMN_NAME_ID LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf("$id")
        // Issue SQL statement.
        val deletedRows = writableDatabase.use {
            it.delete(TABLE_NAME, selection, selectionArgs)
        }

        if (deletedRows != 0) Log.i(TAG, "Delete user complete. CURSOR")
        else Log.e(TAG, "ERROR delete user by id!!!")

        updateDbState()
    }

    override suspend fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            writableDatabase.use {
                Log.i(
                    TAG,
                    "deleteAll: ${it.delete(TABLE_NAME, "?", arrayOf("1"))}"
                ) // .rawQuery("DELETE FROM $TABLE_NAME", null)
            }
        }

        updateDbState()
    }

    companion object {
        //Database info
        private const val DATABASE_NAME = "user_database"
        private const val DATABASE_VERSION = 2

        //Table info
        private const val TABLE_NAME = "local_user_table"
        private const val COLUMN_NAME_ID = "user_id"
        private const val COLUMN_NAME_NICKNAME = "nickname"
        private const val COLUMN_NAME_FIRST_NAME = "first_name"
        private const val COLUMN_NAME_SECOND_NAME = "second_name"
        private const val COLUMN_NAME_AGE = "age"

        //"$COLUMN_NAME_ID INTEGER PRIMARY KEY," +
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_NAME_ID INTEGER PRIMARY KEY NOT NULL," +
                    "$COLUMN_NAME_NICKNAME TEXT NOT NULL," +
                    "$COLUMN_NAME_FIRST_NAME TEXT," +
                    "$COLUMN_NAME_SECOND_NAME TEXT," +
                    "$COLUMN_NAME_AGE INTEGER)"

        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS $TABLE_NAME"

        @Volatile
        private var INSTANCE: LocalUserCursorDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): LocalUserCursorDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = LocalUserCursorDatabase(context.applicationContext, scope)
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    override fun onBegin() {
        Log.w(TAG, "onBegin")
    }

    override fun onCommit() {
        Log.w(TAG, "onCommit")
    }

    override fun onRollback() {
        Log.w(TAG, "onRollback")
    }
}