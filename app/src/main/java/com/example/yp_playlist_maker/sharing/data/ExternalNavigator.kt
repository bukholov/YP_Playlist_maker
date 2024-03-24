package com.example.yp_playlist_maker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.yp_playlist_maker.R

class ExternalNavigator(val context: Context) {
    fun shareLink(link: String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = context.getString(R.string.share_app_text_plain)
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun openLink(link: String){
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    fun openEmail(emailData: EmailData){
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(emailData.mailTo)
            putExtra(Intent.EXTRA_EMAIL, emailData.email)
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
        }
       context.startActivity(emailIntent)
    }
}