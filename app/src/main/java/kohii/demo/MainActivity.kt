package kohii.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kohii.v1.Kohii
import kohii.v1.Playback
import kotlinx.android.synthetic.main.activity_main.bottomSheet
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.activity_main.videoOverlay
import kotlinx.android.synthetic.main.overlay_video.overlayPlayerView

class MainActivity : AppCompatActivity(), TransitionListener {

  private var selectionTracker: SelectionTracker<String>? = null
  private var overlaySheet: BottomSheetBehavior<*>? = null
  private var playback: Playback<*>? = null
  private var viewModel: SelectionViewModel? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val kohii = Kohii[this]
    viewModel = ViewModelProviders.of(this)
        .get(SelectionViewModel::class.java)
        .apply {
          liveData.observe(this@MainActivity, Observer {
            if (it.second) { // selected
              overlaySheet?.state = BottomSheetBehavior.STATE_EXPANDED
              playback = kohii.findPlayable(it.first)
                  ?.bind(overlayPlayerView, Playback.PRIORITY_HIGH)
                  .also { pk ->
                    pk?.observe(this@MainActivity)
                  }
            }
          })
        }

    val adapter = ItemsAdapter(kohii)
    recyclerView.adapter = adapter

    val keyProvider = VideoTagKeyProvider(recyclerView)
    selectionTracker = SelectionTracker.Builder<String>(
        getString(R.string.app_name),
        recyclerView,
        keyProvider,
        VideoItemLookup(recyclerView),
        StorageStrategy.createStringStorage()
    )
        .withSelectionPredicate(SelectionPredicates.createSelectSingleAnything())
        .build()
        .also {
          it.onRestoreInstanceState(savedInstanceState)
          adapter.selectionTracker = it
        }

    overlaySheet = BottomSheetBehavior.from(bottomSheet)
        .also { sheet ->
          if (savedInstanceState == null) sheet.state = BottomSheetBehavior.STATE_HIDDEN
          sheet.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onSlide(
              bottomSheet: View,
              slideOffset: Float
            ) {
              (videoOverlay as? MotionLayout)?.progress = 1 - slideOffset.coerceIn(0F, 1F)
            }

            override fun onStateChanged(
              bottomSheet: View,
              state: Int
            ) {
              if (state == BottomSheetBehavior.STATE_HIDDEN) {
                selectionTracker?.clearSelection()
                // Trick: we unbind a Playback if the ViewHolder of the same tag is detached.
                playback?.also {
                  (it.tag as? String)?.let { tag ->
                    val pos = keyProvider.getPosition(tag)
                    if (pos == RecyclerView.NO_POSITION) it.unbind() // <-- that pos is detached.
                  }
                }
                playback = null
              }
            }
          })
        }

    (this.videoOverlay as? MotionLayout)?.setTransitionListener(this)
  }

  override fun onStart() {
    super.onStart()
    selectionTracker?.addObserver(object : SelectionObserver<String>() {
      override fun onItemStateChanged(
        key: String,
        selected: Boolean
      ) {
        viewModel!!.liveData.value = Pair(key, selected)
      }
    })

    // Restore selection.
    selectionTracker?.selection?.firstOrNull()
        ?.let {
          viewModel!!.liveData.value = Pair(it, true)
        }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    selectionTracker?.onSaveInstanceState(outState)
  }

  override fun onDestroy() {
    super.onDestroy()
    recyclerView.adapter = null
  }

  // MotionLayout.TransitionListener

  override fun onTransitionTrigger(
    p0: MotionLayout?,
    p1: Int,
    p2: Boolean,
    p3: Float
  ) = Unit

  override fun onTransitionStarted(
    p0: MotionLayout?,
    p1: Int,
    p2: Int
  ) = Unit

  override fun onTransitionChange(
    p0: MotionLayout?,
    p1: Int,
    p2: Int,
    progress: Float
  ) {
    overlayPlayerView.useController = progress < 0.1
  }

  override fun onTransitionCompleted(
    p0: MotionLayout?,
    p1: Int
  ) = Unit

  override fun onBackPressed() {
    if (!ignoreBackPress()) super.onBackPressed()
  }

  private fun ignoreBackPress(): Boolean {
    return overlaySheet?.let {
      return when {
        it.state == BottomSheetBehavior.STATE_COLLAPSED -> {
          it.state = BottomSheetBehavior.STATE_HIDDEN
          true
        }
        it.state == BottomSheetBehavior.STATE_EXPANDED -> {
          it.state = BottomSheetBehavior.STATE_COLLAPSED
          true
        }
        else -> false
      }
    } ?: false
  }
}
