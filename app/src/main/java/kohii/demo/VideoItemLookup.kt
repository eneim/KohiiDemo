package kohii.demo

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class VideoItemLookup(private val parent: RecyclerView) : ItemDetailsLookup<String>() {
  override fun getItemDetails(event: MotionEvent): ItemDetails<String>? {
    val view = parent.findChildViewUnder(event.x, event.y)
    if (view != null) {
      return (parent.getChildViewHolder(view) as VideoViewHolder).getItemDetails()
    }
    return null
  }
}