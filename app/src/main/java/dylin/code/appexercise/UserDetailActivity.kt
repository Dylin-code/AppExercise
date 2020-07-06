package dylin.code.appexercise

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dylin.code.appexercise.databinding.ActivityUserDetailBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch

class UserDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        val user = GitHubUserModel()
        user.name = intent.getStringExtra("name")
        user.bio = intent.getStringExtra("bio")
        user.bio = intent.getStringExtra("bio")
        user.blog = intent.getStringExtra("blog")
        user.login = intent.getStringExtra("login")
        user.location = intent.getStringExtra("location")
        user.avatar_url = intent.getStringExtra("avatar_url")
        user.site_admin = intent.getBooleanExtra("site_admin",false)
        val okHttpClient = OkHttpClient()
        val countDownLatch = CountDownLatch(1)
        val request  = Request.Builder().url(user.avatar_url).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                println("fail : $e")
                countDownLatch.countDown()
            }
            override fun onResponse(call: Call?, response: Response?) {
                user.avatarImage = user.loadImageFromURL(user.avatar_url)
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()

        var binding = DataBindingUtil.setContentView<ActivityUserDetailBinding>(this, R.layout.activity_user_detail)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.user = user
    }
}