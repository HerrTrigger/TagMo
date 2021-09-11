package com.hiddenramblings.tagmo;

import android.annotation.SuppressLint;

import com.hiddenramblings.tagmo.settings.SettingsFragment;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SuppressLint("NonConstantResourceId")
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Preferences {
    String query();

    @DefaultInt(BrowserActivity.SORT_NAME)
    int sort();

    String filterGameSeries();

    String filterCharacter();

    String filterAmiiboSeries();

    String filterAmiiboType();

    @DefaultBoolean(keyRes = R.string.settings_enable_tag_type_validation, value = true)
    boolean enableTagTypeValidation();

    @DefaultBoolean(keyRes = R.string.settings_enable_power_tag_support, value = false)
    boolean enablePowerTagSupport();

    @DefaultInt(BrowserActivity.VIEW_TYPE_COMPACT)
    int browserAmiiboView();

    @DefaultString(keyRes = R.string.image_network_settings, value = SettingsFragment.IMAGE_NETWORK_ALWAYS)
    String imageNetworkSetting();

    String browserRootFolder();

    @DefaultBoolean(true)
    boolean recursiveFolders();

    @DefaultBoolean(false)
    boolean showMissingFiles();

    @DefaultBoolean(keyRes = R.string.settings_disable_debug, value = false)
    boolean disableDebug();

    @DefaultBoolean(keyRes = R.string.settings_ignore_sdcard, value = false)
    boolean ignoreSdcard();
}
