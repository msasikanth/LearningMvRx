package dev.sasikanth.mvrx

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

data class LoginState(
    val uuid: Async<UUID> = Uninitialized,
    val canLogin: Boolean = false
) : MvRxState

class LoginViewModel(initialState: LoginState) : BaseViewModel<LoginState>(initialState) {

    fun login(username: String, password: String) = withState {
        // If login is already requested return early
        if (it.uuid is Loading) return@withState

        viewModelScope.launch {
            setState {
                copy(uuid = Loading())
            }

            // Handle login process here
            delay(2000)

            setState {
                copy(uuid = Success<UUID>(UUID.randomUUID()))
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        setState {
            copy(canLogin = isUsernameValid(username) && isPasswordValid(password))
        }
    }

    fun reset() {
        setState {
            copy(uuid = Uninitialized)
        }
    }

    private fun isUsernameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
