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
        holder.bind(dataModel)

        // 設定動畫效果
        setAnimation(holder.itemRowBinding.root, position)

        // 建立與註冊是否選擇元件的點擊監聽物件
//        holder.selected_check.setOnClickListener {
//            // 設定目前位置項目物件的是否選擇狀態
//            fruit.isSelected = holder.selected_check.isChecked
//        }
    }

    // 設定動畫效果
    private fun setAnimation(view: View, position: Int) {
        // 如果是最後一個項目
        if (position > lastPosition) {
            // 建立 Animation 動畫物件, 指定效果為由左方滑入
            val animation = AnimationUtils.loadAnimation(
                context, android.R.anim.slide_in_left)
            // 啟動動畫效果
            view.startAnimation(animation)
            // 儲存最後一個項目的位置
            lastPosition = position
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    // 實作 ItemTouchHelperAdapter 介面的方法
    // 左右滑動項目
    override fun onItemDismiss(position: Int) {

    }

    // 實作 ItemTouchHelperAdapter 介面的方法
    // 長按並移動項目
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            // 如果是往下拖拉
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            // 如果是往上拖拉
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }

        // 通知資料項目已經移動
        notifyItemMoved(fromPosition, toPosition)
    }

}
