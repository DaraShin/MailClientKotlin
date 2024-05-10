package com.shinkevich.mailclientkotlin.model.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MailDBEntity::class], version = 5)
abstract class MailsDatabase : RoomDatabase() {
    abstract fun mailDao(): MailDAO
}