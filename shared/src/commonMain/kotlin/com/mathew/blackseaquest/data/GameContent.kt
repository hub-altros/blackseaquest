package com.mathew.blackseaquest.data

import androidx.compose.ui.geometry.Offset
import com.mathew.blackseaquest.model.AnswerOption
import com.mathew.blackseaquest.model.Station
import com.mathew.blackseaquest.model.StationType

private fun answers(correct: String, vararg wrong: String): List<AnswerOption> =
    listOf(AnswerOption(correct, true)) + wrong.map { AnswerOption(it, false) }

object GameContent {
    const val LAST_INDEX = 19

    val stations: List<Station> = listOf(
        Station(0, StationType.START, "Старт: отряд волонтёров", question = "Кто такие волонтёры?", options = answers("Люди, которые бесплатно помогают другим и природе", "Люди, которым платят за уборку пляжа", "Только врачи и спасатели"), explanation = "Волонтёр помогает по своей воле и без денег."),
        Station(1, StationType.QUESTION, "Помощь морю", question = "Чем волонтёры помогают Чёрному морю?", options = answers("Убирают мусор, спасают птиц и животных, учат людей беречь природу", "Ловят всю рыбу", "Купаются в море"), explanation = "Главная сила волонтёра — забота и пример для других."),
        Station(2, StationType.PENALTY, "Пластик на гальке", penaltyText = "Ты оставил пластиковый стаканчик на гальке. Ветер унёс его в море — пропусти ход."),
        Station(3, StationType.QUESTION, "Страны у моря", question = "Сколько стран имеют выход к Чёрному морю?", options = answers("6", "3", "12"), explanation = "Россия, Украина, Румыния, Болгария, Турция и Грузия."),
        Station(4, StationType.QUESTION, "Глубина", question = "Что находится в глубине Чёрного моря?", options = answers("Сероводород — там почти нет жизни", "Коралловые рифы", "Затонувшие города"), explanation = "Глубже 150–200 м в воде растворён сероводород, и живые организмы там не живут."),
        Station(5, StationType.QUESTION, "Дельфины", question = "Какие дельфины живут в Чёрном море?", options = answers("Афалина, белобочка и азовка", "Косатка и белуха", "Только афалина"), explanation = "В Чёрном море живут три вида дельфинов — все они охраняются."),
        Station(6, StationType.PENALTY, "Пакет в море", penaltyText = "Ты бросил в море пластиковый пакет. Дельфин принял его за медузу — пропусти ход."),
        Station(7, StationType.QUESTION, "Сколько живёт мусор", question = "Сколько лет разлагается пластиковая бутылка?", options = answers("Около 450 лет", "5 лет", "50 лет"), explanation = "Пластик почти не разлагается — поэтому его так важно сдавать в переработку."),
        Station(8, StationType.BONUS, "Помощь волонтёру", penaltyText = "Ты помог волонтёру мыть птицу от мазута — бросай кубик ещё раз!"),
        Station(9, StationType.QUESTION, "Птица в мазуте", question = "Ты нашёл птицу, испачканную мазутом. Что делать?", options = answers("Позвать взрослых и специалистов, не трогать без перчаток", "Искупать её в море", "Покормить хлебом"), explanation = "Мазут ядовит. Птицу моют специальным раствором в защитных перчатках."),
        Station(10, StationType.QUESTION, "Что такое мазут", question = "Чем опасен мазут?", options = answers("Он ядовит, склеивает перья и губит животных", "Он полезен для рыб", "Он быстро исчезает без следа"), explanation = "Плёнка мазута не пропускает воздух, и морские животные задыхаются."),
        Station(11, StationType.PENALTY, "Опасная зона", penaltyText = "Ты подошёл к разливу мазута без перчаток и защитного костюма — пропусти ход, это опасно!"),
        Station(12, StationType.QUESTION, "Древнее имя", question = "Как древние греки называли Чёрное море?", options = answers("Понт Эвксинский — «Гостеприимное море»", "Маре Нострум", "Океанус"), explanation = "Сначала море звали Понт Аксинский — «Негостеприимное», потом имя сменили."),
        Station(13, StationType.QUESTION, "Город-герой", question = "Какой город на Чёрном море — город-герой?", options = answers("Севастополь", "Казань", "Москва"), explanation = "Севастополь и Новороссийск носят звание «Город-герой»."),
        Station(14, StationType.QUESTION, "Не кормить!", question = "Почему нельзя кормить чаек и дельфинов?", options = answers("Они привыкают и перестают искать еду сами, а хлеб им вреден", "Они станут ручными", "Это запрещено только зимой"), explanation = "Диким животным нужна их обычная еда, а не хлеб с пляжа."),
        Station(15, StationType.BONUS, "Переработка", penaltyText = "Ты сдал батарейки и пластик в переработку — бросай кубик ещё раз!"),
        Station(16, StationType.QUESTION, "После пикника", question = "Что делать с мусором после пикника на пляже?", options = answers("Забрать с собой и выбросить в контейнеры", "Закопать в гальку", "Оставить — его уберут"), explanation = "Закопанный мусор всё равно окажется в море после шторма."),
        Station(17, StationType.QUESTION, "Зимняя птица", question = "Какая птица прилетает на Чёрное море зимой?", options = answers("Гагара", "Пингвин", "Страус"), explanation = "Гагары зимуют у берегов Чёрного моря и часто страдают от разливов нефти."),
        Station(18, StationType.PENALTY, "Громкая музыка", penaltyText = "Ты включил громкую музыку на пляже и распугал птиц — пропусти ход."),
        Station(19, StationType.FINISH, "Финиш!", question = "Ты дошёл до финиша! Кто ты для Чёрного моря?", options = answers("Настоящий друг и волонтёр", "Просто турист", "Случайный прохожий"), explanation = "Ты знаешь, как беречь море, — расскажи об этом друзьям!")
    )

    val stationPositions: List<Offset> = listOf(
        Offset(0.10f, 0.12f), Offset(0.21f, 0.09f), Offset(0.32f, 0.13f), Offset(0.39f, 0.22f),
        Offset(0.43f, 0.33f), Offset(0.40f, 0.45f), Offset(0.34f, 0.56f), Offset(0.26f, 0.66f),
        Offset(0.18f, 0.75f), Offset(0.23f, 0.85f), Offset(0.35f, 0.89f), Offset(0.48f, 0.91f),
        Offset(0.60f, 0.87f), Offset(0.70f, 0.79f), Offset(0.76f, 0.69f), Offset(0.81f, 0.57f),
        Offset(0.83f, 0.45f), Offset(0.81f, 0.33f), Offset(0.75f, 0.23f), Offset(0.66f, 0.14f)
    )
}
