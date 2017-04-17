package com.prayer.vakit.times.other;

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

public enum Method {

    MWL("جهان اسلام", "اروپا، شرق دور، بخش هایی از آمریکا",
            new double[]{18, 1, 0, 0, 17}),
    ISNA("انجمن اسلامی شمال امریکا", "شمال امریکا (ایالات متحده و کانادا)",
            new double[]{15, 1, 0, 0, 15}),
    Egypt("اداره کل بررسی و نظرسنجی مصر", "آفریقا، سوریه، لبنان، مالزی",
            new double[]{19.5, 1, 0, 0, 17.5}),
    Makkah("دانشگاه ام القری، مکه", "شبه جزیره عربی",
            new double[]{18.5, 1, 0, 1, 90}),
    Karachi("دانشگاه علوم اسلامی، کراچی", "پاکستان، افغانستان، بنگلادش، هند",
            new double[]{18, 1, 0, 0, 18}),
    Tehran("موسسه ژئوفیزیک، دانشگاه تهران", "ایران، برخی از جوامع شیعه",
            new double[]{17.7, 0, 4.5, 0, 14}),
    Jafari("شیعه اثنی عشری، موسسه تحقیقات لوا، قم", "برخی از جوامع شیعه در سراسر جهان",
            new double[]{16, 0, 4, 0, 14}),
    Custom("سفارشی", "پارامترهای محاسبه سفارشی",
            new double[]{0, 0, 0, 0, 0});
    public String title;
    public String desc;
    /*
     * ==Calc Method Parameters:
     * fa : fajr angle
     * ms : maghrib selector (0 = angle; 1 = minutes after sunset)
     * mv : maghrib parameter value (in angle or minutes)
     * is : isha selector (0 = angle; 1 = minutes after maghrib)
     * iv : isha parameter value (in angle or minutes)
     */
    public double[] params;
    double[] offsets = {0, 0, 0, 0, 0, 0, 0, 0};

    Method(String title, String desc, double[] params) {
        this.params = params;
        this.title = title;
        this.desc = desc;
    }

}
