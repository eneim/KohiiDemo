package kohii.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectionViewModel: ViewModel() {

  val liveData = MutableLiveData<Pair<String, Boolean>>()
}