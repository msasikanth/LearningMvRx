package dev.sasikanth.mvrx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import kotlinx.android.synthetic.main.fragment_login.loading
import kotlinx.android.synthetic.main.fragment_login.loginButton
import kotlinx.android.synthetic.main.fragment_login.passwordInput
import kotlinx.android.synthetic.main.fragment_login.usernameInput

class LoginFragment : BaseMvRxFragment() {

    private val viewModel: LoginViewModel by fragmentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usernameInput.doAfterTextChanged {
            viewModel.loginDataChanged(
                username = usernameInput.text?.toString().orEmpty(),
                password = passwordInput.text?.toString().orEmpty()
            )
        }

        passwordInput.doAfterTextChanged {
            viewModel.loginDataChanged(
                username = usernameInput.text?.toString().orEmpty(),
                password = passwordInput.text?.toString().orEmpty()
            )
        }

        loginButton.setOnClickListener {
            viewModel.login(
                username = usernameInput.text?.toString().orEmpty(),
                password = passwordInput.text?.toString().orEmpty()
            )
        }
    }

    override fun invalidate() = withState(viewModel) { loginState ->
        loginButton.isEnabled = loginState.canLogin && loginState.uuid !is Loading
        loginButton.isVisible = loginState.uuid !is Loading
        loading.isVisible = loginState.uuid is Loading

        // Login successful
        if (loginState.uuid.invoke() != null) {
            Toast.makeText(requireContext(), R.string.welcome, Toast.LENGTH_SHORT).show()
            viewModel.reset()
        }
    }
}
