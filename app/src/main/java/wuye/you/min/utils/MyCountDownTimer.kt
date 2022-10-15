package wuye.you.min.utils

import android.os.CountDownTimer

class MyCountDownTimer constructor(total: Long, internal: Long, private val listener: CountDownFinishListener) : CountDownTimer(
    total, internal
) {
    init {
        start()
    }

    override fun onTick(p0: Long) {
        "leave time : $p0".print()
    }

    override fun onFinish() {
        "count down done".print()
        listener.countDownFinish()
        cancel()
    }
}