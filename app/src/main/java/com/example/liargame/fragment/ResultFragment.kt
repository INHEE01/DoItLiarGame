package com.example.liargame.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.liargame.R
import java.util.*
import kotlin.concurrent.timer

class ResultFragment(liarIndex : Int, selectedWord : String, answerWord : String) : Fragment() {

    private var mLiarIndex : Int
    private var mSelectedWord : String
    private var mAnswerWord : String

    init {
        mLiarIndex = liarIndex
        mSelectedWord = selectedWord
        mAnswerWord = answerWord
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (view != null) {
            view.findViewById<TextView>(R.id.fragment_result_liar_index_text).text = "${mLiarIndex}번"
            view.findViewById<TextView>(R.id.fragment_result_select_word_text).text = mSelectedWord
            view.findViewById<TextView>(R.id.fragment_result_answer_word_text).text = mAnswerWord
        }

        Handler(Looper.getMainLooper()).postDelayed({
            view.findViewById<TextView>(R.id.fragment_result_info_text).visibility = View.VISIBLE
        }, 1000)
    }
}