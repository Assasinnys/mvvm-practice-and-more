package com.example.mvvm_practice.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the LocalUser class
@Database(entities = [LocalUser::class], version = 2, exportSchema = false)
abstract class LocalUserDatabase : RoomDatabase() {

    abstract fun localUserDao(): LocalUserDao

    companion object {
        @Volatile
        private var INSTANCE: LocalUserDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): LocalUserDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalUserDatabase::class.java,
                    "user_database"
                )
                    .addCallback(LocalUserDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class LocalUserDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.localUserDao())
                    }
                }
            }

            /**
             * Populate the database in a new coroutine.
             * If you want to start with more users, just add them.
             */
            suspend fun populateDatabase(localUserDao: LocalUserDao) {
                // Start the app with a clean database every time.
                // Not needed if you only populate on creation.
                //localUserDao.deleteAll()

                // Add sample users.
                var localUser = LocalUser(
                    nickname = "Assasinnys(Dmitry)",
                    firstName = "Dmitry",
                    secondName = "Androider",
                    age = 228
                )
                localUserDao.insert(localUser)
                localUser = LocalUser(
                    nickname = "AlexKray",
                    firstName = "Alex",
                    secondName = "Androider",
                    age = 227
                )
                localUserDao.insert(localUser)
                localUser = LocalUser(
                    nickname = "Belarussianin",
                    firstName = "Arseni",
                    secondName = "Kasheuski",
                    age = 226
                )
                localUserDao.insert(localUser)
                localUser = LocalUser(
                    nickname = "Bor9",
                    firstName = "Alex",
                    secondName = "Androider",
                    age = 225
                )
                localUserDao.insert(localUser)
                localUser = LocalUser(
                    nickname = "Gen0ciD",
                    firstName = "sql",
                    secondName = "pointer",
                    age = 224
                )
                localUserDao.insert(localUser)
                localUser = LocalUser(
                    nickname = "devlee",
                    firstName = "Kazahstanos",
                    secondName = "Seniorovich",
                    age = 32
                )
                localUserDao.insert(localUser)

                // TODO: Add users!
            }
        }

    }
}