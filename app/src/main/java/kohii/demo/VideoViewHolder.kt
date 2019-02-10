package kohii.demo

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.exoplayer2.ui.PlayerView
import kohii.v1.Playable

@Suppress("MemberVisibilityCanBePrivate")
class VideoViewHolder(
  itemView: View
) : ViewHolder(
    itemView
) {

  val playerView = itemView.findViewById(R.id.playerView) as PlayerView
  val playerContainer = itemView.findViewById(R.id.playerViewContainer) as ViewGroup

  val tagKey: String
    get() = "${ItemsAdapter.videoUrl}::$adapterPosition"

  fun bind(playable: Playable?) {
    playable?.bind(playerView)
  }

  fun getItemDetails(): ItemDetails<String> {
    return object : ItemDetails<String>() {
      override fun getSelectionKey() = tagKey

      override fun getPosition() = adapterPosition
    }
  }
}