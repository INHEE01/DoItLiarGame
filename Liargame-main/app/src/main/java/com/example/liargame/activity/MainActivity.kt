package com.example.liargame.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.liargame.DEFINES.DEFINES
import com.example.liargame.DEFINES.SubjectEnum
import com.example.liargame.R
import com.example.liargame.fragment.GameFragment
import com.example.liargame.fragment.ResultFragment
import com.example.liargame.fragment.SettingFragment
import com.example.liargame.fragment.popup.PopupFragment
import com.example.liargame.list.WordSelectAdapter
import com.example.liargame.listener.OnGameEventListener
import com.example.liargame.listener.OnPopupDismissListener
import java.util.*
import javax.security.auth.Subject
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

/**
 * 메인화면
 *
 * TODO
 * 1. 게임을 플레이할 수 있는 실제 Fragment를 넣어둠
 * 2. 주제 표출
 * 3. 주제 변경
 * 4. 재시작
 * 5. 뒤로가기 (앱 종료)
 * 6. 무작위로 제시어 선택하기
 */
class MainActivity : AppCompatActivity(), OnGameEventListener, OnPopupDismissListener {

    private var backPressedTime : Int = 0

    private var transaction : FragmentTransaction? = null

    private var mWord : String? = null

    private var mList : ArrayList<String>? = null

    private var mLiarIndex : Int = 0

    /**
     * 변경 버튼, 재시작 visible 처리
     * 
     * true 
     * 1. 변경 버튼 보이게 
     * 2. 재시작 버튼 안 보이게
     * 
     * false
     * 1. 변경 버튼 안 보이게
     * 2. 재시작 버튼 보이게
     */
    private var isClickableSubjectBtn : Boolean by Delegates.observable(true) { _, oldValue, newValue ->
        Log.d("[MainActivity]", "clickableSubjectBtn -> old : $oldValue / new : $newValue")
        when (newValue) {
            true -> {
                    findViewById<TextView>(R.id.activity_main_subject_btn_layout).visibility = View.VISIBLE
            }
            false -> {
                    findViewById<TextView>(R.id.activity_main_subject_btn_layout).visibility = View.GONE
            }
        }

    }

    /**
     * 제시어 선택 버튼 보이게
     *
     * true  -> 보이게
     * false -> 안 보이게
     */
    var isSelectWord : Boolean by Delegates.observable(false) { _, oldValue, newValue ->
        Log.d("[MainActivity]", "isSelectWord -> old : $oldValue / new : $newValue")
        when (newValue) {
            true -> {
                findViewById<LinearLayout>(R.id.activity_main_select_button).visibility = View.VISIBLE
            }
            false -> {
                findViewById<LinearLayout>(R.id.activity_main_select_button).visibility = View.GONE
            }
        }
    }

    private var isShowResetButton : Boolean by Delegates.observable(false) { _, oldValue, newValue ->
        Log.d("[MainActivity]", "isShowResetButton -> old : $oldValue / new : $newValue")
        when (newValue) {
            true -> {
                findViewById<LinearLayout>(R.id.activity_main_restart_button).visibility = View.VISIBLE
                isClickableSubjectBtn = false
            }
            false -> {
                findViewById<LinearLayout>(R.id.activity_main_restart_button).visibility = View.GONE
                isClickableSubjectBtn = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        isShowResetButton = false

        // 초기 Fragment (SettingFragment) call
        transaction = supportFragmentManager.beginTransaction()
        transaction!!.replace(R.id.activity_main_game_frame_layout, SettingFragment(this))
        transaction!!.commit()

        initWidgets()
    }

    // 뒤로가기 버튼 클릭 시 호출
    override fun onBackPressed() {
        if (backPressedTime == 0) {
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            backPressedTime = System.currentTimeMillis().toInt()
        } else {
            if (System.currentTimeMillis() - backPressedTime < 2000) {
                super.onBackPressed()
                moveTaskToBack(true)
                finish()
            } else {
                Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                backPressedTime = System.currentTimeMillis().toInt()
            }
        }
    }

    // 버튼 클릭 이벤트 동작 선언
    private fun initWidgets() {

        // 변경 버튼
        findViewById<TextView>(R.id.activity_main_subject_btn_layout).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("BRANCH", "주제")
            val popup = PopupFragment(applicationContext, bundle, mWord, arrayListOf("물건", "음식", "직업"), this)
            popup.show(supportFragmentManager, "주제")
        }

        // 재시작 버튼
        findViewById<LinearLayout>(R.id.activity_main_restart_button).setOnClickListener {
            resetGame()
        }

        // 제시어 선택 버튼
        findViewById<LinearLayout>(R.id.activity_main_select_button).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("BRANCH", "제시어")
            val popup = PopupFragment(applicationContext, bundle, mWord, mList!!, this)
            popup.show(supportFragmentManager, "제시어")
        }

    }

