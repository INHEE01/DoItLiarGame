package com.example.liargame.listener

interface OnGameEventListener {
    fun onGameStartListener()
    fun onGameSelectListener(liarIndex : Int)
    fun onGameEndListener()
}