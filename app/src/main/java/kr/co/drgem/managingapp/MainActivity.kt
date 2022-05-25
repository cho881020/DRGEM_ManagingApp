package kr.co.drgem.managingapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.databinding.ActivityMainBinding
import kr.co.drgem.managingapp.models.BasicResponse
import kr.co.drgem.managingapp.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.security.MessageDigest

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

//        binding.btnLogin.setOnClickListener {
//            val myIntent = Intent(mContext, MenuActivity::class.java)
//            startActivity(myIntent)}

       binding.btnLogin.setOnClickListener{

           val inputId = binding.edtId.text.toString()
           val inputPw = binding.edtPw.text.toString()



//           JSON 바디로 보낼때 => 해쉬맵을 보내는 방향으로
           val dataMap = hashMapOf(
               "username" to inputId,
               "password" to md5(inputPw), // md5 변환해서 첨부
           )

           Log.d("맵확인", dataMap.toString())

           apiList.postRequestLogin(dataMap,
           ).enqueue(object : Callback<BasicResponse> {
               override fun onResponse(
                   call: Call<BasicResponse>,
                   response: Response<BasicResponse>

               ) {

                   Log.d("콜확인", call.request().body().toString())

                   if (response.isSuccessful) {

                       val br = response.body()!!
                       Log.d("응답확인", br.resultmsg)

                       if (br.resultcd.toInt() == 200) {

                           val myIntent = Intent(mContext, MenuActivity::class.java)
                           myIntent.putExtra("name", br.sawonmyeong)
                           startActivity(myIntent)
                           Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_SHORT).show()

                           ContextUtil.setToken(mContext, br.security_token)

                       }
                       else {

                           Toast.makeText(mContext, br.resultmsg, Toast.LENGTH_SHORT).show()
                       }

                   }
                   else {
                       Toast.makeText(mContext, "뭔가 결과만 실패", Toast.LENGTH_SHORT).show()
                       Log.d("에러코드", response.errorBody()!!.string())
                   }

               }

               override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                   Log.d("콜확인", call.request().body().toString())
                   Toast.makeText(mContext, "서버 연결 실패", Toast.LENGTH_SHORT).show()
                   t.printStackTrace()
               }

           })

        }

    }



    override fun setValues() {

    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0').lowercase()
    }

}