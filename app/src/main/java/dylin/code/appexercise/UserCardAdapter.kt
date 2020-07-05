package dylin.code.appexercise


import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import dylin.code.appexercise.databinding.ItemRowBinding
import java.util.*

open class UserCardAdapter(
    private val items: MutableList<GitHubUserModel>,
    private val context: Context) :
    RecyclerView.Adapter<UserCardAdapter.ViewHolder>(),
    ItemTouchHelpAdapter {


    private var lastPosition = -1

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemRowBinding>(LayoutInflater.from(parent.context),
            R.layout.item_row, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(var itemRowBinding: ItemRowBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {
        fun bind(obj: GitHubUserModel){
            itemRowBinding.setVariable(BR.userViewModel,obj)
            itemRowBinding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        var dataModel = items[position]
        holder.itemRowBinding.userViewModel = dataModel

        setAnimation(holder.itemRowBinding.root, position)


    }

    private fun setAnimation(view: View, position: Int) {

        if (position > lastPosition) {

            val animation = AnimationUtils.loadAnimation(
                context, android.R.anim.slide_in_left)

            view.startAnimation(animation)

            lastPosition = position
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onItemDismiss(position: Int) {

    }


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {

            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {

            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }


        notifyItemMoved(fromPosition, toPosition)
    }


    fun loadPage(url : String){
        val model = GitHubUserModel()
        var dataModels = model.getUsers(url)
        items.removeAll(items);
        items.addAll(dataModels)
    }
}
