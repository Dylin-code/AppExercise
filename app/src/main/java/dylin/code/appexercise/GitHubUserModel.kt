package dylin.code.appexercise



import android.content.Intent
import android.databinding.ObservableField

import android.graphics.drawable.Drawable
import android.view.View

import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.CountDownLatch


class GitHubUserModel : java.io.Serializable {
    var login : String = ""
    var avatar_url : String = ""
    var url : String = ""
    var site_admin : Boolean = false
    var avatarImage : Drawable? = null
    var bio : String = ""
    var location : String = ""
    var blog : String = ""
    var nextPageLink = ""

    fun getUsers(apiUrl: String = "https://api.github.com/users?since=0" ): ArrayList<GitHubUserModel> {
        var users = ArrayList<GitHubUserModel>()
        val okHttpClient = OkHttpClient()
        val countDownLatch = CountDownLatch(1)
        val request  = Request.Builder().url(apiUrl).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                println("fail : $e")
                countDownLatch.countDown()
            }


            override fun onResponse(call: Call?, response: Response?) {
                val responseStr = response!!.body()!!.string()
                val linkHeader = response.header("Link")
                nextPageLink = linkHeader!!.substring(linkHeader.indexOf("<")+1,linkHeader.indexOf(">"))
                val itemList = JSONArray(responseStr)
                for(i in 0 until itemList.length()){
                    var user = JSONObject( itemList.get(i).toString())
                    var data = GitHubUserModel()
                    data.login = user.getString("login")
                    data.avatar_url = user.getString("avatar_url")
                    data.url = user.getString("url")
                    data.site_admin =  user.getBoolean("site_admin")
                    data.avatarImage = loadImageFromURL(data.avatar_url)
//                    if(i==3)
//                        data.site_admin = true //icon test
                    users.add(data)

                }
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()

        return users
    }

    private fun loadImageFromURL(url: String): Drawable {
        val inputStream: InputStream = URL(url).content as InputStream
        return Drawable.createFromStream(inputStream, "src")
    }

    fun getUserDetail(currentUser:GitHubUserModel , view : View){
        val okHttpClient = OkHttpClient()
        val countDownLatch = CountDownLatch(1)
        val request  = Request.Builder().url(currentUser.url).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                println("fail : $e")
                countDownLatch.countDown()
            }
            override fun onResponse(call: Call?, response: Response?) {
                val responseStr = response!!.body()!!.string()
                var user = JSONObject( responseStr)
                currentUser.bio = user.getString("bio")
                currentUser.location = user.getString("location")
                currentUser.blog = user.getString("blog")
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()
        val intent = Intent(view.context,UserDetailActivity::class.java)
        intent.putExtra("model",currentUser)
        view.context.startActivity(intent)
    }
}

