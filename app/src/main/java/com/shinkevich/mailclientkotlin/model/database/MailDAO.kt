package com.shinkevich.mailclientkotlin.model.database

import androidx.room.*
import com.shinkevich.mailclientkotlin.model.MailType

@Dao
interface MailDAO {
    @Insert
    fun insertMail(mail : MailDBEntity)

    @Update
    fun updateMail(mail : MailDBEntity)

    @Delete
    fun deleteMail(mail : MailDBEntity)

    @Query("SELECT * FROM mail WHERE mail_type = :mailType")
    fun getMailsByType(mailType : MailType) : List<MailDBEntity>

    @Query("SELECT * FROM mail WHERE mail_id = :mailID")
    suspend fun getMailByPK(mailID : String) : MailDBEntity?

    @Query("SELECT * FROM mail WHERE  is_in_favourites = 1")
    fun getFavouriteMails() : List<MailDBEntity>

    @Query("DELETE FROM mail")
    suspend fun clear()

}