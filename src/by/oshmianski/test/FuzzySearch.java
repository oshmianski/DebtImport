package by.oshmianski.test;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 07.10.13
 * Time: 12:56
 */
public class FuzzySearch {

    public static void main(String[] args) {
        Address address;

        String test1 = "231100 бэарус р'спублика, обл. гродненнскаяя ошмяны кв.стрAителей дд.3 кв  15 456890";
        String test2 = "201100 бэарус р'спублика, брэсская барановичкий район совет городищенский г. арабовщина кв.стрAителей д. 357а кв69 012345";

        FuzzySearch fuzzySearch = new FuzzySearch();
        address = fuzzySearch.getAddress(test1);
        System.out.println("===");
        address = fuzzySearch.getAddress(test2);
    }

    public Address getAddress(String addressStr) {
        Address address = new Address();
        String country = "Беларусь";
        String[] regions = {"Брестская", "Гродненская", "Гомельская", "Витебская", "Минская", "Могилевская"};
        String[] districts = {"Барановичский", "Белыничский", "Березинский", "Березовский", "Берестовицкий", "Бешенковичский",
                "Бобруйский", "Борисовский", "Брагинский", "Браславский", "Брестский", "Буда-Кошелевский", "Быховский",
                "Верхнедвинский", "Ветковский", "Вилейский", "Витебский", "Волковысский", "Воложинский", "Вороновский",
                "Ганцевичский", " Глубокский", " Глусский", " Гомельский", " Горецкий", " Городокский", " Гродненский", " Дзержинский",
                "Добрушский", "Докшицкий", "Дрибинский", "Дрогичинский", "Дубровенский", "Дятловский", "Ельский", "Жабинковский",
                "Житковичский", "Жлобинский", "Зельвенский", "Ивановский", "Ивацевичский", "Ивьевский", "Калинковичский", "Каменецкий",
                "Кировский", "Клецкий", "Климовичский", "Кличевский", "Кобринский", "Копыльский", "Кореличский", "Кормянский",
                "Костюковичский", "Краснопольский", "Кричевский", "Круглянский", "Крупский", "Лельчицкий", "Лепельский", "Лидский",
                "Лиозненский", "Логойский", "Лоевский", "Лунинецкий", "Любанский", "Ляховичский", "Малоритский", "Минский",
                "Миорский", "Могилевский", "Мозырский", "Молодечненский", "Мостовский", "Мстиславский", "Мядельский", "Наровлянский",
                "Несвижский", "Новогрудский", "Новополоцкий", "Октябрьский", "Оршанский", "Осиповичский", "Островецкий", "Ошмянский",
                "Петриковский", "Пинский", "Полоцкий", "Поставский", "Пружанский", "Пуховичский", "Речицкий", "Рогачевский",
                "Россонский", "Светлогорский", "Свислочский", "Сенненский", "Славгородский", "Слонимский", "Слуцкий", "Смолевичский",
                "Сморгонский", "Солигорский", "Стародорожский", "Столбцовский", "Столинский", "Толочинский", "Узденский", "Ушачский",
                "Хойникский", "Хотимский", "Чаусский", "Чашникский", "Червенский", "Чериковский", "Чечерский", "Шарковщинский",
                "Шкловский", "Шумилинский", "Щучинский"
        };

        String addressStrTmp = addressStr.replaceAll(" ", "_").replaceAll(",", "_");
        String[] addressArray = addressStrTmp.split("_");
        String strTmp = "";
        boolean isCountry;
        boolean isRegion;
        int threshold = 3;

        for (String str : addressArray) {
            isCountry = false;
            isRegion = false;

            strTmp = str.trim().toLowerCase();

            if (StringUtils.getLevenshteinDistance(strTmp, country.toLowerCase(), threshold) != -1) {   //поиск страны
                address.setCountry(country);
                isCountry = true;
            }

            //поиск области
            if (!isCountry) {
                for (String region : regions)
                    if (StringUtils.getLevenshteinDistance(strTmp, region.toLowerCase(), threshold) != -1) {
                        address.setRegion(region);
                        isRegion = true;

                        break;
                    }
            }

            //поиск района
            if (!(isRegion || isCountry)) {
                for (String district : districts)
                    if (StringUtils.getLevenshteinDistance(strTmp, district.toLowerCase(), threshold) != -1) {
                        address.setDistrict(district);

                        break;
                    }
            }
        }

        String val = "";
        //индекс
        Pattern pattern3 = Pattern.compile("\\d{6}");
        Matcher matcher3 = pattern3.matcher(addressStr);
        if (matcher3.find()) {
            System.out.println("индекс=" + matcher3.group());
            address.setIndex(matcher3.group());
            addressStr.replace(matcher3.group(), "");
        }

        val = "";
        //город
        Pattern patternCity = Pattern.compile("(?<=(г\\.|гор\\.)\\s{0,}).*");
        Matcher matcherCity = patternCity.matcher(addressStr);
        if (matcherCity.find()) {
            val = matcherCity.group().trim();
            if (val.indexOf(" ") != -1)
                val = StringUtils.left(val, val.indexOf(" "));
            val = val.replaceAll(",", "").trim();
            System.out.println("город=" + val);
            address.setCity(val);
        }

        val = "";
        //дом
        Pattern pattern = Pattern.compile("(?<=(д|д\\.|дом|дом\\.)\\s{0,})\\d.*");
        Matcher matcher = pattern.matcher(addressStr);
        if (matcher.find()) {
            val = matcher.group().trim();
            if (val.indexOf(" ") != -1)
                val = StringUtils.left(val, val.indexOf(" "));
            val = val.replaceAll(",", "").trim();
            System.out.println("дом=" + val);
            address.setHouse(val);
        }

        val = "";
        //квартира
        Pattern pattern2 = Pattern.compile("(?<=(к|к\\.|квартира|кв\\.|кв)\\s{0,})\\d.*");
        Matcher matcher2 = pattern2.matcher(addressStr);
        if (matcher2.find()) {
            val = matcher2.group().trim();
            if (val.indexOf(" ") != -1)
                val = StringUtils.left(val, val.indexOf(" "));
            val = val.replaceAll(",", "").trim();
            System.out.println("квартира=" + val);
            address.setFlat(val);
        }

        return address;
    }
}
