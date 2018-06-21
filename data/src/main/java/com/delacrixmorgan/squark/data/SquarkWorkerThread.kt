package com.delacrixmorgan.squark.data

import android.os.Handler
import android.os.HandlerThread

class SquarkWorkerThread(threadName: String) : HandlerThread(threadName) {
    private lateinit var workerHandler: Handler

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        this.workerHandler = Handler(looper)
    }

    fun postTask(task: Runnable) {
        this.workerHandler.post(task)
    }
}