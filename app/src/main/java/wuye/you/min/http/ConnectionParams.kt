package wuye.you.min.http

import wuye.you.min.http.Connection


class ConnectionParams {
    private var connection: Connection? = null

    fun setConnectionInstance(connectionInstance: Connection) {
        connection = connectionInstance
    }

    fun ofTypeGet(): Connection {
        connection!!.setRequestType("GET")
        return connection!!
    }

    fun ofTypePost(): Connection {
        connection!!.setRequestType("POST")
        return connection!!
    }

}