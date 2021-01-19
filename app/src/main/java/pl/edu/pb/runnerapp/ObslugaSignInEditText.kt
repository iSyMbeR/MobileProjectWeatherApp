package pl.edu.pb.runnerapp

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

fun editPassword(password: EditText, warnPassword: TextView){

    password.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if(password.length() == 0) warnPassword.visibility = TextView.INVISIBLE
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(password.length() <= 6){
                warnPassword.setText("Password too short")
                warnPassword.visibility = TextView.VISIBLE
            } else {
                warnPassword.setText("Password is gud")
                warnPassword.visibility = TextView.VISIBLE
            }
        }
    })
}

fun editEmail(email: EditText, warnEmail: TextView){
    email.addTextChangedListener(object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            if(email.length() == 0) warnEmail.visibility = TextView.INVISIBLE
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            warnEmail.setText("Incorrect email")
            for(i in email.text){
                if (i  == '@'){
                    warnEmail.setText("Correctly mail")
                    warnEmail.visibility = TextView.VISIBLE
                }
            }
            warnEmail.visibility=TextView.VISIBLE
        }
    })
}