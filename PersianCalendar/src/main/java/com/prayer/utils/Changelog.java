/*
 * Copyright (c) 2016 Metin Kale
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.prayer.utils;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.widget.Toast;

import com.byagowi.persiancalendar.R;
import com.prayer.settings.Prefs;
import com.prayer.settings.Settings;

public class Changelog {
    private static final int CHANGELOG_VERSION = 16;
    private static Context mContext;

    public static void start(Context c) {
        mContext = c;
        if (Prefs.getChangelogVersion() < CHANGELOG_VERSION) {
            Prefs.setChangelogVersion(CHANGELOG_VERSION);
        }
    }
}
