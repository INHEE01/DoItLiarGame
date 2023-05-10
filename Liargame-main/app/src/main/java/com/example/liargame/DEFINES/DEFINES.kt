package com.example.liargame.DEFINES

object DEFINES {

    var MAX_PLAYER_COUNT = 7
    var MIN_PLAYER_COUNT = 3
    var MAX_HINT_COUNT = 9
    var MIN_HINT_COUNT = 3

    var PLAYER_COUNT : Int = 3
    var GAME_MODE : Enum<GameModeEnum> = GameModeEnum.NORMAL
    var HINT_COUNT : Int = 9

    var SUBJECT : Enum<SubjectEnum> = SubjectEnum.OBJECT

    object WORD {
        var OBJECT_LIST : ArrayList<String> = arrayListOf(
            "장난감", "컴퓨터", "마우스", "지갑", "숟가락", "양말", "물통", "나무"
        )

        var FOOD_LIST : ArrayList<String> = arrayListOf(
            "김치찌개", "계란말이", "짜장면", "짬뽕", "치킨", "막국수", "제육볶음", "마카롱"
        )

        var JOB_LIST : ArrayList<String> = arrayListOf(
            "선생님", "의사", "변호사", "버스기사", "마법사", "영화감독", "뮤지컬배우"
        )
    }
}