/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : BaseDialogFragment
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 모든 시리얼번호 입력 다이얼로그에 전체 적용되는 기본설정
 */

package kr.co.drgem.managingapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kr.co.drgem.managingapp.apis.APIList
import kr.co.drgem.managingapp.apis.ServerAPI
import kr.co.drgem.managingapp.localdb.DBHelper
import kr.co.drgem.managingapp.localdb.SQLiteDB

abstract class BaseDialogFragment : DialogFragment() {

    lateinit var mContext: Context

    lateinit var apiList: APIList

    lateinit var mSqliteDB: SQLiteDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = requireContext()

        val retrofit = ServerAPI.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)

        val dbHelper = DBHelper(mContext, "drgemdb.db", null, 1)
        mSqliteDB = SQLiteDB()
        mSqliteDB.makeDb(dbHelper.writableDatabase)


    }

    abstract fun setupEvents()

    abstract fun setValues()

    fun saveDoneDialog() {
        AlertDialog.Builder(mContext)
            .setMessage("등록이 완료되었습니다.")
            .setNegativeButton("확인", null)
            .show()
    }

}