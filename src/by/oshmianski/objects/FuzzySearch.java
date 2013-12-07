package by.oshmianski.objects;

import by.oshmianski.utils.MyLog;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
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
            "Ганцевичский", "Глубокский", "Глусский", "Гомельский", "Горецкий", "Городокский", "Гродненский", "Дзержинский",
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

    private View viewGEO;

    public FuzzySearch(View viewGEO) {
        this.viewGEO = viewGEO;
    }

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

        if (address.getCityType().isEmpty()) {
            processCityTypeWithGEO(address);
        }

        processWarningAddress(address, dataChildItems);

        return address;
    }

    public AddressCityWithType processCityUnprocessed(String cityUnprocessed) {
        AddressCityWithType cityWithType = new AddressCityWithType();
        ArrayList<AliasValue> aliasValues = new ArrayList<AliasValue>();

//        aliasValues.add(new AliasValue("н.п.", "н.п."));
        aliasValues.add(new AliasValue("город", "г"));
        aliasValues.add(new AliasValue("деревня", "д"));
        aliasValues.add(new AliasValue("гор.", "г"));
        aliasValues.add(new AliasValue("дер.", "д"));
        aliasValues.add(new AliasValue("г.п.", "гп"));
        aliasValues.add(new AliasValue("гп.", "гп"));
        aliasValues.add(new AliasValue("кп.", "кп"));
        aliasValues.add(new AliasValue("п.", "п"));
        aliasValues.add(new AliasValue("с.", "с"));
        aliasValues.add(new AliasValue("г.", "г"));
        aliasValues.add(new AliasValue("д.", "д"));

        if (cityUnprocessed.isEmpty())
            return cityWithType;

        for (AliasValue aliasValue : aliasValues)
            if (cityUnprocessed.toLowerCase().indexOf(aliasValue.getAlias().toLowerCase()) > -1) {
                cityWithType.setCityType(aliasValue.getValue());
                cityUnprocessed = cityUnprocessed.replaceAll("(?i)" + aliasValue.getAlias().replaceAll("\\.", "\\\\."), "");
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
        aliasValues.add(new AliasValue("аллея", "аллея"));
        aliasValues.add(new AliasValue("ал.", "аллея"));
        aliasValues.add(new AliasValue("набережная", "набережная"));
        aliasValues.add(new AliasValue("наб.", "набережная"));
        aliasValues.add(new AliasValue("шоссе", "шоссе"));
        aliasValues.add(new AliasValue("ш.", "шоссе"));
        aliasValues.add(new AliasValue("площадь", "площадь"));
        aliasValues.add(new AliasValue("пл.", "площадь"));

        if (streetUnprocessed.isEmpty())
            return streetWithType;


        for (AliasValue aliasValue : aliasValues)
            if (streetUnprocessed.toLowerCase().indexOf(aliasValue.getAlias().toLowerCase()) > -1) {
                streetWithType.setStreetType(aliasValue.getValue());
                streetUnprocessed = streetUnprocessed.replaceAll("(?i)" + aliasValue.getAlias().replaceAll("\\.", "\\\\."), "");
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

        //очищу от лишних пробелов
        int index = 0;
        for (String s : addressArray) {
            addressArray[index] = s.trim();
            index++;
        }

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

            if (!strTmp.isEmpty())
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
        if (addressStr.indexOf("@") > -1) {
            String s = addressStr.substring(addressStr.indexOf("н.п.") + 4);
            s = s.substring(0, s.indexOf("@"));

            address.setCity(s);
        } else {
            Pattern patternCity = Pattern.compile("(?<=(г\\.|гор\\.)\\s{0,}).*");
            Matcher matcherCity = patternCity.matcher(addressStr);
            if (matcherCity.find())

            {
                val = matcherCity.group().trim();
                if (val.contains(" "))
                    val = StringUtils.left(val, val.indexOf(" "));
                val = val.replaceAll(",", "").trim();
                address.setCity(val);
                address.setCityType("г");
            }
            Pattern patternCity2 = Pattern.compile("(?<=(н\\.п\\.|нп\\.)\\s*).*");
            Matcher matcherCity2 = patternCity2.matcher(addressStr);
            if (matcherCity2.find())

            {
                val = matcherCity2.group().trim();
                if (val.contains(" "))
                    val = StringUtils.left(val, val.indexOf(" "));
                val = val.replaceAll(",", "").trim();
                address.setCity(val);
            }

            Pattern patternCity3 = Pattern.compile("(?<=(д\\.|дер\\.)\\s*)\\D.*");
            Matcher matcherCity3 = patternCity3.matcher(addressStr);
            if (matcherCity3.find())

            {
                val = matcherCity3.group().trim();
                if (val.contains(" "))
                    val = StringUtils.left(val, val.indexOf(" "));
                val = val.replaceAll(",", "").trim();
                address.setCity(val);
                address.setCityType("д");
            }
        }

        //улица
        if(addressStr.indexOf("~") > -1){
            String s = addressStr.substring(addressStr.indexOf("&") + 1);
            s = s.substring(0, s.indexOf("~"));

            String streetType = s.substring(0, 1);

            if("Т".equalsIgnoreCase(streetType)){
                address.setStreetType("тракт");
            }
            if("С".equalsIgnoreCase(streetType)){
                address.setStreetType("улица");
            }
            if("П".equalsIgnoreCase(streetType)){
                address.setStreetType("проспект");
            }
            if("М".equalsIgnoreCase(streetType)){
                address.setStreetType("микрорайон");
            }
            if("Н".equalsIgnoreCase(streetType)){
//                address.setStreetType("улица");
            }
            if("Р".equalsIgnoreCase(streetType)){
                address.setStreetType("переулок");
            }
            if("Б".equalsIgnoreCase(streetType)){
                address.setStreetType("бульвар");
            }

            address.setStreet(s.substring(1));
        }

        val = "";
        //дом
        Pattern pattern = Pattern.compile("(?<=(д|д\\.|дом|дом\\.)\\s{0,})\\d.*");
        Matcher matcher = pattern.matcher(addressStr);
        if (matcher.find())

        {
            val = matcher.group().trim();
            if (val.contains(" "))
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
            if (val.contains(" "))
                val = StringUtils.left(val, val.indexOf(" "));
            val = val.replaceAll(",", "").trim();
            address.setFlat(val);
        }

        if (address.getCityType().isEmpty() && !address.getCity().isEmpty()) {
            processCityTypeWithGEO(address);
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

    private void processCityTypeWithGEO(Address address) {
        String cityType = "";
        boolean equal = true;

        ViewEntryCollection vec = null;
        ViewEntry ve = null;
        ViewEntry vetmp = null;

        try {
            vec = viewGEO.getAllEntriesByKey(address.getCity(), true);

            if (vec.getCount() == 1) {
                ve = vec.getFirstEntry();
                Vector vals = ve.getColumnValues();
                address.setCityType(vals.elementAt(3).toString());

                return;
            }

            if (vec.getCount() > 1) {
                Vector key = new Vector();
                key.addElement(address.getCity());
                key.addElement(address.getDistrict());
                vec = viewGEO.getAllEntriesByKey(key, true);

                if (vec.getCount() == 1) {
                    ve = vec.getFirstEntry();
                    Vector vals = ve.getColumnValues();
                    address.setCityType(vals.elementAt(3).toString());

                    return;
                }

                if (vec.getCount() > 1) {
                    ve = vec.getFirstEntry();
                    Vector vals = ve.getColumnValues();

                    cityType = vals.elementAt(3).toString();
                    vetmp = vec.getNextEntry();
                    ve.recycle();
                    ve = vetmp;
                    while (ve != null) {
                        vals = ve.getColumnValues();

                        if (!cityType.equals(vals.elementAt(3).toString())) {
                            equal = false;
                            break;
                        }

                        vetmp = vec.getNextEntry();
                        ve.recycle();
                        ve = vetmp;
                    }

                    if (equal) {
                        address.setCityType(cityType);
                    }
                }
            }

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (vetmp != null) {
                    vetmp.recycle();
                }
                if (ve != null) {
                    ve.recycle();
                }
                if (vec != null) {
                    vec.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }
    }
}
