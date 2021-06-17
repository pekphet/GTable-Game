package cc.fish91.gtable.base.net.extensions

import java.util.concurrent.Executors

/**
 * Created by fish on 18-2-27.
 */
object ThreadPool {
    val sThreadService by lazy { Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2) }
    fun addTask(task: () -> Unit) {
        sThreadService.submit(task)
    }
}