    /** OnGameEventListener **/
    override fun onGameStartListener() {
        Log.d("[MainActivity]", "onGameStartListener called -> Game Start!!")
        try {
            selectAnswer()
            isSelectWord = false
            if (mWord != null) {
                isShowResetButton = true
//                mFragment = GameFragment(mWord!!, this)
                transaction = supportFragmentManager.beginTransaction()
                transaction!!.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                transaction!!.add(R.id.activity_main_game_frame_layout, GameFragment(mWord!!, this))
                transaction!!.addToBackStack(null)
                transaction!!.commit()
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    override fun onGameSelectListener(liarIndex : Int) {
        Log.d("[MainActivity]", "onGameSelectListener called -> Find Liar!!")
        isSelectWord = true
        mLiarIndex = liarIndex
    }

    override fun onGameEndListener() {
        Log.d("[MainActivity]"," onGameEndListener called -> Game End!!")
        isSelectWord = false
        mLiarIndex = 0
        resetGame()
    }

    /** OnPopupDismissListener **/
    override fun onWordPopupDismiss(selectWord : String, isAnswer: Boolean) {
        Log.d("[MainActivity]"," onPopupDismissListener called -> Game End!!")

        when (isAnswer) {
            true -> {
                Toast.makeText(this, "라이어가 맞췄습니다!!", Toast.LENGTH_SHORT).show()
//                resetGame()
            }
            false -> {
                Toast.makeText(this, "라이어가 틀렸습니다!!", Toast.LENGTH_SHORT).show()
            }
        }
        isSelectWord = false

        try {
            transaction = supportFragmentManager.beginTransaction()
            transaction!!.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction!!.add(R.id.activity_main_game_frame_layout, ResultFragment(mLiarIndex, selectWord, mWord!!))
            transaction!!.commit()
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 주제 변경
     *
     * TODO
     * 1. 화면 주제 텍스트 변경
     * 2. DEFINES.SUBJECT ENUM 변경
     *
     * --- 시점 상 안 해도 됨 ---
     * 3. mList 변경(?)
     * 4. mWord 변경
     */
    override fun onSubjectPopupDismiss(index: Int) {
        changeSubject(index)
    }

    private fun changeSubject(index : Int) {
        DEFINES.SUBJECT = SubjectEnum.values().get(index)

        findViewById<TextView>(R.id.activity_main_subject_text).text =
            when (DEFINES.SUBJECT) {
                SubjectEnum.JOB -> "직업"
                SubjectEnum.FOOD -> "음식"
                SubjectEnum.OBJECT -> "물건"
                else -> {
                    DEFINES.SUBJECT = SubjectEnum.OBJECT
                    "물건"
                }
            }
    }

    private fun resetGame() {
        try {
            isShowResetButton = false
//            mFragment = SettingFragment(this)
            transaction = supportFragmentManager.beginTransaction()
            transaction!!.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction!!.replace(R.id.activity_main_game_frame_layout, SettingFragment(this))
            transaction!!.commit()
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    private fun selectAnswer() {
        when (DEFINES.SUBJECT) {
            SubjectEnum.OBJECT -> mList = DEFINES.WORD.OBJECT_LIST
            SubjectEnum.FOOD -> mList = DEFINES.WORD.FOOD_LIST
            SubjectEnum.JOB -> mList = DEFINES.WORD.JOB_LIST
        }
        var index = Random().nextInt(mList!!.size)
        mWord = mList!!.get(index)
    }

}