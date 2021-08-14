package com.zhj.bluetooth.sdkdemo.data.model.base

/**
 * Abstract State
 */
open class State

/**
 * Generic Loading State
 */
object ShowLoadingState : State()
object HideLoadingState : State()
