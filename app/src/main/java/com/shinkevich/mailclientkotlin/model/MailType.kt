package com.shinkevich.mailclientkotlin.model

import android.content.Context
import com.shinkevich.mailclientkotlin.R

enum class MailType(val nameId: Int) {
    INCOMING(R.string.incoming),
    SENT(R.string.sent),
    DRAFT(R.string.drafts),
    FAVOURITE(R.string.favourites),
    SPAM(R.string.spam),
    DEFERRED(R.string.deferred);

    fun getName(context: Context): String {
        return context.getString(nameId)
    }
}