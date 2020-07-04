package dylin.code.appexercise

interface ItemTouchHelpAdapter {
    // long touch and move
    fun onItemMove(fromPosition: Int, toPosition: Int)

    // left right move
    fun onItemDismiss(position: Int)

}