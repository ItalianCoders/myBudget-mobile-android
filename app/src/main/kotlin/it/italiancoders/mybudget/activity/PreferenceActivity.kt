package it.italiancoders.mybudget.activity

import android.os.Bundle
import android.preference.PreferenceActivity
import it.italiancoders.mybudget.fragment.preferences.PreferencesFragment_
import org.androidannotations.annotations.EActivity

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/11/17
 */
@EActivity
open class PreferenceActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, PreferencesFragment_.builder().build()).commit()
    }
}