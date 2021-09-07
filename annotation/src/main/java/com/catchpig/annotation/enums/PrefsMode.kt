package com.catchpig.annotation.enums

/**
 * SharedPreferences模式
 * @author catchpig
 * @date 2019/10/29 0029
 */
enum class PrefsMode(val value:Int) {
    MODE_PRIVATE(0x0000),
    MODE_WORLD_READABLE(0x0001),
    MODE_WORLD_WRITEABLE(0x0002),
    MODE_MULTI_PROCESS(0x0004),
}