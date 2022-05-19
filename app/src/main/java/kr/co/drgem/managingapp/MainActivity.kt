package kr.co.drgem.managingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.databinding.ActivityMainBinding
import kr.co.drgem.managingapp.models.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

       binding.btnLogin.setOnClickListener{

           val inputId = binding.edtId.text.toString()
           val inputPw = binding.edtPw.text.toString() // MD5 변환해야함

//           JSON 바디로 보낼때 => 해쉬맵을 보내는 방향으로
           val dataMap = hashMapOf(
               "username" to inputId,
               "password" to inputPw,
           )

           apiList.postRequestLogin(
                dataMap,
           ).enqueue(object : Callback<BasicResponse> {
               override fun onResponse(
                   call: Call<BasicResponse>,
                   response: Response<BasicResponse>
               ) {

                   if (response.isSuccessful) {
                       Toast.makeText(mContext, "최종 연결 성공", Toast.LENGTH_SHORT).show()


                       val myIntent = Intent(mContext, MenuActivity::class.java)
                       startActivity(myIntent)
                   }
                   else {
                       Toast.makeText(mContext, "뭔가 결과만 실패", Toast.LENGTH_SHORT).show()
                       Log.d("에러코드", response.errorBody()!!.string())
                   }

               }

               override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                   Toast.makeText(mContext, "서버 연결 실패", Toast.LENGTH_SHORT).show()
                   t.printStackTrace()
                   Log.d("아예실패", t.toString())
               }

           })


        }

    }

    override fun setValues() {

    }


}