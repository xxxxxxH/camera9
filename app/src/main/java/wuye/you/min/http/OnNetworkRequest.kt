package wuye.you.min.http

interface OnNetworkRequest {
    fun onSuccess(response: String?)
    fun onFailure(responseCode: Int, responseMessage: String, errorStream: String)
}