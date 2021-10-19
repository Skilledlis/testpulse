package com.example.testpulse

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.testpulse.databinding.ActivityMainBinding
import com.example.testpulse.models.UserRequest
import com.example.testpulse.util.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var errorResponse: ErrorResponse


    val userRepository: Repository = Repository()
    val user: MutableLiveData<Resource<UserRequest>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {

            val userAuth = User(
                binding.editTextTextPersonName.text.toString(),
                binding.editTextTextPassword.text.toString()
            )
            Log.d("MainActivity", "$userAuth")
            auth(userAuth)
            user.observe(this, { responce ->
                when (responce) {
                    is Resource.Success -> {
                        responce.data?.let {
                            Log.d(
                                "MainActivityResponse",
                                "userId: ${it.userId} access_token: ${it.access_token}"
                            )
                        }
                        Toast.makeText(this, "Success ${responce.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is Resource.Error -> {


//                        val gson = Gson()
//                        val type = object : TypeToken<ErrorResponse>() {}.type

//                        Log.d("ErrorResponse", "Error data ${error}")
                        Log.d("ErrorResponse", "Error message ${responce.message}")
                    }
                    is Resource.Loading -> {
                        Log.d("ErrorResponse", "Loading ${responce.message}")
                    }
                }

            })
        }

    }


    fun auth(userAuth: User) {
        GlobalScope.launch(Dispatchers.IO) {
            user.postValue(Resource.Loading())
            val response = userRepository.postUserAuth(userAuth)
            user.postValue(handler(response))
        }
    }


    private fun handler(response: Response<UserRequest>): Resource<UserRequest> {
        if (response.isSuccessful) {
            response.body()?.let {
                Log.d("MyBadSuccess", "Body ${response.raw()}")
                Log.d("MyBadSuccess", "Body ${response.body()}")
                return Resource.Success(it)
            }
        }
        if (!response.isSuccessful) {
            val gson = Gson()
            val type = object : TypeToken<ErrorResponse>() {}.type
            errorResponse = gson.fromJson(response.errorBody()?.charStream(), type)
            Log.d("MyBadSuccess", errorResponse.detail)
            Log.d("MyBadSuccess", "Body ${response.errorBody()?.string()}")
        }
        return Resource.Error(response.message())

    }


}