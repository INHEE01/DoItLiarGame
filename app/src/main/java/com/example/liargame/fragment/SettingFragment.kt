package com.example.liargame.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.liargame.DEFINES.GameModeEnum
import com.example.liargame.R
import com.example.liargame.listener.OnGameEventListener

/**
 * 게임 설정 화면
 *
 * TODO
 * 1. 인원
 * 2. 모드
 * 3. 그림모드
 * 4. 제시어 수
 * 5. 게임시작
 */
class SettingFragment(onGameStartListener: OnGameEventListener) : Fragment() {

    private var mOnGameEventListener : OnGameEventListener? = null

    init {
        mOnGameEventListener = onGameStartListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onResume() {
        super.onResume()

        if (view != null) {

            // 인원 감소
            view?.findViewById<LinearLayout>(R.id.fragment_setting_player_count_lower)?.setOnClickListener {
                Log.d("[SettingFragment]", "인원감소 버튼 클릭")
                if (DEFINES.PLAYER_COUNT > DEFINES.MIN_PLAYER_COUNT) {
                    DEFINES.PLAYER_COUNT--
                    Handler(Looper.getMainLooper()).post {
                        view?.findViewById<TextView>(R.id.fragment_setting_player_count_text)?.text =
                            "${DEFINES.PLAYER_COUNT}명"
                    }
                } else {
                    Toast.makeText(context, "최소 3명 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            // 인원 증가
            view?.findViewById<LinearLayout>(R.id.fragment_setting_player_count_higher)?.setOnClickListener {
                Log.d("[SettingFragment]", "인원증가 버튼 클릭")
                if (DEFINES.PLAYER_COUNT < DEFINES.MAX_PLAYER_COUNT) {
                    DEFINES.PLAYER_COUNT++
                    Handler(Looper.getMainLooper()).post {
                        view?.findViewById<TextView>(R.id.fragment_setting_player_count_text)?.text =
                            "${DEFINES.PLAYER_COUNT}명"
                    }
                } else {
                    Toast.makeText(context, "최대 7명까지여야 합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            // 이전 모드
            view?.findViewById<LinearLayout>(R.id.fragment_setting_mode_pre)?.setOnClickListener {
                Log.d("[SettingFragment]", "이전 모드 버튼 클릭")
                Handler(Looper.getMainLooper()).post {
                    view?.findViewById<TextView>(R.id.fragment_setting_mode_text)?.text =
                        if (DEFINES.GAME_MODE== GameModeEnum.NORMAL) (
                            GameModeEnum.SPY.toString()
                            ) else (
                            GameModeEnum.NORMAL.toString()
                            )
                }
            }

            // 이후 모드
            view?.findViewById<LinearLayout>(R.id.fragment_setting_mode_next)?.setOnClickListener {
                Log.d("[SettingFragment]", "다음 모드 버튼 클릭")
                Handler(Looper.getMainLooper()).post {
                    if (DEFINES.GAME_MODE == GameModeEnum.NORMAL) {
                        DEFINES.GAME_MODE = GameModeEnum.SPY
                    } else {
                        DEFINES.GAME_MODE = GameModeEnum.NORMAL
                    }
                    view?.findViewById<TextView>(R.id.fragment_setting_mode_text)?.text = DEFINES.GAME_MODE.toString()
                }
            }

            // 제시어 감소
            view?.findViewById<LinearLayout>(R.id.fragment_setting_hint_count_lower)?.setOnClickListener {
                Log.d("[SettingFragment]", "제시어 감소 버튼 클릭")
                if (DEFINES.HINT_COUNT > DEFINES.MIN_HINT_COUNT) {
                    DEFINES.HINT_COUNT--
                    Handler(Looper.getMainLooper()).post {
                        view?.findViewById<TextView>(R.id.fragment_setting_hint_count_text)?.text =
                            "${DEFINES.HINT_COUNT}"
                    }
                } else {
                    Toast.makeText(context, "최소 3개 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            // 제시어 증가
            view?.findViewById<LinearLayout>(R.id.fragment_setting_hint_count_higher)?.setOnClickListener {
                Log.d("[SettingFragment]", "제시어 증가 버튼 클릭")
                if (DEFINES.HINT_COUNT < DEFINES.MAX_HINT_COUNT) {
                    DEFINES.HINT_COUNT++
                    Handler(Looper.getMainLooper()).post {
                        view?.findViewById<TextView>(R.id.fragment_setting_hint_count_text)?.text =
                            "${DEFINES.HINT_COUNT}"
                    }
                } else {
                    Toast.makeText(context, "최대 9개 이하이어야 합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            // 게임시작
            view?.findViewById<TextView>(R.id.fragment_setting_play_btn)?.setOnClickListener {
                Log.d("[SettingFragment]", "게임 시작 버튼 클릭")
                // 게임 시작 리스너 콜
                mOnGameEventListener?.onGameStartListener()
            }
        }
    }
}