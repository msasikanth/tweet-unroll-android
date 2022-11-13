package dev.sasikanth.twine.common.testing.sync

import dev.sasikanth.twine.data.sync.ConversationSyncQueue
import dev.sasikanth.twine.data.sync.ConversationSyncQueueItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

class FakeConversationSyncQueue(
  private val itemFactory: (item: ConversationSyncQueueItem) -> Pair<UUID, ConversationSyncQueueItem>
) : ConversationSyncQueue {

  private val items = mutableMapOf<UUID, ConversationSyncQueueItem>()

  override fun add(item: ConversationSyncQueueItem): UUID {
    val (id, queueItem) = itemFactory.invoke(item)

    items[id] = queueItem

    return id
  }

  override fun remove(item: ConversationSyncQueueItem) {
    val key = items.filterValues { it == item }.keys.first()
    items.remove(key, item)
  }

  override fun queue(): Flow<List<ConversationSyncQueueItem>> {
    return flowOf(items.values.toList())
  }
}
