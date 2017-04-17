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

package com.prayer.vakit.sounds;

import android.content.SharedPreferences;
import android.os.Environment;

import com.prayer.App;
import com.prayer.utils.MD5;
import com.prayer.vakit.times.Times;
import com.prayer.vakit.times.other.Vakit;

import java.io.File;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Sounds {
    private static AbstractMap<String, List<Sound>> sSounds = new LinkedHashMap<>();

    public static boolean isDownloaded(Sound sound) {
        return (sound.url == null) || sound.getFile().exists();

    }

    public static Map<String, List<Sound>> getSounds() {

        if (sSounds.isEmpty()) {
            List<Sound> sabah = new ArrayList<>();
            List<Sound> ogle = new ArrayList<>();
            List<Sound> ikindi = new ArrayList<>();
            List<Sound> aksam = new ArrayList<>();
            List<Sound> yatsi = new ArrayList<>();
            List<Sound> sela = new ArrayList<>();
            List<Sound> dua = new ArrayList<>();
            List<Sound> ezan = new ArrayList<>();

            /////
            ezan.add(new Sound("سعید طوسی", App.API_URL_FA + "/voice/azan/moazenin2/01-saeid-tosi.mp3", "1510kB"));
            ezan.add(new Sound("مرحوم محسن حاجی حسنی", App.API_URL_FA + "/voice/azan/moazenin/azan-Mohsen-Haji-Hasani.mp3", "4,796kB"));
            ezan.add(new Sound("محمد رضا پور زرگری", App.API_URL_FA + "/voice/azan/moazenin2/02-porzargari.mp3", "1558kB"));
            ezan.add(new Sound("قاسم رضیعی", App.API_URL_FA + "/voice/azan/moazenin2/03-ghasem-raziei.mp3", "1173kB"));
            ezan.add(new Sound("شهید شیخ ید الله ابراهیمی کچویی", App.API_URL_FA + "/voice/azan/moazenin/yadolla-ebrahimi-kachooei.mp3", "3144kB"));
            ezan.add(new Sound("حجة الاسلام والمسلمين شيخ حميد اسماعيل پور", App.API_URL_FA + "/voice/azan/moazenin/88-sheikh-hamid-esmayil-pur.mp3", "1359kB"));
            ezan.add(new Sound("سلیم موذن زاده ی اردبیلی 1", App.API_URL_FA + "/voice/azan/moazenin/52-salim-moazenzadeh-01.mp3", "1467kB"));
            ezan.add(new Sound("سلیم موذن زاده ی اردبیلی 2", App.API_URL_FA + "/voice/azan/moazenin/53-salim-moazenzadeh-02.mp3", "1957kB"));
            ezan.add(new Sound("مرحوم رحیم مؤذن زاده اردبیلی 1", App.API_URL_FA + "/voice/azan/moazenin/85-rahim-Moazenzadeh-01.mp3", "325kB"));
            ezan.add(new Sound("مرحوم داوود مؤذن زاده اردبیلی", App.API_URL_FA + "/voice/azan/moazenin/86-rahim-Moazenzadeh-02.mp3", "1083kB"));
            ezan.add(new Sound("مرحوم رحیم مؤذن زاده اردبیلی 2", App.API_URL_FA + "/voice/azan/moazenin/87-rahim-Moazenzadeh-03.mp3", "1243kB"));
            ezan.add(new Sound("کریم منصوری", App.API_URL_FA + "/voice/azan/moazenin/47-karim-Mansori.mp3", "957kB"));
            ezan.add(new Sound("راغب مصطفی غلوش", App.API_URL_FA + "/voice/azan/moazenin/82-ghalvash.mp3", "1387kB"));
            ezan.add(new Sound("حسین صبحدل", App.API_URL_FA + "/voice/azan/moazenin/83-sobhdel.mp3", "1068kB"));
            ezan.add(new Sound("مرحوم آقاتی", App.API_URL_FA + "/voice/azan/moazenin/84-aghati.mp3", "1103kB"));
            ezan.add(new Sound("عبدالباسط", App.API_URL_FA + "/voice/azan/moazenin/33-abdolbaset.mp3", "1021kB"));
            ezan.add(new Sound("منشاوی", App.API_URL_FA + "/voice/azan/moazenin/51-menshavi.mp3", "1108kB"));
            ezan.add(new Sound("عبدالنعم طوخی", App.API_URL_FA + "/voice/azan/moazenin/80-tukhi.mp3", "1368kB"));
            ezan.add(new Sound("شعیشع", App.API_URL_FA + "/voice/azan/moazenin/77-shoaysha.mp3", "1173kB"));
            ezan.add(new Sound("شحات انور", App.API_URL_FA + "/voice/azan/moazenin/74-shahhat-anvar.mp3", "229kB"));
            ezan.add(new Sound("نقشبندی", App.API_URL_FA + "/voice/azan/moazenin/58-naghshbandi.mp3", "639kB"));
            ezan.add(new Sound("عبدالفتاح", App.API_URL_FA + "/voice/azan/moazenin/01-abdolfatah.mp3", "876kB"));
            ezan.add(new Sound("عبدالرحیم", App.API_URL_FA + "/voice/azan/moazenin/02-abdolrahim.mp3", "733kB"));
            ezan.add(new Sound("عبدالفتاح طاروتی 1", App.API_URL_FA + "/voice/azan/moazenin/03-abdolfatah-taaruti-01.mp3", "1200kB"));
            ezan.add(new Sound("عبدالفتاح طاروتی 2", App.API_URL_FA + "/voice/azan/moazenin/04-abdolfatah-taaruti-02.mp3", "1095kB"));
            ezan.add(new Sound("عبدالفتاح طاروتی 3", App.API_URL_FA + "/voice/azan/moazenin/05-abdolfatah-taaruti-03.mp3", "1176kB"));
            ezan.add(new Sound("عبدالغفار", App.API_URL_FA + "/voice/azan/moazenin/06-abdolghaffar.mp3", "187kB"));
            ezan.add(new Sound("عبدالحکم", App.API_URL_FA + "/voice/azan/moazenin/07-abdolhakim.mp3", "906kB"));
            ezan.add(new Sound("عبدالعطیف", App.API_URL_FA + "/voice/azan/moazenin/08-abdollatif.mp3", "184kB"));
            ezan.add(new Sound("ابوالحسن دماوندی", App.API_URL_FA + "/voice/azan/moazenin/09-abolhasan-damavandi.mp3", "748kB"));
            ezan.add(new Sound("محمد حسین ابوریه", App.API_URL_FA + "/voice/azan/moazenin/10-aborieh.mp3", "1289kB"));
            ezan.add(new Sound("زاهر", App.API_URL_FA + "/voice/azan/moazenin/11-zaher.mp3", "905kB"));
            ezan.add(new Sound("علی البناء", App.API_URL_FA + "/voice/azan/moazenin/12-alelbana.mp3", "753kB"));
            ezan.add(new Sound("الحصری", App.API_URL_FA + "/voice/azan/moazenin/13-alhasri.mp3", "187kB"));
            ezan.add(new Sound("الحسینی", App.API_URL_FA + "/voice/azan/moazenin/14-alhoseini.mp3", "196kB"));
            ezan.add(new Sound("علی اکبر حنیفی", App.API_URL_FA + "/voice/azan/moazenin/15-ali-akbar-hanifi.mp3", "1373kB"));
            ezan.add(new Sound("علی اربابی", App.API_URL_FA + "/voice/azan/moazenin/16-ali-arbabi.mp3", "1084kB"));
            ezan.add(new Sound("علی محمود", App.API_URL_FA + "/voice/azan/moazenin/17-ali-mohamad.mp3", "733kB"));
            ezan.add(new Sound("علیرضا احمدی", App.API_URL_FA + "/voice/azan/moazenin/18-alireza-ahmadi.mp3", "1175kB"));
            ezan.add(new Sound("الجوهری", App.API_URL_FA + "/voice/azan/moazenin/19-aljohari.mp3", "134kB"));
            ezan.add(new Sound("شیخ محمود خلیل الحصری", App.API_URL_FA + "/voice/azan/moazenin/20-alhosari.mp3", "709kB"));
            ezan.add(new Sound("النبراوی", App.API_URL_FA + "/voice/azan/moazenin/21-alnabravi.mp3", "181kB"));
            ezan.add(new Sound("الزاوی", App.API_URL_FA + "/voice/azan/moazenin/22-alzavi.mp3", "145kB"));
            ezan.add(new Sound("عطاء الله امیدوار", App.API_URL_FA + "/voice/azan/moazenin/23-ataollah-omidvar.mp3", "604kB"));
            ezan.add(new Sound("محمد پور", App.API_URL_FA + "/voice/azan/moazenin/24-mohamadpoor.mp3", "1355kB"));
            ezan.add(new Sound("محمد رضا ابوالقاسمی", App.API_URL_FA + "/voice/azan/moazenin/25-abolghasemi.mp3", "1105kB"));
            ezan.add(new Sound("سید عباس میرداماد", App.API_URL_FA + "/voice/azan/moazenin/26-seyed-abas-mirdamad.mp3", "1267kB"));
            ezan.add(new Sound("سید حسین موسوی بلده", App.API_URL_FA + "/voice/azan/moazenin/27-mosavi-balade.mp3", "1123kB"));
            ezan.add(new Sound(" محمد حسین سبز", App.API_URL_FA + "/voice/azan/moazenin/28-mohamd-hosein-sabz.mp3", "980kB"));
            ezan.add(new Sound(" تاج اصفهانی", App.API_URL_FA + "/voice/azan/moazenin/29-esfehani.mp3", "775kB"));
            ezan.add(new Sound("رضا محمد پور", App.API_URL_FA + "/voice/azan/moazenin/30-mohamad-poor.mp3", "1145kB"));
            ezan.add(new Sound("محسن بادیا", App.API_URL_FA + "/voice/azan/moazenin/31-badia.mp3", "1157kB"));
            ezan.add(new Sound("محمد کاظم نداف", App.API_URL_FA + "/voice/azan/moazenin/32-nadaaf.mp3", "1230kB"));
            ezan.add(new Sound("کامل یوسف", App.API_URL_FA + "/voice/azan/moazenin/34-kamel-yusof.mp3", "898kB"));
            ezan.add(new Sound("الدمنهوری", App.API_URL_FA + "/voice/azan/moazenin/35-damenhuri.mp3", "893kB"));
            ezan.add(new Sound("ابراهیم الشجاعی", App.API_URL_FA + "/voice/azan/moazenin/36-ebrahim-alshojaei.mp3", "195kB"));
            ezan.add(new Sound("عمران", App.API_URL_FA + "/voice/azan/moazenin/37-emran.mp3", "902kB"));
            ezan.add(new Sound("طه ألفَشنی", App.API_URL_FA + "/voice/azan/moazenin/38-fashani.mp3", "1129kB"));
            ezan.add(new Sound("محمد قندیل", App.API_URL_FA + "/voice/azan/moazenin/39-ghandil.mp3", "229kB"));
            ezan.add(new Sound("سعید حافظ", App.API_URL_FA + "/voice/azan/moazenin/40-hafez.mp3", "167kB"));
            ezan.add(new Sound("حسین فردی", App.API_URL_FA + "/voice/azan/moazenin/41-hosein-fardi.mp3", "998kB"));
            ezan.add(new Sound("حسن خانچی", App.API_URL_FA + "/voice/azan/moazenin/42-hosein-khanchi.mp3", "1257kB"));
            ezan.add(new Sound("حسین ربیعیان", App.API_URL_FA + "/voice/azan/moazenin/43-hosein-rabiyian.mp3", "1283kB"));
            ezan.add(new Sound("حسین رستمی", App.API_URL_FA + "/voice/azan/moazenin/44-hosein-rostami.mp3", "1161kB"));
            ezan.add(new Sound("حسین یزدان پناه", App.API_URL_FA + "/voice/azan/moazenin/45-hosein-yazdan-panah.mp3", "1205kB"));
            ezan.add(new Sound("کرمی", App.API_URL_FA + "/voice/azan/moazenin/46-karami.mp3", "1443kB"));
            ezan.add(new Sound("سلمان خلیل", App.API_URL_FA + "/voice/azan/moazenin/48-khalil.mp3", "1055kB"));

            ezan.add(new Sound("اذان مدینه", App.API_URL_FA + "/voice/azan/azan-haram/01-madineh.mp3", "845kB"));
            ezan.add(new Sound("اذان مسجد الحرام", App.API_URL_FA + "/voice/azan/azan-haram/02-masjedolharam.mp3", "1747kB"));
            ezan.add(new Sound("حرم امام رضا (ع)", App.API_URL_FA + "/voice/azan/azan-haram/03-Haram-emam-reza.mp3", "1228kB"));
            ezan.add(new Sound("اذان حزین", App.API_URL_FA + "/voice/azan/other/02-hazin.mp3", "1442kB"));
            ezan.add(new Sound("اذان انتظار - کاظم زاده", App.API_URL_FA + "/voice/azan/other/01-entezar.mp3", "1281kB"));


            dua.add(new Sound("Abdulkadir Sehitoglu - Ezan Duasi", App.API_URL + "/sounds/dua/Abdulkadir%20Sehitoglu%20-%20Ezan%20Duasi.mp3", "1098kB"));



            sSounds.put("sabah", sabah);
            sSounds.put("ogle", ogle);
            sSounds.put("ikindi", ikindi);
            sSounds.put("aksam", aksam);
            sSounds.put("yatsi", yatsi);
            sSounds.put("sela", sela);
            sSounds.put("dua", dua);
            sSounds.put("ezan", ezan);


        }

        return sSounds;
    }

    public static List<Sound> getSounds(Vakit vakit) {
        if (vakit == Vakit.IMSAK) {
            vakit = Vakit.SABAH;
        }
        if (vakit == Vakit.GUNES) {
            vakit = Vakit.SABAH;
        }
        if (vakit == null) {
            return getSounds("extra");
        }
        return getSounds(vakit.name().toLowerCase(Locale.GERMAN), "ezan", "extra");
    }

    public static List<Sound> getSounds(String... categories) {
        List<Sound> sounds = new ArrayList<>();
        for (String cat : categories) {
            if (getSounds().containsKey(cat)) {
                sounds.addAll(getSounds().get(cat));
            }
        }


        return sounds;
    }

    public static List<Sound> getAllSounds() {
        List<Sound> sounds = new ArrayList<>();
        Set<String> set = getSounds().keySet();
        for (String cat : set) {
            if (getSounds().containsKey(cat)) {
                sounds.addAll(getSounds().get(cat));
            }
        }


        return sounds;
    }

    private static String forAlarm(Times.Alarm alarm) {
        Times t = Times.getTimes(alarm.time);
        String sound;
        if (alarm.cuma) {
            sound = t.getCumaSound();
        } else if (alarm.early) {
            sound = t.getEarlySound(alarm.vakit);
        } else {
            sound = t.getSound(alarm.vakit);
        }
        return sound;
    }


    public static class Sound implements Serializable {
        public String name;
        public String uri;
        public String url;
        public String size;

        public Sound() {
        }

        public Sound(String name, String url, String size) {
            this.name = name;
            this.url = url;
            this.size = size;
            uri = getFile().toURI().toString();
        }

        public File getFile() {
            File old = new File(url.replace(App.API_URL_FA + "/voice/", App.getContext().getExternalFilesDir(null).getAbsolutePath()));
            if (old.exists()) {
                return old;
            }

            File def = new File(url.replace(App.API_URL_FA + "/voice", App.getContext().getExternalFilesDir(null).getAbsolutePath()));
            if (def.exists()) {
                return def;
            }

            File nosd = new File(url.replace(App.API_URL_FA + "/voice", App.getContext().getFilesDir().getAbsolutePath()));
            if (nosd.exists()) {
                return nosd;
            }

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return def;
            } else {
                return nosd;
            }
        }

        public void checkMD5() {
            File file = getFile();
            if (file.exists()) {
                SharedPreferences preferences = App.getContext().getSharedPreferences("md5", 0);
                String md5 = preferences.getString(name, null);

                if (md5 != null && !MD5.checkMD5(md5, file)) {
                    file.delete();
                }
            }
        }


        public boolean equals(Object o) {
            if (o instanceof Sound) {
                return uri.equals(((Sound) o).uri);
            } else {
                return uri.equals(o.toString());
            }
        }
    }


}
