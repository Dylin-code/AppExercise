package dylin.code.appexercise

import android.database.Observable
import android.databinding.ObservableField

class GitHubUserModel(){
    var login : ObservableField<String> = ObservableField("")
    var avatar_url : ObservableField<String> = ObservableField("")
    var url : ObservableField<String> = ObservableField("")
    var site_admin : Boolean = false
}

