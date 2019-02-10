package kohii.demo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView.Adapter
import kohii.v1.Kohii

class ItemsAdapter(val kohii: Kohii) : Adapter<VideoViewHolder>() {

  companion object {
    // Big Buck Bunny, CC3.0 license
    val videoUri = "https://video-dev.github.io/streams/x36xhzz/x36xhzz.m3u8".toUri()
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): VideoViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.holder_video, parent, false)
    return VideoViewHolder(view)
  }

  override fun getItemCount(): Int {
    return Int.MAX_VALUE // for demo purpose.
  }

  override fun onBindViewHolder(
    holder: VideoViewHolder,
    position: Int
  ) {
    holder.bind(
        kohii.setUp(videoUri)
            .copy(tag = "$videoUri::$position")
            .asPlayable()
    )
  }

}