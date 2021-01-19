package pl.edu.pb.runnerapp

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileButtonWeather.setOnClickListener {
            //nowa aktywnosc odpalajacaa weatherAcitivity
            var intent: Intent = Intent(applicationContext, WeatherActivity::class.java)
            startActivity(intent)
        }
        profileYoutubeButton.setOnClickListener {
            var address = "https://www.youtube.com/c/MajorSuchodolski"
            var youtubeChannel = Intent(ACTION_VIEW, Uri.parse(address))
            startActivity(youtubeChannel)
        }
        profileChangeLanguage.setOnClickListener {
            changeLanguage()
        }
        //intent zwraca intencje która wywołała daną aktywnosc
        if(intent.hasExtra("sex")){
            profileUserSex.setText(intent.getCharSequenceExtra("sex"))
            profileInfo.setText("Hello " + intent.getCharSequenceExtra("login"))
        }
        profileLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            startActivity(Intent(applicationContext,LoginActivity::class.java))
            finish()
        }
    }

    fun changeLanguage(){
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
        val refresh = Intent(this, ProfileActivity::class.java)
        startActivity(refresh)
        this.finish()
    }

}