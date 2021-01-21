package pl.edu.pb.runnerapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login)
        editPassword(loginPasswordText, passwordHint)

        loginChangeLanguage.setOnClickListener {
            changeLanguage()
        }


        loginConfirmButton.setOnClickListener {
            var password: String = loginPasswordText.text.toString().trim()
            var email: String = loginLoginText.text.toString().trim()
            checkUser(email, password)
        }

        loginSignUp.setOnClickListener {
            val signInActivity = Intent(this, SignInActivity::class.java)
            startActivity(signInActivity)
            finish()
        }
    }

    fun checkUser(userEmail: String, userPassword: String) {
        if (userEmail.length <= 5 || userPassword.length < 6) {
            return
        }
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    makeText(applicationContext, "Logged in", LENGTH_LONG)
                        .show()
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    finish()
                } else {
                    makeText(
                        applicationContext,
                        "Error !" + task.exception!!.message,
                        LENGTH_LONG
                    )
                        .show()
                }
            })
    }

    fun changeLanguage() {
        var currentLocale = getResources().getConfiguration().locale;
        Log.d("blond", currentLocale.toString())
        if (currentLocale.toString().equals("pl_PL")) {
            var locale = Locale("en", "US");
            var res = getResources();
            var dm = res.getDisplayMetrics();
            var conf = res.getConfiguration();
            conf.locale = locale;
            res.updateConfiguration(conf, dm);
        } else {
            var locale = Locale("pl", "PL");
            var res = getResources();
            var dm = res.getDisplayMetrics();
            var conf = res.getConfiguration();
            conf.locale = locale;
            res.updateConfiguration(conf, dm);
        }
        val refresh = Intent(this, LoginActivity::class.java)
        startActivity(refresh)
        this.finish()
    }
}

