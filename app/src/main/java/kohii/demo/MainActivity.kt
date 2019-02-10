package kohii.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver
import androidx.recyclerview.selection.StorageStrategy
import kohii.v1.Kohii
import kotlinx.android.synthetic.main.activity_main.recyclerView

class MainActivity : AppCompatActivity() {

  private var selectionTracker: SelectionTracker<String>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val adapter = ItemsAdapter(Kohii[this])
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
  }

  override fun onStart() {
    super.onStart()
    selectionTracker?.addObserver(object : SelectionObserver<String>() {
      override fun onItemStateChanged(
        key: String,
        selected: Boolean
      ) {
        if (selected) {
          Toast.makeText(this@MainActivity, "Selected: $key", Toast.LENGTH_SHORT)
              .show()
        } else {
          Toast.makeText(this@MainActivity, "Unselected: $key", Toast.LENGTH_SHORT)
              .show()
        }
      }
    })
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    selectionTracker?.onSaveInstanceState(outState)
  }

  override fun onDestroy() {
    super.onDestroy()
    recyclerView.adapter = null
  }
}
