package wuye.you.min.http

import android.app.Activity
import android.content.Context
import wuye.you.min.http.Connection


object HttpTools {
    fun with(context: Context): Connection {
        val connection = Connection(context)
        connection.setSingletonInstance(connection)
        return connection
    }

    fun with(activity: Activity): Connection {
        val connection = Connection(activity)
        connection.setSingletonInstance(connection)
        return connection
    }
}