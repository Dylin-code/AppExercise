package dylin.code.appexercise

import android.database.Observable
import android.databinding.ObservableField
import android.util.Log
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class GitHubUserModel(){
    var login : ObservableField<String> = ObservableField("")
    var avatar_url : ObservableField<String> = ObservableField("")
    var url : ObservableField<String> = ObservableField("")
    var site_admin : Boolean = false

    fun getUsers(): ArrayList<GitHubUserModel> {
        var users = ArrayList<GitHubUserModel>()
        val apiUrl = "https://api.github.com/users"
        val okHttpClient = OkHttpClient()
        val request  = Request.Builder().url(apiUrl).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                println("fail : $e")
            }

            override fun onResponse(call: Call?, response: Response?) {
                val responseStr = response!!.body()!!.string()
                val itemList = JSONArray(responseStr)
                for(i in 0..(itemList.length()-1)){
                    var user = JSONObject( itemList.get(i).toString())
                    Log.d("test", user.getString("login") )
                    var data = GitHubUserModel()
                    data.login = ObservableField( user.getString("login"))
                    data.avatar_url = ObservableField( user.getString("avatar_url"))
                    data.url = ObservableField( user.getString("url"))
                    data.site_admin =  user.getBoolean("site_admin")
                    users.add(data)
                }
            }
        })

        return users
    }

}

