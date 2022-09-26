package com.nvc.orderfood.data.source.firebase

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.nvc.orderfood.data.source.IAuthSoucreService
import com.nvc.orderfood.model.User
import com.nvc.orderfood.utils.UiState
import java.util.regex.Pattern
import javax.inject.Inject

class SignUpRemote @Inject constructor(
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
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(user.nameUser)
            || TextUtils.isEmpty(user.addressUser) || TextUtils.isEmpty(user.phoneUser)
        ) {
            result.invoke(UiState.Failure("Please input your information"))
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        authRef
                            .child(firebaseAuth.currentUser!!.uid)
                            .setValue(user.apply { id = firebaseAuth.currentUser!!.uid })
                            .addOnSuccessListener {
                                firebaseAuth.signOut()
                                result.invoke(UiState.Success("Sign up successfully!"))
                            }
                            .addOnFailureListener {
                                result.invoke(
                                    UiState.Failure(
                                        it.localizedMessage
                                    )
                                )
                            }

                    } else {
                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            result.invoke(UiState.Failure("Email wrong format!"))
                        } else if (password.length < 6) {
                            result.invoke(UiState.Failure("Password must be greater than 6"))
                        } else if (!Pattern.matches(
                                "^[+]?[0-9]{10,11}\$",
                                user.phoneUser.toString()
                            )
                        ) {
                            result.invoke(UiState.Failure("Invalid phone!"))
                        } else {
                            if (task.exception is FirebaseAuthException) {
                                Log.d(
                                    "TAG", (task.exception as FirebaseAuthException).errorCode
                                )
                                when ((task.exception as FirebaseAuthException).errorCode) {
                                    "ERROR_WEAK_PASSWORD" -> result.invoke(UiState.Failure("Password is invalid."))
                                    "ERROR_EMAIL_ALREADY_IN_USE" -> result.invoke(UiState.Failure("Email already registered"))
                                }
                            }
                        }
                    }

                }
                .addOnFailureListener { error -> error.printStackTrace() }

        }
    }

    override fun updateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        //
    }


    override fun logout(result: (UiState<String>) -> Unit) {
        //
    }

}