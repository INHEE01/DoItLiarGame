package com.example.liargame.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.liargame.DEFINES.DEFINES
import com.example.liargame.R
import com.example.liargame.activity.MainActivity
import com.example.liargame.fragment.popup.PopupFragment
import com.example.liargame.listener.OnGameEventListener
import org.w3c.dom.Text
import java.util.*
import kotlin.properties.Delegates

/**
 * 실제 게임 화면
 *
 * TODO
 * 1. 라이어가 아닌 사람들에게만 제시어 보여줌
 * 2. 라이어인 사람에게는 제시어 안 보여줌
 * 3. 다 보여주고 나면 범인색출 view show
 */
class GameFragment(word : String, onGameEventListener: OnGameEventListener) : Fragment(){

    private var mWord : String
    private var mOnGameEventListener : OnGameEventListener

    private var playerIndex = 0
    private var liarIndex = 0

    private var isShowCover : Boolean by Delegates.observable(true) { _, oldValue, newValue ->
        Log.d("[GameFragment]", "isShowCover -> newValue : $newValue")
        if (view != null) {
            when (newValue) {
                true -> {
                    view?.findViewById<LinearLayout>(R.id.fragment_game_show_button_layout)?.visibility = View.VISIBLE
                    view?.findViewById<LinearLayout>(R.id.fragment_game_show_word_layout)?.visibility = View.GONE
                }
                false -> {
                    view?.findViewById<LinearLayout>(R.id.fragment_game_show_button_layout)?.visibility = View.GONE
                    view?.findViewById<LinearLayout>(R.id.fragment_game_show_word_layout)?.visibility = View.VISIBLE
                }
            }
        }
    }

    private var isLiar : Boolean by Delegates.observable(false) { _, oldValue, newValue ->
        Log.d("[GameFragment]", "isLiar -> newValue : $newValue")
        if (view != null) {
            when (newValue) {
                true -> {
                    view?.findViewById<LinearLayout>(R.id.fragment_game_show_button_layout)?.visibility = View.GONE
                    view?.findViewById<LinearLayout>(R.id.fragment_game_show_word_layout)?.visibility = View.VISIBLE

                    view?.findViewById<LinearLayout>(R.id.fragment_game_liar_layout)?.visibility = View.VISIBLE
                    view?.findViewById<LinearLayout>(R.id.fragment_game_not_liar_layout)?.visibility = View.GONE
                }
                false -> {
                    view?.findViewById<LinearLayout>(R.id.fragment_game_show_button_layout)?.visibility = View.GONE
                    view?.findViewById<LinearLayout>(R.id.fragment_game_show_word_layout)?.visibility = View.VISIBLE

                    view?.findViewById<LinearLayout>(R.id.fragment_game_liar_layout)?.visibility = View.GONE
                    view?.findViewById<LinearLayout>(R.id.fragment_game_not_liar_layout)?.visibility = View.VISIBLE
                }
            }
        }
    }

    init {
        mWord = word
        mOnGameEventListener = onGameEventListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        liarIndex = Random().nextInt(DEFINES.PLAYER_COUNT)

        initWidgets()
    }

    private fun initWidgets() {
        if (view != null) {

            /**
             * 제시어 확인 버튼
             *
             * TODO
             * 1. 커버 뷰 gone
             * 2. 라이어 판별 -> 라이어 뷰 visible
             * 3. 제시어 확인 시 순서 텍스트 할당
             */
            view?.findViewById<TextView>(R.id.fragment_game_show_button)?.setOnClickListener {
                isShowCover = false
                isLiar = (playerIndex == liarIndex)
                if (isLiar) {
                    view?.findViewById<TextView>(R.id.fragment_game_liar_index_text)?.text = "설명 순서 : ${playerIndex+1}번"
                } else {
                    view?.findViewById<TextView>(R.id.fragment_game_not_liar_word_text)?.text = mWord
                    view?.findViewById<TextView>(R.id.fragment_game_not_liar_index_text)?.text = "설명 순서 : ${playerIndex+1}번"
                }
            }

            /**
             * 확인 완료 버튼
             *
             * TODO
             * 1. 커버 뷰 visible
             * 2. 현재 순서 증가
             * 3. 순서가 다 끝났으면 범인색출 뷰 visible
             */
            view?.findViewById<TextView>(R.id.fragment_game_check_word_button)?.setOnClickListener {
                isShowCover = true
                playerIndex++
                if (playerIndex >= DEFINES.PLAYER_COUNT) {
                    view?.findViewById<LinearLayout>(R.id.fragment_game_show_button_layout)?.visibility = View.GONE
                    view?.findViewById<LinearLayout>(R.id.fragment_game_show_word_layout)?.visibility = View.GONE
                    view?.findViewById<LinearLayout>(R.id.fragment_game_find_liar_layout)?.visibility = View.VISIBLE
                }
            }

            /**
             * 범인 색출 화면
             *
             * TODO
             * 1. 현재 게임을 리셋
             */
            view?.findViewById<LinearLayout>(R.id.fragment_game_find_liar_layout)?.setOnClickListener {
                mOnGameEventListener.onGameSelectListener(liarIndex)
            }
        }
    }
}