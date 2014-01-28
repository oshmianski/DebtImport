package by.oshmianski.objects.addressParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by vintselovich on 18.01.14.
 */
public class AddressParserHelper {
    private AddressParserHelper() {
    }

    public static Set<String> regions = new HashSet<String>();
    public static Set<String> districts = new HashSet<String>();
    public static Set<String> cityFirst = new HashSet<String>();
    public static ArrayList<AliasValueType> aliasValues = new ArrayList<AliasValueType>();

    static {
        regions.add("брестская");
        regions.add("гродненская");
        regions.add("гомельская");
        regions.add("витебская");
        regions.add("минская");
        regions.add("могилевская");

        String[] districtsStr = {
                "барановичский", "белыничский", "березинский", "березовский", "берестовицкий", "бешенковичский",
                "бобруйский", "борисовский", "брагинский", "браславский", "брестский", "буда-кошелевский", "быховский",
                "верхнедвинский", "ветковский", "вилейский", "витебский", "волковысский", "воложинский", "вороновский",
                "ганцевичский", "глубокский", "глусский", "гомельский", "горецкий", "городокский", "гродненский", "дзержинский",
                "добрушский", "докшицкий", "дрибинский", "дрогичинский", "дубровенский", "дятловский", "ельский", "жабинковский",
                "житковичский", "жлобинский", "зельвенский", "ивановский", "ивацевичский", "ивьевский", "калинковичский", "каменецкий",
                "кировский", "клецкий", "климовичский", "кличевский", "кобринский", "копыльский", "кореличский", "кормянский",
                "костюковичский", "краснопольский", "кричевский", "круглянский", "крупский", "лельчицкий", "лепельский", "лидский",
                "лиозненский", "логойский", "лоевский", "лунинецкий", "любанский", "ляховичский", "малоритский", "минский",
                "миорский", "могилевский", "мозырский", "молодечненский", "мостовский", "мстиславский", "мядельский", "наровлянский",
                "несвижский", "новогрудский", "новополоцкий", "октябрьский", "оршанский", "осиповичский", "островецкий", "ошмянский",
                "петриковский", "пинский", "полоцкий", "поставский", "пружанский", "пуховичский", "речицкий", "рогачевский",
                "россонский", "светлогорский", "свислочский", "сенненский", "славгородский", "слонимский", "слуцкий", "смолевичский",
                "сморгонский", "солигорский", "стародорожский", "столбцовский", "столинский", "толочинский", "узденский", "ушачский",
                "хойникский", "хотимский", "чаусский", "чашникский", "червенский", "чериковский", "чечерский", "шарковщинский",
                "шкловский", "шумилинский", "щучинский"
        };

        districts.addAll(Arrays.asList(districtsStr));

        aliasValues.add(new AliasValueType("обл", AddressParserItemTypeValue.region, "область"));
        aliasValues.add(new AliasValueType("область", AddressParserItemTypeValue.region, "область"));

        aliasValues.add(new AliasValueType("р-н", AddressParserItemTypeValue.district, "район"));
        aliasValues.add(new AliasValueType("р-он", AddressParserItemTypeValue.district, "район"));
        aliasValues.add(new AliasValueType("район", AddressParserItemTypeValue.district, "район"));

        aliasValues.add(new AliasValueType("нп", AddressParserItemTypeValue.city, ""));     //населенный пункт
        aliasValues.add(new AliasValueType("гп", AddressParserItemTypeValue.city, "гп"));     //городской поселок
        aliasValues.add(new AliasValueType("г\\п", AddressParserItemTypeValue.city, "гп"));     //городской поселок
        aliasValues.add(new AliasValueType("г/п", AddressParserItemTypeValue.city, "гп"));     //городской поселок
        aliasValues.add(new AliasValueType("пгт", AddressParserItemTypeValue.city, "пгт"));    //поселок городского типа
        aliasValues.add(new AliasValueType("кп", AddressParserItemTypeValue.city, "кп"));     //курортный поселок
        aliasValues.add(new AliasValueType("город", AddressParserItemTypeValue.city, "г"));
        aliasValues.add(new AliasValueType("гор", AddressParserItemTypeValue.city, "г"));
        aliasValues.add(new AliasValueType("г", AddressParserItemTypeValue.city, "г"));
        aliasValues.add(new AliasValueType("дер", AddressParserItemTypeValue.city, "д"));
        aliasValues.add(new AliasValueType("деревня", AddressParserItemTypeValue.city, "д"));
        aliasValues.add(new AliasValueType("поселок", AddressParserItemTypeValue.city, "п"));
        aliasValues.add(new AliasValueType("пос", AddressParserItemTypeValue.city, "п"));
        aliasValues.add(new AliasValueType("п", AddressParserItemTypeValue.city, "п"));
        aliasValues.add(new AliasValueType("рп", AddressParserItemTypeValue.city, "рп"));
        aliasValues.add(new AliasValueType("с", AddressParserItemTypeValue.city, "с"));
        aliasValues.add(new AliasValueType("село", AddressParserItemTypeValue.city, "с"));
        aliasValues.add(new AliasValueType("снп", AddressParserItemTypeValue.city, "снп"));
        aliasValues.add(new AliasValueType("х", AddressParserItemTypeValue.city, "х"));
        aliasValues.add(new AliasValueType("аг", AddressParserItemTypeValue.city, "аг"));

        aliasValues.add(new AliasValueType("ул", AddressParserItemTypeValue.street, "улица"));
        aliasValues.add(new AliasValueType("улица", AddressParserItemTypeValue.street, "улица"));
        aliasValues.add(new AliasValueType("цлица", AddressParserItemTypeValue.street, "улица"));//ИСПРАВЛЯЮ ОПЕЧАТКУ
        aliasValues.add(new AliasValueType("вч", AddressParserItemTypeValue.street, "вч"));
        aliasValues.add(new AliasValueType("в\\ч", AddressParserItemTypeValue.street, "вч"));
        aliasValues.add(new AliasValueType("в/ч", AddressParserItemTypeValue.street, "вч"));
        aliasValues.add(new AliasValueType("пр-т", AddressParserItemTypeValue.street, "проспект"));
        aliasValues.add(new AliasValueType("пр-кт", AddressParserItemTypeValue.street, "проспект"));
        aliasValues.add(new AliasValueType("проспект", AddressParserItemTypeValue.street, "проспект"));
        aliasValues.add(new AliasValueType("бульвар", AddressParserItemTypeValue.street, "бульвар"));
        aliasValues.add(new AliasValueType("б-р", AddressParserItemTypeValue.street, "бульвар"));
        aliasValues.add(new AliasValueType("пр", AddressParserItemTypeValue.street, "проезд"));
        aliasValues.add(new AliasValueType("проезд", AddressParserItemTypeValue.street, "проезд"));
        aliasValues.add(new AliasValueType("аллея", AddressParserItemTypeValue.street, "аллея"));
        aliasValues.add(new AliasValueType("микрорайон", AddressParserItemTypeValue.street, "микрорайон"));
        aliasValues.add(new AliasValueType("м-н", AddressParserItemTypeValue.street, "микрорайон"));
        aliasValues.add(new AliasValueType("м-он", AddressParserItemTypeValue.street, "микрорайон"));
        aliasValues.add(new AliasValueType("набережная", AddressParserItemTypeValue.street, "набережная"));
        aliasValues.add(new AliasValueType("пер", AddressParserItemTypeValue.street, "переулок"));
        aliasValues.add(new AliasValueType("пар", AddressParserItemTypeValue.street, "переулок"));//ИСПРАВЛЯЮ ОПЕЧАТКУ
        aliasValues.add(new AliasValueType("переулок", AddressParserItemTypeValue.street, "переулок"));
        aliasValues.add(new AliasValueType("преулок", AddressParserItemTypeValue.street, "переулок"));//ИСПРАВЛЯЮ ОПЕЧАТКУ
        aliasValues.add(new AliasValueType("переул", AddressParserItemTypeValue.street, "переулок"));//ИСПРАВЛЯЮ ОПЕЧАТКУ
        aliasValues.add(new AliasValueType("тракт", AddressParserItemTypeValue.street, "тракт"));
        aliasValues.add(new AliasValueType("шоссе", AddressParserItemTypeValue.street, "шоссе"));
        aliasValues.add(new AliasValueType("квартал", AddressParserItemTypeValue.street, "квартал"));
        aliasValues.add(new AliasValueType("тупик", AddressParserItemTypeValue.street, "тупик"));
        aliasValues.add(new AliasValueType("туп", AddressParserItemTypeValue.street, "тупик"));

        aliasValues.add(new AliasValueType("дом", AddressParserItemTypeValue.house, "дом"));

        aliasValues.add(new AliasValueType("ком", AddressParserItemTypeValue.flat, "квартира"));

        aliasValues.add(new AliasValueType("корпус", AddressParserItemTypeValue.build, "корпус"));
        aliasValues.add(new AliasValueType("корп", AddressParserItemTypeValue.build, "корпус"));
        aliasValues.add(new AliasValueType("кор", AddressParserItemTypeValue.build, "корпус"));
        aliasValues.add(new AliasValueType("к", AddressParserItemTypeValue.build, "корпус"));
        aliasValues.add(new AliasValueType("стр", AddressParserItemTypeValue.build, "корпус"));
        aliasValues.add(new AliasValueType("строение", AddressParserItemTypeValue.build, "корпус"));

        aliasValues.add(new AliasValueType("кв", AddressParserItemTypeValue.UNKNOW, ""));
        aliasValues.add(new AliasValueType("д", AddressParserItemTypeValue.UNKNOW, ""));
        aliasValues.add(new AliasValueType("общ", AddressParserItemTypeValue.UNKNOW, ""));
        aliasValues.add(new AliasValueType("общежитие", AddressParserItemTypeValue.UNKNOW, ""));

        String[] cityFirstStr = {
                "абрицкая", "агальница", "алашки", "амховая", "антонова", "антоновские", "апанасишки", "аронова", "ахимковичи", "бабий",
                "байдино", "бакуново", "баля", "барановичи", "барань", "барсучий", "баченцы", "белая", "белобережская", "белое", "белые",
                "белый",
                "береговая",
                "береза",
                "березова",
                "березовая",
                "березовка",
                "березовый",
                "беседский",
                "бечанская",
                "бирча",
                "бирчанская",
                "ближние",
                "ближняя",
                "блужский",
                "боброво",
                "богатырево",
                "болонов",
                "большая",
                "большие",
                "большое",
                "большой",
                "бондары",
                "борки",
                "боровая",
                "боровенские",
                "боровка",
                "боровое",
                "бортники",
                "босянки",
                "бояры",
                "браздецкая",
                "бракова",
                "браславская",
                "братняя",
                "броды",
                "бронная",
                "буда",
                "буденичская",
                "букарево",
                "бурелом",
                "бучемль",
                "валавская",
                "василины",
                "василькишки",
                "велика",
                "великая",
                "великие",
                "великий",
                "великое",
                "веретеи",
                "верхнее",
                "верхние",
                "верхний",
                "верхняя",
                "верховая",
                "веселая",
                "веселый",
                "ветеревичи",
                "ветчинская",
                "волова",
                "воложино",
                "волчья",
                "вородьков",
                "восточный",
                "вулька",
                "выгребная",
                "выселка",
                "высокая",
                "высокие",
                "высокий",
                "вышков",
                "вышний",
                "вязов",
                "гайдукова",
                "галичская",
                "галый",
                "гаравки",
                "гатная",
                "гвоздь",
                "гиневичев",
                "гинево",
                "глебова",
                "глинная",
                "глубокий",
                "глухой",
                "глухская",
                "голени",
                "головные",
                "голый",
                "голынец",
                "гончарово",
                "горавки",
                "горбово",
                "горелая",
                "горелый",
                "горки",
                "горковская",
                "горная",
                "горные",
                "горовые",
                "городнянский",
                "горошковка",
                "горы",
                "грибова",
                "григи",
                "гридюшки",
                "гринки",
                "губичский",
                "гуркова",
                "гуры",
                "дайнова",
                "дальнее",
                "дальние",
                "данилов",
                "дашки",
                "двор",
                "дворецкая",
                "дворицкая",
                "дворное",
                "дедов",
                "демидова",
                "дешковцы",
                "дмитриевка",
                "добрая",
                "добрый",
                "довъяты",
                "докудово",
                "долгая",
                "долгий",
                "долина",
                "дольная",
                "дольный",
                "досова",
                "драгунские",
                "дроздова",
                "другая",
                "дубина",
                "дубинка",
                "дубицкая",
                "дубовая",
                "дубовый",
                "дубошинский",
                "дубровка",
                "дубровские",
                "дужее",
                "думаришки",
                "еловый",
                "емельянов",
                "ермаки",
                "есипова",
                "ж/д",
                "жвойришки",
                "железная",
                "железнодорожная",
                "желтый",
                "жилин",
                "жирмоны",
                "жуки",
                "жуков",
                "жукойни",
                "журбинская",
                "забелье",
                "заборные",
                "заборье",
                "забуднянские",
                "заверские",
                "завишинская",
                "заводный",
                "заводская",
                "загальская",
                "задняя",
                "зазыбы",
                "зайцева",
                "закорки",
                "закрошинский",
                "закутский",
                "залеский",
                "залог",
                "замошье",
                "занки",
                "заречная",
                "заречное",
                "заречные",
                "зарубин",
                "заря",
                "застенок",
                "затитова",
                "зафранцузская",
                "зеленая",
                "зеленые",
                "зеленый",
                "зимовая",
                "знамя",
                "золотая",
                "золотой",
                "зосин",
                "иванов",
                "иванова",
                "ивановские",
                "ильянские",
                "исакова",
                "кабина",
                "казарма",
                "казацкие",
                "казенные",
                "казимировская",
                "каймина",
                "калиновка",
                "калюга",
                "каменная",
                "каменное",
                "каменные",
                "каменный",
                "каменская",
                "капличский",
                "капоренские",
                "кисаревщина",
                "киселева",
                "кищина",
                "кленник",
                "клепчаный",
                "клетов",
                "климов",
                "кличевские",
                "кляпинская",
                "ковалев",
                "козий",
                "козиное",
                "козлов",
                "козловичи",
                "козлы",
                "козьи",
                "колбчанская",
                "коллективный",
                "колония",
                "комиссарский",
                "конный",
                "корица",
                "корнюшкин",
                "коробы",
                "королев",
                "королева",
                "королевские",
                "корчи",
                "косельский",
                "костричская",
                "кравцова",
                "крайские",
                "красная",
                "красница",
                "краснобережская",
                "красное",
                "красные",
                "красный",
                "кремяница",
                "кривая",
                "кривое",
                "кривой",
                "криничная",
                "круглое",
                "круглый",
                "крутая",
                "крутой",
                "крыжий",
                "крюки",
                "куницкие",
                "курчевская",
                "куцый",
                "лавский",
                "лаздуны",
                "лесная",
                "лесные",
                "лесовая",
                "лещенская",
                "липичанская",
                "липовая",
                "липово",
                "лисинская",
                "лисковская",
                "лозовый",
                "луговая",
                "лукомские",
                "лутово",
                "лучной",
                "лысая",
                "лютнянская",
                "лютый",
                "лядск",
                "малая",
                "малевичская",
                "малиновка",
                "малое",
                "малые",
                "малый",
                "марьина",
                "марьино",
                "мастицкая",
                "матьковские",
                "машецкая",
                "медвежий",
                "медовая",
                "медынки",
                "межная",
                "мерецкий",
                "местечко",
                "микуль",
                "миорки",
                "михалево",
                "мнюто",
                "мокрая",
                "мокрянские",
                "морозова",
                "московская",
                "мостище",
                "мосты",
                "мулеры",
                "мурины",
                "мурованая",
                "мыслов",
                "наша",
                "недашево",
                "нижнее",
                "нижние",
                "нижний",
                "нижняя",
                "низкая",
                "низкий",
                "николаевка",
                "новая",
                "новицкие",
                "новоганцевичская",
                "новое",
                "новоселки",
                "новосяды",
                "новые",
                "новый",
                "оголичская",
                "огородня",
                "околица",
                "олешковичи",
                "олизаров",
                "олься",
                "ольшаники",
                "омеленская",
                "оплиса",
                "орлова",
                "осада",
                "осиповичи",
                "осовецкая",
                "осовской",
                "острая",
                "островской",
                "острошицкий",
                "оточная",
                "палуж",
                "паньковская",
                "пархимковская",
                "пасека",
                "пастухово",
                "паськова",
                "пацева",
                "пашутская",
                "пенная",
                "пепелишки",
                "первая",
                "первое",
                "первомайский",
                "первый",
                "переровский",
                "перетрутовский",
                "перунов",
                "перучь",
                "першая",
                "песочная",
                "песчаная",
                "петралевичи",
                "плавущее",
                "плесовичская",
                "плиговки",
                "пограничное",
                "подбельские",
                "подбродяны",
                "поженьки",
                "полевая",
                "половинный",
                "половковский",
                "полонное",
                "полыковичи",
                "полыковичские",
                "поселок",
                "почта",
                "почтовая",
                "прибужье",
                "прилепская",
                "прилуки",
                "прилукская",
                "присно",
                "пролетарская",
                "прусинская",
                "пруска",
                "пустая",
                "пустой",
                "путеводная",
                "пуща",
                "пячковский",
                "радькова",
                "разъезд",
                "разьезд",
                "раков",
                "ранняя",
                "рачнево",
                "редкий",
                "репная",
                "речки",
                "ржавка",
                "рованичская",
                "ровенская",
                "ровецкий",
                "ровное",
                "родное",
                "рожки",
                "роза",
                "романов",
                "романяцкая",
                "росина",
                "росский",
                "руда",
                "рудец",
                "рудня",
                "русское",
                "рутка",
                "рыча",
                "рябиновая",
                "рябый",
                "савин",
                "савичев",
                "савский",
                "сакова",
                "самуйловичи",
                "саска",
                "светлая",
                "светлый",
                "свиталовка",
                "свободная",
                "святая",
                "северное",
                "селище",
                "сельцо",
                "селянская",
                "семеновка",
                "семков",
                "серебряный",
                "середняя",
                "силино",
                "симоничская",
                "симоничский",
                "синие",
                "синий",
                "синицкое",
                "синявская",
                "синяя",
                "ситницкий",
                "ситьково",
                "скаменный",
                "слижи",
                "слобода",
                "слободка",
                "слободской",
                "смолеговская",
                "смоловые",
                "смольковская",
                "смоляная",
                "советская",
                "соколье",
                "солодухи",
                "солонец",
                "солонская",
                "сонькин",
                "сороки",
                "сосновая",
                "сосновка",
                "сосновое",
                "сосновый",
                "споры",
                "среднее",
                "средние",
                "станция",
                "старая",
                "староганцевичская",
                "старое",
                "старосеков",
                "старые",
                "старый",
                "степанова",
                "стриганецкие",
                "строкова",
                "студеная",
                "сухая",
                "сухой",
                "сыпаная",
                "сырская",
                "сычевка",
                "сябрынь",
                "табольская",
                "татенка",
                "темный",
                "темра",
                "теплая",
                "терехов",
                "тесновая",
                "тихонова",
                "толстый",
                "томилова",
                "тонежская",
                "третной",
                "тупичино",
                "турковская",
                "турна",
                "тюрли",
                "убортская",
                "узнацкий",
                "улино",
                "урочище",
                "усохская",
                "уша",
                "фарный",
                "федотова",
                "французская",
                "хвойная",
                "хистецкий",
                "хлебный",
                "хлупинская",
                "хмелевские",
                "хорошая",
                "хутор",
                "хутора",
                "цирклишки",
                "чемери",
                "чепуки",
                "червоное",
                "червоный",
                "черемушники",
                "черная",
                "чернецкий",
                "черные",
                "черный",
                "чернявские",
                "черняцкая",
                "чирвоная",
                "чирвоное",
                "чирвоны",
                "чирвоный",
                "чистая",
                "чистые",
                "чистый",
                "чумачов",
                "чурилово",
                "шаличи",
                "шарипы",
                "шереховская",
                "шилов",
                "шиманов",
                "широкий",
                "щежерь",
                "щекотовская",
                "щучий",
                "южное",
                "южный",
                "юный",
                "ядреная",
                "якимова",
                "якимово",
                "яковина",
                "якубовичи",
                "янка",
                "янки",
                "янков",
                "ясная",
                "ясное",
                "ясный",
                "ячная"
        };

        cityFirst.addAll(Arrays.asList(cityFirstStr));
    }
}
