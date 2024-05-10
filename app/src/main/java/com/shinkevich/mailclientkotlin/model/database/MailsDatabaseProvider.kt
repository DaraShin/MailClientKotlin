package com.shinkevich.mailclientkotlin.model.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MailsDatabaseProvider {
    @Provides
    fun provideMailDao(mailsDatabase: MailsDatabase): MailDAO {
        return mailsDatabase.mailDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): MailsDatabase {
        return Room.databaseBuilder(
            appContext,
            MailsDatabase::class.java,
            "mails_database"
        ).fallbackToDestructiveMigration().build()
    }
}