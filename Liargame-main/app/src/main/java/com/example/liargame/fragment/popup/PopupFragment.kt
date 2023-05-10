package com.example.liargame.fragment.popup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.liargame.DEFINES.DEFINES
import com.example.liargame.DEFINES.SubjectEnum
import com.example.liargame.R
import com.example.liargame.list.WordSelectAdapter
import com.example.liargame.listener.OnPopupDismissListener
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList

class PopupFragment(
    context : Context,
    bundle : Bundle,
    answer : String?,
    list : ArrayList<String>,
    onPopupDismissListener: OnPopupDismissListener
) : DialogFragment() {

    private var mContext : Context

    private var mBundle : Bundle

    private var mAnswer : String? = null

    private var mList : ArrayList<String>? = null

    private var mAdapter : WordSelectAdapter? = null

    private var mPopupDismissListener : OnPopupDismissListener

    private var mAnswerIndex : Int = 0

    init {
        mContext = context
        mBundle = bundle
        mAnswer = answer
        mList = list
        mPopupDismissListener = onPopupDismissListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (mList != null) {

            when (mBundle.getString("BRANCH")) {
                "주제" -> {
                    view.findViewById<TextView>(R.id.fragment_popup_title_text).text = "주제를 선택해주세요"
                    mAdapter = WordSelectAdapter(mBundle.getString("BRANCH")!!, mList!!)
                }
                "제시어" -> {
                    view.findViewById<TextView>(R.id.fragment_popup_title_text).text = "제시어를 선택해주세요"
                    mAdapter = WordSelectAdapter(mBundle.getString("BRANCH")!!, mList!!)
                }
                else -> {
                    Toast.makeText(mContext, "올바른 접근이 아닙니다. 앱을 재시작해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            val mRecyclerView = view.findViewById<RecyclerView>(R.id.fragment_popup_button_list)
            mRecyclerView.adapter = mAdapter
            val layoutManager = LinearLayoutManager(mContext)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            mRecyclerView.layoutManager = layoutManager
//            mRecyclerView.setHasFixedSize(true)

        } else {
            Log.d("[PopupFragment]", "onViewCreated -> mList is null!!")
            return
        }
    }

    /**
     * 리사이클러 뷰에서 클릭된 아이템이 정답인지 아닌지에 따라 분기처리
     */
    override fun onResume() {
        super.onResume()

        mAdapter!!.setItemClickListener(object : WordSelectAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, mode: String) {
                Log.d("[PopupFragment]", "OnItemClickListener -> onItemClick")

                if (mList != null) {
                    when (mode) {
                        "주제" -> {
                            mPopupDismissListener.onSubjectPopupDismiss(position)
                        }
                        "제시어" -> {
                            mPopupDismissListener.onWordPopupDismiss(mList!!.get(position), mList!!.get(position) == mAnswer)
                        }
                        else -> {
                            // Do Nothing
                        }
                    }
                    dismiss()
                }
            }

        })
    }
}