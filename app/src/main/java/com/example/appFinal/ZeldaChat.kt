package com.example.appFinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.*
import com.example.appFinal.databinding.ActivityZeldaChatBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ZeldaChat : AppCompatActivity() {

    //初始化
    private  lateinit var  auth: FirebaseAuth
    private  lateinit var  binding: ActivityZeldaChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityZeldaChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        val sendchat = findViewById<Button>(R.id.sendchat)
        val database = Firebase.database("https://register-945ad-default-rtdb.asia-southeast1.firebasedatabase.app")
        val userRef = database.getReference("Zelda")
        var i = 1
        val email = user?.email
        val emailAsNode =email?.replace(".", "_")
        val layout = findViewById<LinearLayout>(R.id.container)
        val sortedQuery=userRef.child("chats").orderByChild("number")

        sortedQuery.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val values = snapshot.value as? HashMap<String, Any>

                if (values != null) {

                    //設定取得的values資料指定為content,email,number,date
                    val content = values["content"] as? String
                    val email = values["email"] as? String
                    val number = values["number"] as? Long
                    val date = values["date"]as?String

                    val textView = TextView(this@ZeldaChat) //新增的textview

                    //設定textview中的文字
                    textView.text = email.toString() + " : \n\n" + content + "\n\n"+date+"\n-------------------------------------------------------------------"
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    params.leftMargin = 30 //向右30
                    params.height = 400 // 設定高度為 400 像素
                    textView.layoutParams = params //將textview的layout參數設定為params
                    textView.textSize = 20f //設定textview的文字大小
                    layout.addView(textView) //將textview加入到指定的linearLayout並指定index為0(將新增的text加到linearLayout的最上方）
                    i = i + 1 //紀錄跑的迴圈次數
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val values = snapshot.value as? HashMap<String, Any>

                if (values != null) {

                    //設定取得的values資料指定為content,email,number,date
                    val content = values["content"] as? String
                    val email = values["email"] as? String
                    val number = values["number"] as? Long
                    val date = values["date"]as?String


                    val textView = TextView(this@ZeldaChat) //新增的textview

                    //設定textview中的文字
                    textView.text = email.toString() + " : \n\n" + content + "\n\n"+date+"\n-------------------------------------------------------------------"
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    params.leftMargin = 30
                    params.height = 400 // 設定高度為 400 像素
                    textView.layoutParams = params //將textview的layout參數設定為params
                    textView.textSize = 20f //設定textview的文字大小

                    layout.addView(textView) //將textview加入到指定的linearLayout
                    i = i + 1 //紀錄跑的迴圈次數
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        //送出按鈕點擊事件
        sendchat.setOnClickListener{
            val text = findViewById<EditText>(R.id.sendchats)
            val chat = text.text.toString()
            val currentTime = Date()
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()) // 設定時間的格式
            val formattedTime = dateFormat.format(currentTime) // 將時間格式化成字串

            //設定articles為一整組的資料email,number,content,date
            val chats = HashMap<String, Any>()
            chats["email"] = emailAsNode.toString()
            chats["number"] = i
            chats["content"] = chat
            chats["date"] = formattedTime

            //在articles的節點下加入子節點articles"i"並設定值為articles
            userRef.child("chats").child("chat"+i).setValue(chats)
        }

        //回上一頁
        binding.back.setOnClickListener {
            startActivity(Intent(this, Zelda::class.java))
        }

        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        scrollView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                // 確保滾動視窗已經完全加載後再進行滾動
                scrollView.post {

                    // 滾動視窗到最底部
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                }
            }
        })
    }
}