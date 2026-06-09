package com.unitx.signal_core.queue

internal class SignalQueue {
    private val queue = ArrayDeque<QueueEntry>()
    private var current: QueueEntry? = null

    fun enqueue(
        show: () -> Unit,
        dismiss: () -> Unit,
        isShowing: () -> Boolean
    ) {
        val entry = QueueEntry(show, dismiss, isShowing)
        if (current == null || current?.isShowing?.invoke() == false) {
            current = entry
            entry.show()
        } else {
            queue.addLast(entry)
        }
    }

    fun next() {
        current = queue.removeFirstOrNull()
        current?.show()
    }

    fun clear() {
        queue.clear()
        current?.dismiss?.invoke()
        current = null
    }
}