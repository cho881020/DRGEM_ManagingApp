/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : BaseActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 모든 액티비티에 전체 적용되는 기본설정
 */

package kr.co.drgem.managingapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.co.drgem.managingapp.apis.APIList
import kr.co.drgem.managingapp.apis.ServerAPI
import kr.co.drgem.managingapp.localdb.DBHelper
import kr.co.drgem.managingapp.localdb.SQLiteDB

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext: Context

    lateinit var apiList: APIList

    lateinit var mSqliteDB : SQLiteDB



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

        val retrofit = ServerAPI.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)

        val dbHelper = DBHelper(mContext, "drgemdb.db", null, 1)
        mSqliteDB = SQLiteDB()
        mSqliteDB.makeDb(dbHelper.writableDatabase)


    }

    abstract fun setupEvents()

    abstract fun setValues()

    fun backDialog(runnable: Runnable?) {

        AlertDialog.Builder(mContext)
            .setTitle("아직 저장하지 않은 사항이 있습니다.")
            .setMessage("그래도 이 화면을 종료하시겠습니까?")
            .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->

                runnable?.let {
                    Handler(Looper.getMainLooper()).post(it)
                }
                finish()

            })
            .setNegativeButton("아니오", null)
            .show()

    }

    fun saveDialog(runnable: Runnable?){

        AlertDialog.Builder(mContext)
            .setTitle("저장하시겠습니까?")
            .setMessage("변경 사항이 저장됩니다.")
            .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                runnable?.let {
                    Handler(Looper.getMainLooper()).post(it)
                }
            })
            .setNegativeButton("아니오", null)
            .show()
    }


    fun endDialog(){
        AlertDialog.Builder(mContext)
            .setMessage("종료하시겠습니까?")
            .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                finishAndRemoveTask()
                android.os.Process.killProcess(android.os.Process.myPid())
            })
            .setNegativeButton("아니오", null)
            .show()
    }

    fun saveDoneDialog(){
        AlertDialog.Builder(mContext)
            .setMessage("저장이 완료되었습니다.")
            .setNegativeButton("확인", null)
            .show()
    }

    fun saveNotDoneDialog(){
        AlertDialog.Builder(mContext)
            .setMessage("저장할 자료가 없습니다.")
            .setNegativeButton("확인", null)
            .show()
    }


    fun searchZeroDialog(){
        AlertDialog.Builder(mContext)
            .setMessage("검색된 내역이 없습니다.")
            .setNegativeButton("확인", null)
            .show()
    }

    fun countSerialDialog(){
        AlertDialog.Builder(mContext)
            .setMessage("입력 수량과 시리얼넘버 수량이 일치하지 않습니다.")
            .setNegativeButton("확인", null)
            .show()
    }

    fun serverErrorDialog(message : String){
        AlertDialog.Builder(mContext)
            .setMessage(message)
            .setNegativeButton("확인", null)
            .show()
    }

    fun status333Dialog(runnable: Runnable?){

        AlertDialog.Builder(mContext)
            .setTitle("현재 진행중인 작업이 있습니다.")
            .setMessage("정말 재 조회 하시겠습니까?")
            .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                runnable?.let {
                    Handler(Looper.getMainLooper()).post(it)
                }
            })
            .setNegativeButton("아니오", null)
            .show()
    }


}