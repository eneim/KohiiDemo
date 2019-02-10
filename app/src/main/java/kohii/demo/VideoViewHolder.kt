package kohii.demo

import android.view.View
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

  fun bind(playable: Playable?) {
    playable?.bind(playerView)
  }
}