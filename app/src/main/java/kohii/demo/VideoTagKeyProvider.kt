package kohii.demo

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import java.util.HashMap

class VideoTagKeyProvider(private val recyclerView: RecyclerView) :
    ItemKeyProvider<String>(SCOPE_CACHED) {

  private val positionToKey = SparseArray<String>()
  private val keyToPosition = HashMap<String, Int>()

  init {
    recyclerView.addOnChildAttachStateChangeListener(
        object : OnChildAttachStateChangeListener {
          override fun onChildViewAttachedToWindow(view: View) {
            onAttached(view)
          }

          override fun onChildViewDetachedFromWindow(view: View) {
            onDetached(view)
          }
        }
    )
  }

  internal /* synthetic access */ fun onAttached(view: View) {
    val holder = recyclerView.findContainingViewHolder(view)
    if (holder is VideoViewHolder) {
      val position = holder.adapterPosition
      val key = holder.tagKey
      if (position != RecyclerView.NO_POSITION) {
        positionToKey.put(position, key)
        keyToPosition[key] = position
      }
    }
  }

  internal /* synthetic access */ fun onDetached(view: View) {
    val holder = recyclerView.findContainingViewHolder(view)
    if (holder is VideoViewHolder) {
      val position = holder.adapterPosition
      val key = holder.tagKey
      if (position != RecyclerView.NO_POSITION) {
        positionToKey.delete(position)
        keyToPosition.remove(key)
      }
    }
  }

  override fun getKey(position: Int): String? {
    return positionToKey.get(position, null)
  }

  override fun getPosition(key: String): Int {
    return keyToPosition[key] ?: RecyclerView.NO_POSITION
  }
}