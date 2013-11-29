package by.oshmianski.objects;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 07.10.13
 * Time: 12:56
 */
public class FuzzySearch {
    private String country = "Беларусь";
    private String[] regions = {"Брестская", "Гродненская", "Гомельская", "Витебская", "Минская", "Могилевская"};
    private String[] districts = {"Барановичский", "Белыничский", "Березинский", "Березовский", "Берестовицкий", "Бешенковичский",
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
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static void main(String[] args) {
        String t = " .";
        String regex = "^\\.$";
        System.out.println("regex=" + regex);
        System.out.println("t=" + t.replaceAll(regex, ""));
    }

    public Passport getPassport(String passStr, ArrayList<DataChildItem> dataChildItems) throws ParseException {
        Passport passport = new Passport("", "", "", "");

        String[] passTypes = {"Паспорт гражданина РБ", "Вид на жительство", "Иностранный паспорт"};

        Pattern pattern = Pattern.compile("(?<=выдан\\s{1}.{8}\\s{1}).{0,}");
        Matcher matcher = pattern.matcher(passStr);
        if (matcher.find()) {
            passport.setPassOrg(matcher.group());
            passStr = passStr.replace(matcher.group(), "").trim();
        }

        pattern = Pattern.compile("(?<=выдан\\s).{0,}");
        matcher = pattern.matcher(passStr);
        if (matcher.find()) {
            passport.setPassDate(formatter.format(new SimpleDateFormat("dd.MM.yy").parse(matcher.group())));
            passStr = passStr.replace(matcher.group(), "").trim();
        }
        passStr = passStr.replace("выдан", "").trim();

        passStr = passStr.trim().toLowerCase();
        for (String passType : passTypes) {
            if (passStr.indexOf(passType.toLowerCase()) > -1) {
                passport.setPassType(passType);
                passStr = passStr.replace(passType.toLowerCase(), "").trim();
                break;
            }
        }

        passport.setPassNum(passStr.toUpperCase());

        processWarningPassport(passport, dataChildItems);

        return passport;
    }

    public Address getAddressStructured1(String addressStr, ArrayList<DataChildItem> dataChildItems) {
        Address address = new Address();
        AddressCityWithType cityWithType;
        AddressStreetWithType streetWithType;

        if (!addressStr.isEmpty()) {
            String[] addressArray = addressStr.split(",", 7);
            if (addressArray.length == 7) {
                String cityUnprocessed = addressArray[3].trim().replaceAll("^\\.$", "");
                String streetUnprocessed = addressArray[4].trim().replaceAll("^\\.$", "");

                cityWithType = processCityUnprocessed(cityUnprocessed);
                streetWithType = processStreetUnprocessed(streetUnprocessed);

                address.setIndex(addressArray[0].trim());
                address.setRegion(addressArray[1].trim());
                address.setDistrict(addressArray[2].trim());
                address.setCity(cityWithType.getCity());
                address.setCityType(cityWithType.getCityType());
                address.setStreet(streetWithType.getStreet());
                address.setStreetType(streetWithType.getStreetType());
                address.setHouse(addressArray[5].trim().replaceAll("^\\.$", ""));
                address.setFlat(addressArray[6].trim().replaceAll("^\\.$", ""));
            }
        }
        processWarningAddress(address, dataChildItems);

        return address;
    }

    public AddressCityWithType processCityUnprocessed(String cityUnprocessed) {
        AddressCityWithType cityWithType = new AddressCityWithType();
        ArrayList<AliasValue> aliasValues = new ArrayList<AliasValue>();

//        aliasValues.add(new AliasValue("н.п.", "н.п."));
        aliasValues.add(new AliasValue("г.п.", "г.п."));
        aliasValues.add(new AliasValue("г.", "город"));
        aliasValues.add(new AliasValue("гор.", "город"));
        aliasValues.add(new AliasValue("город", "город"));
        aliasValues.add(new AliasValue("д.", "деревня"));
        aliasValues.add(new AliasValue("дер.", "деревня"));
        aliasValues.add(new AliasValue("деревня", "деревня"));

        if (cityUnprocessed.isEmpty())
            return cityWithType;

        for (AliasValue aliasValue : aliasValues)
            if (cityUnprocessed.indexOf(aliasValue.getAlias()) > -1) {
                cityWithType.setCityType(aliasValue.getValue());
                cityUnprocessed = cityUnprocessed.replace(aliasValue.getAlias(), "");
            }

        cityWithType.setCity(cityUnprocessed.trim());

        return cityWithType;
    }

    public AddressStreetWithType processStreetUnprocessed(String streetUnprocessed) {
        AddressStreetWithType streetWithType = new AddressStreetWithType();
        ArrayList<AliasValue> aliasValues = new ArrayList<AliasValue>();

        aliasValues.add(new AliasValue("ул.", "улица"));
        aliasValues.add(new AliasValue("улица", "улица"));
        aliasValues.add(new AliasValue("пер.", "переулок"));
        aliasValues.add(new AliasValue("пер-к", "переулок"));
        aliasValues.add(new AliasValue("переулок", "переулок"));
        aliasValues.add(new AliasValue("пр.", "проспект"));
        aliasValues.add(new AliasValue("пр-т", "проспект"));
        aliasValues.add(new AliasValue("пр-кт", "проспект"));
        aliasValues.add(new AliasValue("проспект", "проспект"));
        aliasValues.add(new AliasValue("п-д", "проезд"));
        aliasValues.add(new AliasValue("проезд", "проезд"));
        aliasValues.add(new AliasValue("м-н", "микрорайон"));
        aliasValues.add(new AliasValue("мик-н", "микрорайон"));
        aliasValues.add(new AliasValue("микрорайон", "микрорайон"));
        aliasValues.add(new AliasValue("тракт", "тракт"));
        aliasValues.add(new AliasValue("бульвар", "бульвар"));
        aliasValues.add(new AliasValue("б-р", "бульвар"));

        if (streetUnprocessed.isEmpty())
            return streetWithType;


        for (AliasValue aliasValue : aliasValues)
            if (streetUnprocessed.indexOf(aliasValue.getAlias()) > -1) {
                streetWithType.setStreetType(aliasValue.getValue());
                streetUnprocessed = streetUnprocessed.replace(aliasValue.getAlias(), "");
            }



        streetWithType.setStreet(streetUnprocessed.trim());

        return streetWithType;
    }

    public Address getAddress(String addressStr, ArrayList<DataChildItem> dataChildItems) {
        Address address = new Address();

        String addressStrTmp = addressStr.replaceAll(" ", "_").replaceAll(",", "_");
        String[] addressArray = addressStrTmp.split("_");
        String strTmp = "";
        boolean isCountry;
        boolean isRegion;
        boolean isDistrict;
        int threshold = 3;

        isCountry = false;
        isRegion = false;
        isDistrict = false;

        //четкий поиск страны
        for (String str : addressArray) {
            strTmp = str.trim().toLowerCase();

            if (strTmp.indexOf(country.toLowerCase()) > -1) {
                address.setCountry(country);
                isCountry = true;

                break;
            }
        }

        //четкий поиск области
        for (String str : addressArray) {
            strTmp = str.trim().toLowerCase();

            for (String region : regions)
                if (strTmp.indexOf(region.toLowerCase()) > -1) {
                    address.setRegion(region);
                    isRegion = true;

                    break;
                }

            if (isRegion) break;
        }

        //четкий поиск района
        for (String str : addressArray) {
            strTmp = str.trim().toLowerCase();

            for (String district : districts)
                if (strTmp.indexOf(district.toLowerCase()) > -1) {
                    address.setDistrict(district);
                    isDistrict = true;

                    break;
                }

            if (isDistrict) break;
        }

        //нечеткий поиск страны. процент ошибки = threshold = 3.
//        if (!isCountry) {
//            for (String str : addressArray) {
//                strTmp = str.trim().toLowerCase();
//
//                if (StringUtils.getLevenshteinDistance(strTmp, country.toLowerCase(), threshold) != -1) {
//                    address.setCountry(country);
//                    break;
//                }
//            }
//        }

        //нечеткий поиск области. процент ошибки = threshold = 3.
//        if (!isRegion) {
//            for (String str : addressArray) {
//                strTmp = str.trim().toLowerCase();
//
//                for (String region : regions)
//                    if (StringUtils.getLevenshteinDistance(strTmp, region.toLowerCase(), threshold) != -1) {
//                        address.setRegion(region);
//                        isRegion = true;
//                        break;
//                    }
//
//                if(isRegion) break;
//            }
//        }

        //нечеткий поиск района. процент ошибки = threshold = 3.
//        if (!isDistrict) {
//            for (String str : addressArray) {
//                strTmp = str.trim().toLowerCase();
//
//                for (String district : districts)
//                    if (StringUtils.getLevenshteinDistance(strTmp, district.toLowerCase(), threshold) != -1) {
//                        address.setDistrict(district);
//                        isDistrict = true;
//                        break;
//                    }
//
//                if(isDistrict) break;
//            }
//        }


        String val = "";
        //индекс
        Pattern pattern3 = Pattern.compile("\\d{6}");
        Matcher matcher3 = pattern3.matcher(addressStr);
        if (matcher3.find())

        {
            address.setIndex(matcher3.group());
            addressStr.replace(matcher3.group(), "");
        }

        val = "";
        //город
        Pattern patternCity = Pattern.compile("(?<=(г\\.|гор\\.)\\s{0,}).*");
        Matcher matcherCity = patternCity.matcher(addressStr);
        if (matcherCity.find())

        {
            val = matcherCity.group().trim();
            if (val.indexOf(" ") != -1)
                val = StringUtils.left(val, val.indexOf(" "));
            val = val.replaceAll(",", "").trim();
            address.setCity(val);
        }

        val = "";
        //дом
        Pattern pattern = Pattern.compile("(?<=(д|д\\.|дом|дом\\.)\\s{0,})\\d.*");
        Matcher matcher = pattern.matcher(addressStr);
        if (matcher.find())

        {
            val = matcher.group().trim();
            if (val.indexOf(" ") != -1)
                val = StringUtils.left(val, val.indexOf(" "));
            val = val.replaceAll(",", "").trim();
            address.setHouse(val);
        }

        val = "";
        //квартира
        Pattern pattern2 = Pattern.compile("(?<=(к|к\\.|квартира|кв\\.|кв)\\s{0,})\\d.*");
        Matcher matcher2 = pattern2.matcher(addressStr);
        if (matcher2.find())

        {
            val = matcher2.group().trim();
            if (val.indexOf(" ") != -1)
                val = StringUtils.left(val, val.indexOf(" "));
            val = val.replaceAll(",", "").trim();
            address.setFlat(val);
        }

        processWarningAddress(address, dataChildItems);

        return address;
    }

    private void processWarningAddress(Address address, ArrayList<DataChildItem> dataChildItems) {
        if (address.getIndex().isEmpty()) {
            DataChildItem dataChildItem = new DataChildItem(
                    Status.WARNING_ADDRESS_NO_INDEX,
                    "_Заполнение адреса",
                    "Ошибка",
                    "Отсутствует индекс"
            );
            dataChildItems.add(dataChildItem);
        }

        if (address.getCityType().isEmpty()) {
            DataChildItem dataChildItem = new DataChildItem(
                    Status.WARNING_ADDRESS_NO_CITY_TYPE,
                    "_Заполнение адреса",
                    "Ошибка",
                    "Отсутствует тип города"
            );
            dataChildItems.add(dataChildItem);
        }

        if (address.getCity().isEmpty()) {
            DataChildItem dataChildItem = new DataChildItem(
                    Status.WARNING_ADDRESS_NO_CITY,
                    "_Заполнение адреса",
                    "Ошибка",
                    "Отсутствует город"
            );
            dataChildItems.add(dataChildItem);
        }

        if (address.getStreetType().isEmpty()) {
            DataChildItem dataChildItem = new DataChildItem(
                    Status.WARNING_ADDRESS_NO_STREET_TYPE,
                    "_Заполнение адреса",
                    "Ошибка",
                    "Отсутствует тип улицы"
            );
            dataChildItems.add(dataChildItem);
        }

        if (address.getStreet().isEmpty()) {
            DataChildItem dataChildItem = new DataChildItem(
                    Status.WARNING_ADDRESS_NO_STREET,
                    "_Заполнение адреса",
                    "Ошибка",
                    "Отсутствует улица"
            );
            dataChildItems.add(dataChildItem);
        }

        if (address.getHouse().isEmpty()) {
            DataChildItem dataChildItem = new DataChildItem(
                    Status.WARNING_ADDRESS_NO_HOUSE,
                    "_Заполнение адреса",
                    "Ошибка",
                    "Отсутствует дом"
            );
            dataChildItems.add(dataChildItem);
        }
    }

    private void processWarningPassport(Passport passport, ArrayList<DataChildItem> dataChildItems) {
        if (passport.getPassType().isEmpty()) {
            DataChildItem dataChildItem = new DataChildItem(
                    Status.WARNING_PASSPORT_NO_TYPE,
                    "_Заполнение паспорта",
                    "Ошибка",
                    "Отсутствует тип"
            );
            dataChildItems.add(dataChildItem);
        }

        if (passport.getPassNum().isEmpty()) {
            DataChildItem dataChildItem = new DataChildItem(
                    Status.WARNING_PASSPORT_NO_NUM,
                    "_Заполнение паспорта",
                    "Ошибка",
                    "Отсутствует номер"
            );
            dataChildItems.add(dataChildItem);
        }

        if (passport.getPassDate().isEmpty()) {
            DataChildItem dataChildItem = new DataChildItem(
                    Status.WARNING_PASSPORT_NO_DATE,
                    "_Заполнение паспорта",
                    "Ошибка",
                    "Отсутствует дата выдачи"
            );
            dataChildItems.add(dataChildItem);
        }

        if (passport.getPassOrg().isEmpty()) {
            DataChildItem dataChildItem = new DataChildItem(
                    Status.WARNING_PASSPORT_NO_ORG,
                    "_Заполнение паспорта",
                    "Ошибка",
                    "Отсутствует орган выдачи"
            );
            dataChildItems.add(dataChildItem);
        }
    }

    private static class AliasValue {
        private String alias;
        private String value;

        private AliasValue(String alias, String value) {
            this.alias = alias;
            this.value = value;
        }

        private String getAlias() {
            return alias;
        }

        private void setAlias(String alias) {
            this.alias = alias;
        }

        private String getValue() {
            return value;
        }

        private void setValue(String value) {
            this.value = value;
        }
    }
}
