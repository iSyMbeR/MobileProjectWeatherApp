package pl.edu.pb.runnerapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registery.*
import java.util.*


class SignInActivity : Activity() {
    lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_registery)

        editEmail(registerEmailText, registerWarnEmail)
        editPassword(registeryPasswordText, registerWarnPassword)

        registeryConfirmButton.setOnClickListener {
            submitData()
        }
        signInChangeLanguage.setOnClickListener {
            changeLanguage()
        }
        signInLoginButton.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }

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

    fun submitData() {
        if (registerEmailText.text.length < 5 || registeryPasswordText.text.length < 6) {
            return
        }
        if (!signInAcceptRules.isChecked) {
            Toast.makeText(applicationContext, "Please accept rules", Toast.LENGTH_SHORT)
                .show()
        } else {

            var login = registerWarnLogin.text.toString()
            var password:String = registeryPasswordText.text.toString()
            var email:String = registerEmailText.text.toString()
            var sex =

                findViewById<RadioButton>(registerChooseSexRadioGroup.checkedRadioButtonId).text
            var newsletter: String

            saveUser(email, password)

            if (signInNewsletter.isChecked) newsletter = "YES"
            else newsletter = "NO"

//            val intent = Intent(applicationContext, ProfileActivity::class.java)
//            intent.putExtra("login", registeryLoginText.text)
//            intent.putExtra("password", registeryPasswordText.text)
//            intent.putExtra("email", registerEmailText.text)
//            intent.putExtra(
//                "sex",
//                findViewById<RadioButton>(registerChooseSexRadioGroup.checkedRadioButtonId).text
//            )
//            if (signInNewsletter.isChecked) intent.putExtra("newsletter", "YES")
//            else intent.putExtra("newsletter", "NO")
//            startActivity(intent)
        }
    }

    fun saveUser(userEmail: String, userPassword: String) {
        fAuth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "User created", Toast.LENGTH_LONG)
                        .show()
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Erroer !" + task.exception!!.message,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
    }
}