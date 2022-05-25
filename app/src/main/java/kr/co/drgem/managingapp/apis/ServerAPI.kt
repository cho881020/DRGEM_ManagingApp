package kr.co.drgem.managingapp.apis

import android.content.Context
import com.google.gson.GsonBuilder
import kr.co.drgem.managingapp.utils.ContextUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ServerAPI {

    companion object {

        private var retrofit : Retrofit? = null
        private var DRGEM_LOCAL_SERVER_URL = "http://192.168.58.39"
        private var DEV_SERVER_URL = "개발서버주소"
        private var LIVE_SERVER_URL = "상용서버주소" // 주소 확정 나면 둘 다 변경
        private val BASE_URL = DRGEM_LOCAL_SERVER_URL

        fun getRetrofit(context: Context) : Retrofit {


            if (retrofit == null) {

                val interceptor = Interceptor {
                    with(it) {

//                       기존의 request에, 헤더를 추가해주자.

                        val newRequest = request().newBuilder()
                            .addHeader("accept", "application/json")
                            .addHeader("content-type", "application/json")
                            .addHeader("Authorization", ContextUtil.getToken(context))   // 서버개발자 토큰 생성 방식 확인 필요
                            .build()

//                       다시 하려던 일을 이어가도록
                        proceed(newRequest)
                    }
                }


                val myClient = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()


                val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")  // 서버 양식 확인 필요
                    .registerTypeAdapter(
                        Date::class.java,
                        DateDeserializer()
                    ) // 어떤 형태의 자료형에 적용시킬지?  Date클래스로 파싱.
                    .create()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL) // 어느 서버를 기반으로 움직일건지.
                    .addConverterFactory( GsonConverterFactory.create( gson ) ) // gson 라이브러리와 결합 + Date 파싱 요령 첨부
                    .client(myClient)  // 인터셉터를 부착해둔 클라이언트로 통신하도록.
                    .build()
            }

            return retrofit!!

        }

    }
}