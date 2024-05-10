package com.shinkevich.mailclientkotlin.model.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MailServerDependencyProvider {
    /*@Provides
    fun provideMailServerConnector() : MailServerConnector{
        return MailServerConnector()
    }*/

    @Provides
    fun provideMailServerProperties() : MailServerProperties{
        return MailServerProperties()
    }

    /*@Provides
    fun provideServerAuth() : ServerAuth{
        return ServerAuth(provideMailServerProperties())
    }*/
}