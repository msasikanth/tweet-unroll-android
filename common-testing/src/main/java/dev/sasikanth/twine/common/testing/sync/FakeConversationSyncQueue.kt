package dev.sasikanth.twine.common.testing.sync

import dev.sasikanth.twine.data.sync.ConversationSyncQueue
import dev.sasikanth.twine.data.sync.ConversationSyncQueueItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class FakeConversationSyncQueue(
  private val itemFactory: (item: ConversationSyncQueueItem) -> Pair<UUID, ConversationSyncQueueItem>
) : ConversationSyncQueue {

  private val items = mutableMapOf<UUID, ConversationSyncQueueItem>()
  private val _mutableQueue = MutableStateFlow<List<ConversationSyncQueueItem>>(emptyList())

  override fun add(item: ConversationSyncQueueItem): UUID {
    val (id, queueItem) = itemFactory.invoke(item)

    items[id] = queueItem

    updateState()

    return id
  }

  override fun remove(item: ConversationSyncQueueItem) {
    val key = items.filterValues { it == item }.keys.first()
    items.remove(key, item)
    updateState()
  }

  override fun queue(): Flow<List<ConversationSyncQueueItem>> {
    return _mutableQueue
  }

  private fun updateState() {
    _mutableQueue.update {
      items.values.toList()
    }
  }
}
