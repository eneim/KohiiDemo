package kohii.demo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView.Adapter
import kohii.v1.Kohii

class ItemsAdapter(val kohii: Kohii) : Adapter<VideoViewHolder>() {

  companion object {
    // Big Buck Bunny, CC3.0 license
    const val videoUrl = "https://video-dev.github.io/streams/x36xhzz/x36xhzz.m3u8"
  }

  var selectionTracker: SelectionTracker<String>? = null

  @Suppress("RedundantLambdaArrow")
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): VideoViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.holder_video, parent, false)
    return VideoViewHolder(view).also {
      it.playerContainer.setOnClickListener { _ ->
        selectionTracker?.select(it.tagKey)
      }
    }
  }

  override fun getItemCount(): Int {
    return Int.MAX_VALUE // for demo purpose.
  }

  override fun onBindViewHolder(
    holder: VideoViewHolder,
    position: Int
  ) {
    // This ViewHolder is selected --> we must not bind a Playable to it.
    if (this.selectionTracker?.isSelected("$videoUrl::$position") == true) {
      holder.bind(null)
    } else {
      holder.bind(
          kohii.setUp(videoUrl)
              .copy(tag = "$videoUrl::$position")
              .asPlayable()
      )
    }
  }

}