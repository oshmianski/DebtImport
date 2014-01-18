package by.oshmianski.objects.addressParser;

import java.lang.reflect.Array;
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
    public ArrayList<AliasValueType> aliasValues = new ArrayList<AliasValueType>();

    static {
        regions.add("брестская");
        regions.add("гродненская");
        regions.add("гомельская");
        regions.add("витебская");
        regions.add("минская");
        regions.add("могилевская");

        String[] districtsStr = {"барановичский", "белыничский", "березинский", "березовский", "берестовицкий", "бешенковичский",
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
        aliasValues.add(new AliasValueType("район", AddressParserItemTypeValue.district, "район"));

        aliasValues.add(new AliasValueType("нп", AddressParserItemTypeValue.city, ""));     //населенный пункт
        aliasValues.add(new AliasValueType("гп", AddressParserItemTypeValue.city, "гп"));     //городской поселок
        aliasValues.add(new AliasValueType("пгт", AddressParserItemTypeValue.city, "пгт"));    //поселок городского типа
        aliasValues.add(new AliasValueType("кп", AddressParserItemTypeValue.city, "кп"));     //курортный поселок
        aliasValues.add(new AliasValueType("город", AddressParserItemTypeValue.city, "г"));
        aliasValues.add(new AliasValueType("гор", AddressParserItemTypeValue.city, "г"));
        aliasValues.add(new AliasValueType("г", AddressParserItemTypeValue.city, "г"));
        aliasValues.add(new AliasValueType("дер", AddressParserItemTypeValue.city, "д"));
        aliasValues.add(new AliasValueType("деревня", AddressParserItemTypeValue.city, "д"));
        aliasValues.add(new AliasValueType("пос", AddressParserItemTypeValue.city, "п"));
        aliasValues.add(new AliasValueType("поселок", AddressParserItemTypeValue.city, "п"));
        aliasValues.add(new AliasValueType("рп", AddressParserItemTypeValue.city, "рп"));
        aliasValues.add(new AliasValueType("с", AddressParserItemTypeValue.city, "с"));
        aliasValues.add(new AliasValueType("село", AddressParserItemTypeValue.city, "с"));
        aliasValues.add(new AliasValueType("снп", AddressParserItemTypeValue.city, "снп"));
        aliasValues.add(new AliasValueType("х", AddressParserItemTypeValue.city, "х"));
        aliasValues.add(new AliasValueType("аг", AddressParserItemTypeValue.city, "аг"));

        aliasValues.add(new AliasValueType("ул", AddressParserItemTypeValue.street, "улица"));
        aliasValues.add(new AliasValueType("улица", AddressParserItemTypeValue.street, "улица"));
        aliasValues.add(new AliasValueType("пр-т", AddressParserItemTypeValue.street, "проспект"));
        aliasValues.add(new AliasValueType("пр-кт", AddressParserItemTypeValue.street, "проспект"));
        aliasValues.add(new AliasValueType("проспект", AddressParserItemTypeValue.street, "проспект"));
        aliasValues.add(new AliasValueType("бульвар", AddressParserItemTypeValue.street, "бульвар"));
        aliasValues.add(new AliasValueType("пр", AddressParserItemTypeValue.street, "проезд"));
        aliasValues.add(new AliasValueType("проезд", AddressParserItemTypeValue.street, "проезд"));
        aliasValues.add(new AliasValueType("аллея", AddressParserItemTypeValue.street, "аллея"));
        aliasValues.add(new AliasValueType("микрорайон", AddressParserItemTypeValue.street, "микрорайон"));
        aliasValues.add(new AliasValueType("м-н", AddressParserItemTypeValue.street, "микрорайон"));
        aliasValues.add(new AliasValueType("набережная", AddressParserItemTypeValue.street, "набережная"));
        aliasValues.add(new AliasValueType("пер", AddressParserItemTypeValue.street, "переулок"));
        aliasValues.add(new AliasValueType("переулок", AddressParserItemTypeValue.street, "переулок"));
        aliasValues.add(new AliasValueType("тракт", AddressParserItemTypeValue.street, "тракт"));
        aliasValues.add(new AliasValueType("шоссе", AddressParserItemTypeValue.street, "шоссе"));

        aliasValues.add(new AliasValueType("дом", AddressParserItemTypeValue.house, "дом"));

        aliasValues.add(new AliasValueType("кв", AddressParserItemTypeValue.UNKNOW, ""));
        aliasValues.add(new AliasValueType("д", AddressParserItemTypeValue.UNKNOW, ""));
    }
}
