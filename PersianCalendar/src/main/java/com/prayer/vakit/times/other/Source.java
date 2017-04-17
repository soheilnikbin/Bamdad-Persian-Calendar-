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

package com.prayer.vakit.times.other;

import com.byagowi.persiancalendar.R;;
import com.prayer.App;
import com.prayer.settings.Prefs;

import java.util.Locale;

/**
 * Created by metin on 03.04.2016.
 */
public enum Source {
    Calc(R.string.calculated, 0), Diyanet("Diyanet.gov.tr", R.drawable.ic_action_on), Fazilet("FaziletTakvimi.com", R.drawable.ic_action_on), IGMG("IGMG.org", R.drawable.ic_action_on), Semerkand("SemerkandTakvimi.com", R.drawable.ic_semerkand), NVC("NamazVakti.com", R.drawable.ic_action_on);

    public int resId;
    public String text;

    Source(String text, int resId) {
        this.text = text;
        this.resId = resId;
    }

    Source(int resTxt, int resIcon) {
        Locale.setDefault(new Locale(Prefs.getLanguage()));
        this.text = App.getContext().getString(resTxt);
        this.resId = resIcon;
    }
}