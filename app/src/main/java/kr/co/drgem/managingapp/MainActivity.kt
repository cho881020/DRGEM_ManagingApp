/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : MainActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 프로그램 초 화면으로 로그인 기능
 */

package kr.co.drgem.managingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.databinding.ActivityMainBinding
import kr.co.drgem.managingapp.menu.order.activity.OrderActivity
import kr.co.drgem.managingapp.models.BasicResponse
import kr.co.drgem.managingapp.utils.ContextUtil
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.security.MessageDigest

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupEvents()
        setValues()

        // test용 지울것
        //binding.edtId.setText("22018")
        //binding.edtPw.setText("jung1049&&")
    }

    override fun setupEvents() {

        // 복구 버튼이 필요??
        // 복구 데이터가 존재한다면 모든 자료가 준비되어 있으므로 다음 메뉴로 이동하고, 거기에서 다음 단계로
        // 그리고 또 다음단계 최종 상세 데이터 디스플레이까지 진행되어야 한다??- by jung

        // 복구 작업데이터의 존재로 인하여 복구 버튼이 활성화 되고 이를 클릭하면 서버와 통신하여 자료를 저장하고
        // 메뉴화면으로 이동한다.
        binding.btnRecovery.setOnClickListener {

            val loginWorkInfo = mSqliteDB.getAllLoginWorkCommon()[0]
            val workType      = loginWorkInfo.WORKGUBUN.toString()  // workgubun 언제 업데이트하는지 검사할 것-메뉴에서 하는 것이 가장 간단
            val workSEQ       = loginWorkInfo.WORKNUMBER.toString()

            // JSON 바디로 보낼때 => 해쉬맵을 보내는 방향으로
            val dataMap = hashMapOf(
                "requesttype" to "01001",
                "username"    to loginWorkInfo.USERID!!,
                "password"    to loginWorkInfo.USERPW!!, // md5로 이미 변환되어있음
                "tabletip"    to IPUtil.getIpAddress(), // IP획득 후 첨부
            )

            Log.d("맵확인", dataMap.toString())

            apiList.postRequestLogin(
                dataMap,
            ).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    //Log.d("콜확인", call.request().body().toString())

                    if (response.isSuccessful) {
                        val br = response.body()!!
                        //Log.d("응답확인", br.resultmsg)
                        //Log.d("응답확인", br.resultcd)

                        if (br.resultcd == "000") {

                            ContextUtil.setToken(mContext, br.security_token!!)
                            LoginUserUtil.setLoginData(br)

                            val myIntent =Intent(mContext, MenuActivity::class.java)
                            myIntent.putExtra("name"    , br.sawonmyeong)
                            myIntent.putExtra("workType", workType)
                            myIntent.putExtra("workSEQ" , workSEQ)

                            startActivity(myIntent)
                            finish()
                        } else {
                            binding.loginMSG.text = br.resultmsg
                        }
                    } else {
                        Toast.makeText(mContext, "뭔가 결과만 실패", Toast.LENGTH_SHORT).show()
                        Log.d("에러코드", response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    //Log.d("콜확인", call.request().body().toString())
                    serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                    t.printStackTrace()
                }
            })
        }

        //binding.btnLogin.setOnClickListener {
        //    val myIntent = Intent(mContext, MenuActivity::class.java)
        //    startActivity(myIntent)}

        // 로그인 버튼을 클릭하면
        binding.btnLogin.setOnClickListener {
            val inputId = binding.edtId.text.toString()
            val inputPw = binding.edtPw.text.toString()

            // JSON 바디로 보낼때 => 해쉬맵을 보내는 방향으로
            val dataMap = hashMapOf(
                "requesttype" to "01001",
                "username"    to inputId,
                "password"    to md5(inputPw),          // md5 변환해서 첨부
                "tabletip"    to IPUtil.getIpAddress(), // IP획득 후 첨부
            )

            Log.d("맵확인", dataMap.toString())

            apiList.postRequestLogin(
                dataMap,
            ).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    // Log.d("콜확인", call.request().body().toString())

                    if (response.isSuccessful) {

                        val br = response.body()!!
                        // Log.d("응답확인", br.resultmsg)
                        // Log.d("응답확인", br.resultcd)

                        if (br.resultcd == "000") {

                            val myIntent = Intent(mContext, MenuActivity::class.java)
                            myIntent.putExtra("name", br.sawonmyeong)
                            startActivity(myIntent)
                            Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_SHORT).show()

                            ContextUtil.setToken(mContext, br.security_token!!)
                            LoginUserUtil.setLoginData(br)

                            mSqliteDB.deleteLoginWorkCommon()
                            mSqliteDB.insertLoginWorkCommon(
                                inputId,
                                md5(inputPw),
                                br
                            )
                            finish()
                        } else {
                            binding.loginMSG.text = br.resultmsg
                        }
                    } else {
                        Toast.makeText(mContext, "뭔가 결과만 실패", Toast.LENGTH_SHORT).show()
                        Log.d("에러코드", response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    // Log.d("콜확인", call.request().body().toString())
                    serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                    t.printStackTrace()
                }
            })
        }
    }

    override fun onBackPressed() {
        endDialog()
    }

    override fun setValues() {

        if (mSqliteDB.getAllLoginWorkCommon().size > 0) {
            val workStatus = mSqliteDB.getAllLoginWorkCommon()[0].WORKGUBUN.toString()
            //val workSEQ = mSqliteDB.getAllLoginWorkCommon()[0].WORKNUMBER.toString() //by jung 막음 2022.07.03
            
//            // 복구 데이터 존재 여부 검사 - 복구작업 진행 할 때 사용 - 현재는 미 작업
//            if (workStatus != "None") {
//                binding.btnRecovery.visibility = View.VISIBLE
//            }
//            else {
//                binding.btnRecovery.visibility = View.GONE
//            }
        }
    }

    // password 암호화
    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
            .lowercase()
    }
}