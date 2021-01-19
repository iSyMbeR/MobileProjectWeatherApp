package pl.edu.pb.runnerapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registery.*
import java.util.*

class LoginActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login)
        editEmail(loginLoginText, loginHint)
        editPassword(loginPasswordText, passwordHint)
        loginChangeLanguage.setOnClickListener {
            changeLanguage()
        }


        loginConfirmButton.setOnClickListener {
            var password: String = loginLoginText.text.toString()
            var email: String = loginPasswordText.text.toString()
                checkUser(email, password)

        }

        loginSignUp.setOnClickListener {
            val signInActivity = Intent(this, SignInActivity::class.java)
            startActivity(signInActivity)
            finish()
        }
    }

    fun checkUser(userEmail: String, userPassword: String) {
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "User created", Toast.LENGTH_LONG)
                        .show()
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Error !" + task.exception!!.message,
                        Toast.LENGTH_LONG
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
        val refresh = Intent(this, SignInActivity::class.java)
        startActivity(refresh)
        this.finish()
    }
}

