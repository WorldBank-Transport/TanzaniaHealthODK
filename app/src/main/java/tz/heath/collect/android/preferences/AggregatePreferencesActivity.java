/*
 * Copyright (C) 2014 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package tz.heath.collect.android.preferences;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import tz.health.collect.android.R;
import tz.heath.collect.android.application.Collect;
import tz.heath.collect.android.utilities.UrlUtils;
import tz.heath.collect.android.utilities.WebUtils;

/**
 * Handles aggregate specific preferences.
 * 
 * @author Carl Hartung (chartung@nafundi.com)
 */
public class AggregatePreferencesActivity extends PreferenceActivity {

	protected EditTextPreference mServerUrlPreference;
	protected EditTextPreference mUsernamePreference;
	protected EditTextPreference mPasswordPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.aggregate_preferences);

		mServerUrlPreference = (EditTextPreference) findPreference(PreferencesActivity.KEY_SERVER_URL);
		mUsernamePreference = (EditTextPreference) findPreference(PreferencesActivity.KEY_USERNAME);
		mPasswordPreference = (EditTextPreference) findPreference(PreferencesActivity.KEY_PASSWORD);

		PreferenceCategory aggregatePreferences = (PreferenceCategory) findPreference(getString(R.string.aggregate_preferences));

		mServerUrlPreference
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						String url = newValue.toString();

						// remove all trailing "/"s
						while (url.endsWith("/")) {
							url = url.substring(0, url.length() - 1);
						}

						if (UrlUtils.isValidUrl(url)) {
							preference.setSummary(newValue.toString());
							return true;
						} else {
							Toast.makeText(getApplicationContext(),
									R.string.url_error, Toast.LENGTH_SHORT)
									.show();
							return false;
						}
					}
				});
		mServerUrlPreference.setSummary(mServerUrlPreference.getText());
		mServerUrlPreference.getEditText().setFilters(
				new InputFilter[] { getReturnFilter() });

		mUsernamePreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((CharSequence) newValue);

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String server = settings.getString(PreferencesActivity.KEY_SERVER_URL, getString(R.string.default_server_url));
                Uri u = Uri.parse(server);
                WebUtils.clearHostCredentials(u.getHost());
                Collect.getInstance().getCookieStore().clear();

                return true;
            }
        });
		mUsernamePreference.setSummary(mUsernamePreference.getText());
		mUsernamePreference.getEditText().setFilters(
				new InputFilter[] { getReturnFilter() });

		mPasswordPreference
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						String pw = newValue.toString();

						if (pw.length() > 0) {
							mPasswordPreference.setSummary("********");
						} else {
							mPasswordPreference.setSummary("");
						}

		                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		                String server = settings.getString(PreferencesActivity.KEY_SERVER_URL, getString(R.string.default_server_url));
		                Uri u = Uri.parse(server);
		                WebUtils.clearHostCredentials(u.getHost());
		                Collect.getInstance().getCookieStore().clear();

						return true;
					}
				});
		if (mPasswordPreference.getText() != null
				&& mPasswordPreference.getText().length() > 0) {
			mPasswordPreference.setSummary("********");
		}
		mPasswordPreference.getEditText().setFilters(
				new InputFilter[] { getReturnFilter() });
	}

	/**
	 * Disallows carriage returns from user entry
	 * 
	 * @return
	 */
	protected InputFilter getReturnFilter() {
		InputFilter returnFilter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (Character.getType((source.charAt(i))) == Character.CONTROL) {
						return "";
					}
				}
				return null;
			}
		};
		return returnFilter;
	}

	protected InputFilter getWhitespaceFilter() {
		InputFilter whitespaceFilter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (Character.isWhitespace(source.charAt(i))) {
						return "";
					}
				}
				return null;
			}
		};
		return whitespaceFilter;
	}

}
