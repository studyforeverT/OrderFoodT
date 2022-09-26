package com.nvc.orderfood.data.source.firebase

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nvc.orderfood.data.source.IAuthSoucreService
import com.nvc.orderfood.model.User
import com.nvc.orderfood.utils.UiState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


// firebase

class AuthRemote @Inject constructor(
    private val authRef: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) :
    IAuthSoucreService {
    private var _userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private var loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        if (this.firebaseAuth.currentUser != null) {
            _userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
        }
    }

    override fun registerUser(
        email: String,
        password: String,
        user: User,
        result: (UiState<String>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun updateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) =
        callbackFlow<Result<User>> {
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                result.invoke(UiState.Failure("Please input your information"))
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val listener = object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val user = snapshot.getValue(User::class.java)
                                    this@callbackFlow.trySendBlocking(Result.success(user!!))
                                    result.invoke(UiState.Success("Login successfully!"))
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
                                }

                            }
                            authRef.child(firebaseAuth.currentUser!!.uid)
                                .addListenerForSingleValueEvent(listener)
                            _userLiveData.postValue(firebaseAuth.currentUser)
                        } else {
                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                result.invoke(UiState.Failure("Email wrong format!"))
                            } else if (password.length < 6) {
                                result.invoke(UiState.Failure("Password must be greater than 6"))
                            } else {
                                if (task.exception is FirebaseAuthException) {
                                    Log.d(
                                        "TAG",
                                        (task.exception as FirebaseAuthException).errorCode
                                    )
                                    when ((task.exception as FirebaseAuthException).errorCode) {
                                        "ERROR_WEAK_PASSWORD" -> result.invoke(UiState.Failure("Password is invalid."))
                                        "ERROR_WRONG_PASSWORD" ->
                                            result.invoke(UiState.Failure("Password incorrect"))
                                        "ERROR_USER_NOT_FOUND" -> result.invoke(UiState.Failure("User not found"))
                                    }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { error -> error.printStackTrace() }
            }
            awaitClose {}

        }


    fun forgotPassword(email: String, result: (UiState<String>) -> Unit) =
        callbackFlow {
            if (TextUtils.isEmpty(email)) {
                result.invoke(UiState.Failure("Please input your information"))
            } else {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        this@callbackFlow.trySendBlocking(Result.success(true))
                        result.invoke(UiState.Success("Login successfully!"))
                    } else {
                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            this@callbackFlow.trySendBlocking(Result.success(false))
                            result.invoke(UiState.Failure("Email wrong format!"))
                        }
                    }
                }.addOnFailureListener { error -> error.printStackTrace() }
            }
            awaitClose {}
        }

    fun changePassword(password: String, result: (UiState<String>) -> Unit) =
        callbackFlow<Result<Boolean>> {
            if (TextUtils.isEmpty(password)) {
                result.invoke(UiState.Failure("Please input your information"))
            } else {
                val user = firebaseAuth.currentUser
                user!!.let {
                    it.updatePassword(password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            this@callbackFlow.trySendBlocking(Result.success(true))
                            result.invoke(UiState.Success("Update successfully!"))
                        } else {

                            if (password.length < 6) {
                                this@callbackFlow.trySendBlocking(Result.success(false))
                                result.invoke(UiState.Failure("Password very weak!"))
                            }
                        }
                    }.addOnFailureListener { error -> error.printStackTrace() }
                }
                awaitClose {}
            }
        }

    override fun logout(result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        firebaseAuth.signOut()
    }

}