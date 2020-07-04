package dylin.code.appexercise

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dylin.code.appexercise.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        val model = GitHubUserModel()
        var dataModel = model.getUsers()
        var recyclerAdapter = UserCardAdapter(dataModel,this)
        binding.myAdapter = recyclerAdapter

    }


}
