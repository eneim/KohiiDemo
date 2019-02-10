package kohii.demo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter

class ItemsAdapter : Adapter<VideoViewHolder>() {

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
    // TODO actually bind stuff.
  }

}