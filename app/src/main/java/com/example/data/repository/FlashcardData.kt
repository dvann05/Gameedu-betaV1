package com.example.data.repository

import androidx.compose.ui.graphics.Color
import com.example.data.model.FlashcardItem
import com.example.data.model.LearningCategory

object FlashcardData {

    fun getItemsForCategory(category: LearningCategory): List<FlashcardItem> {
        return when (category) {
            LearningCategory.LETTERS -> ( 'A'..'Z' ).map { char ->
                FlashcardItem(
                    id = "letter_$char",
                    nameEn = "Letter $char",
                    nameId = "Huruf $char",
                    nameAr = "حرف $char",
                    nameJa = "文字 $char",
                    emojiOrSymbol = char.toString(),
                    cardColor = Color(0xFFFF8A65)
                )
            }
            LearningCategory.NUMBERS -> (1..20).map { num ->
                FlashcardItem(
                    id = "num_$num",
                    nameEn = "Number $num",
                    nameId = "Angka $num",
                    nameAr = "رقم $num",
                    nameJa = "数字 $num",
                    emojiOrSymbol = num.toString(),
                    cardColor = Color(0xFF64B5F6)
                )
            }
            LearningCategory.COLORS -> listOf(
                FlashcardItem("c1", "Red", "Merah", "أحمر", "赤", "🔴", cardColor = Color(0xFFEF5350)),
                FlashcardItem("c2", "Blue", "Biru", "أزرق", "青", "🔵", cardColor = Color(0xFF42A5F5)),
                FlashcardItem("c3", "Yellow", "Kuning", "أصفر", "黄色", "🟡", cardColor = Color(0xFFFFCA28)),
                FlashcardItem("c4", "Green", "Hijau", "أخضر", "緑", "🟢", cardColor = Color(0xFF66BB6A)),
                FlashcardItem("c5", "Purple", "Ungu", "أرجواني", "紫", "🟣", cardColor = Color(0xFFAB47BC)),
                FlashcardItem("c6", "Orange", "Oranye", "برتقالي", "オレンジ", "🟧", cardColor = Color(0xFFFF7043)),
                FlashcardItem("c7", "Pink", "Merah Muda", "وردي", "ピンク", "🌸", cardColor = Color(0xFFEC407A)),
                FlashcardItem("c8", "Brown", "Cokelat", "بني", "茶色", "🤎", cardColor = Color(0xFF8D6E63))
            )
            LearningCategory.SHAPES -> listOf(
                FlashcardItem("s1", "Circle", "Lingkaran", "دائرة", "円", "🔴"),
                FlashcardItem("s2", "Square", "Persegi", "مربع", "正方形", "🟦"),
                FlashcardItem("s3", "Triangle", "Segitiga", "مثلث", "三角形", "🔺"),
                FlashcardItem("s4", "Star", "Bintang", "نجمة", "星", "⭐"),
                FlashcardItem("s5", "Heart", "Hati", "قلب", "ハート", "❤️"),
                FlashcardItem("s6", "Diamond", "Belah Ketupat", "ماس", "ひし形", "💎")
            )
            LearningCategory.FRUITS -> listOf(
                FlashcardItem("f1", "Apple", "Apel", "تفاح", "りんご", "🍎"),
                FlashcardItem("f2", "Banana", "Pisang", "موز", "バナナ", "🍌"),
                FlashcardItem("f3", "Orange", "Jeruk", "برتقال", "オレンジ", "🍊"),
                FlashcardItem("f4", "Strawberry", "Stroberi", "فراولة", "いちご", "🍓"),
                FlashcardItem("f5", "Grape", "Anggur", "عنب", "ぶどう", "🍇"),
                FlashcardItem("f6", "Watermelon", "Semangka", "بطيخ", "すいか", "🍉"),
                FlashcardItem("f7", "Pineapple", "Nanas", "أناناس", "パイナップル", "🍍")
            )
            LearningCategory.VEGETABLES -> listOf(
                FlashcardItem("v1", "Carrot", "Wortel", "جزر", "にんじん", "🥕"),
                FlashcardItem("v2", "Tomato", "Tomat", "طماطم", "トマト", "🍅"),
                FlashcardItem("v3", "Broccoli", "Brokoli", "بروكلي", "ブロッコリー", "🥦"),
                FlashcardItem("v4", "Corn", "Jagung", "ذرة", "とうもろこし", "🌽"),
                FlashcardItem("v5", "Eggplant", "Terong", "باذنجان", "なす", "🍆"),
                FlashcardItem("v6", "Cucumber", "Mentimun", "خيار", "きゅうり", "🥒")
            )
            LearningCategory.ANIMALS -> listOf(
                FlashcardItem("a1", "Elephant", "Gajah", "فيل", "ぞう", "🐘"),
                FlashcardItem("a2", "Lion", "Singa", "أسد", "ライオン", "🦁"),
                FlashcardItem("a3", "Tiger", "Harimau", "نمر", "トラ", "🐯"),
                FlashcardItem("a4", "Cat", "Kucing", "قطة", "ねこ", "🐱"),
                FlashcardItem("a5", "Dog", "Anjing", "كلب", "いぬ", "🐶"),
                FlashcardItem("a6", "Panda", "Panda", "باندا", "パンダ", "🐼"),
                FlashcardItem("a7", "Bear", "Beruang", "دب", "くま", "🐻")
            )
            LearningCategory.INSECTS -> listOf(
                FlashcardItem("i1", "Butterfly", "Kupu-kupu", "فراشة", "ちょう", "🦋"),
                FlashcardItem("i2", "Bee", "Lebah", "نحلة", "みつばち", "🐝"),
                FlashcardItem("i3", "Ladybug", "Kumbang", "دعسوقة", "てんとう虫", "🐞"),
                FlashcardItem("i4", "Ant", "Semut", "نملة", "あり", "🐜")
            )
            LearningCategory.BIRDS -> listOf(
                FlashcardItem("b1", "Owl", "Burung Hantu", "بومة", "フクロウ", "🦉"),
                FlashcardItem("b2", "Eagle", "Elang", "نسر", "ワシ", "🦅"),
                FlashcardItem("b3", "Parrot", "Kakaktua", "ببغاء", "オウム", "🦜"),
                FlashcardItem("b4", "Penguin", "Penguin", "بطريق", "ペンギン", "🐧"),
                FlashcardItem("b5", "Flamingo", "Flamingo", "نحام", "フラミンゴ", "🦩")
            )
            LearningCategory.SEA -> listOf(
                FlashcardItem("sea1", "Dolphin", "Lumba-lumba", "دلفين", "イルカ", "🐬"),
                FlashcardItem("sea2", "Shark", "Ikan Hiu", "قرش", "サメ", "🦈"),
                FlashcardItem("sea3", "Whale", "Ikan Paus", "حوت", "クジラ", "🐳"),
                FlashcardItem("sea4", "Octopus", "Gurita", "أخطبوط", "タコ", "🐙"),
                FlashcardItem("sea5", "Crab", "Kepiting", "سلطعون", "カニ", "🦀")
            )
            LearningCategory.PLANETS -> listOf(
                FlashcardItem("p1", "Earth", "Bumi", "الأرض", "地球", "🌍"),
                FlashcardItem("p2", "Mars", "Mars", "المريخ", "火星", "🔴"),
                FlashcardItem("p3", "Jupiter", "Yupiter", "المشتري", "木星", "🪐"),
                FlashcardItem("p4", "Saturn", "Saturnus", "زحل", "土星", "🪐")
            )
            LearningCategory.SOLAR -> listOf(
                FlashcardItem("sol1", "Sun", "Matahari", "شمس", "太陽", "☀️"),
                FlashcardItem("sol2", "Moon", "Bulan", "قمر", "月", "🌙"),
                FlashcardItem("sol3", "Astronaut", "Astronot", "رائد فضاء", "宇宙飛行士", "👨‍🚀")
            )
            LearningCategory.COUNTRIES -> listOf(
                FlashcardItem("cnt1", "Indonesia", "Indonesia", "إندونيسيا", "インドネシア", "🇮🇩"),
                FlashcardItem("cnt2", "USA", "Amerika Serikat", "أمريكا", "アメリカ", "🇺🇸"),
                FlashcardItem("cnt3", "Japan", "Jepang", "اليابان", "日本", "🇯🇵"),
                FlashcardItem("cnt4", "Saudi Arabia", "Arab Saudi", "السعودية", "サウジアラビア", "🇸🇦"),
                FlashcardItem("cnt5", "Korea", "Korea Selatan", "كوريا", "韓国", "🇰🇷")
            )
            LearningCategory.FLAGS -> listOf(
                FlashcardItem("flg1", "Indonesia Flag", "Bendera Indonesia", "علم إندونيسيا", "インドネシア国旗", "🇮🇩"),
                FlashcardItem("flg2", "USA Flag", "Bendera Amerika", "علم أمريكا", "米国国旗", "🇺🇸"),
                FlashcardItem("flg3", "Japan Flag", "Bendera Jepang", "علم اليابان", "日本国旗", "🇯🇵")
            )
            LearningCategory.PROFESSIONS -> listOf(
                FlashcardItem("prof1", "Doctor", "Dokter", "طبيب", "医者", "👨‍⚕️"),
                FlashcardItem("prof2", "Police", "Polisi", "شرطي", "警察官", "👮"),
                FlashcardItem("prof3", "Firefighter", "Pemadam Kebakaran", "إطفائي", "消防士", "👨‍🚒"),
                FlashcardItem("prof4", "Teacher", "Guru", "معلم", "教師", "👩‍🏫")
            )
            LearningCategory.VEHICLES -> listOf(
                FlashcardItem("veh1", "Car", "Mobil", "سيارة", "車", "🚗"),
                FlashcardItem("veh2", "Bus", "Bus", "حافلة", "バス", "🚌"),
                FlashcardItem("veh3", "Train", "Kereta Api", "قطار", "電車", "🚆"),
                FlashcardItem("veh4", "Airplane", "Pesawat", "طائرة", "飛行機", "✈️")
            )
            LearningCategory.BODY -> listOf(
                FlashcardItem("body1", "Eye", "Mata", "عين", "目", "👁️"),
                FlashcardItem("body2", "Ear", "Telinga", "أذن", "耳", "👂"),
                FlashcardItem("body3", "Hand", "Tangan", "يد", "手", "🖐️"),
                FlashcardItem("body4", "Foot", "Kaki", "قدم", "足", "🦶")
            )
            LearningCategory.FOOD -> listOf(
                FlashcardItem("food1", "Pizza", "Pizza", "بيتزا", "ピザ", "🍕"),
                FlashcardItem("food2", "Burger", "Burger", "برجر", "ハンバーガー", "🍔"),
                FlashcardItem("food3", "Rice", "Nasi", "أرز", "ご飯", "🍚")
            )
            LearningCategory.DRINKS -> listOf(
                FlashcardItem("drk1", "Milk", "Susu", "حليب", "牛乳", "🥛"),
                FlashcardItem("drk2", "Orange Juice", "Jus Jeruk", "عصير برتقال", "オレンジジュース", "🧃"),
                FlashcardItem("drk3", "Water", "Air Putih", "ماء", "水", "💧")
            )
        }
    }
}
