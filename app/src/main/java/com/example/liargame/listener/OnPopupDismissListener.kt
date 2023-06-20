package com.example.liargame.listener

interface OnPopupDismissListener {
    fun onWordPopupDismiss(selectWord : String, isAnswer : Boolean)
    fun onSubjectPopupDismiss(subjectIdx : Int)
}