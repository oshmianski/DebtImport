package by.oshmianski.objects.addressParser;

import by.oshmianski.objects.Address;
import by.oshmianski.objects.AliasValue;
import by.oshmianski.utils.MyLog;
import lotus.domino.Document;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vintselovich on 24.12.13.
 */
public class AddressParser {
    private View viewGEO;
    private View viewGEOStreet;

    private String realStr = "";
    private String realStrExclusion = "";
    private String realStr_ = "";
    private ArrayList<AddressParserItem> parserItems = new ArrayList<AddressParserItem>();
    private Address address = new Address();

    public AddressParser(String realStr, View viewGEO, View viewGEOStreet) {
        this.realStr = realStr.replaceAll("ё", "е").replaceAll("Ё", "Е");
        realStrExclusion = this.realStr;

        this.viewGEO = viewGEO;
        this.viewGEOStreet = viewGEOStreet;
    }

    public void parse() {
        processExclusion();

        if (realStrExclusion.isEmpty()) return;

        realStr_ = realStrExclusion.replaceAll("\\.", "_").replaceAll(",", "_").replaceAll(" ", "_");

        String[] addressArray = realStr_.split("_");
        int i = 1;
        int start = 0;

        for (String addr : addressArray) {
            if (!addr.isEmpty()) {
                AddressParserItem addressParserItem = new AddressParserItem(i, addr);

                if (Character.isDigit(addr.charAt(0))) {
                    addressParserItem.setBeginWithNumber(true);
                }

                parserItems.add(addressParserItem);
                i++;
            }
        }

        //PROCESS_OPERATION_1
        for (AddressParserItem item : parserItems) {
            if (isIndex(item.getText())) {
                item.setIndex(true);
                address.setIndex(item.getText(), AddressParserOperation.PROCESS_OPERATION_1);
                item.setProcessed(true, AddressParserOperation.PROCESS_OPERATION_1);
                item.setTypeValue(AddressParserItemTypeValue.index);
            }

            setTypeAndTypeValue(item);

            setCharAfter(item, start);
            setCharBefore(item, start);

            start = start + item.getText().length() + 1;  //+1 для _

        }

        if (parserItems.size() > 0) {
            if ("-1".equalsIgnoreCase(parserItems.get(0).getText()) || "0".equalsIgnoreCase(parserItems.get(0).getText())) {
                parserItems.get(0).setIndex(true);
                parserItems.get(0).setProcessed(true, AddressParserOperation.PROCESS_OPERATION_1);
            }
        }

        transpositionStreetTypeAndPartOfStreet();

        processServices();

        processCountry();

        processRegionDistrict();


        processHouseBuildFlat();

        processStreetOnly();

        processRestWithCityAndStreet();

        processCityAndStreetWithPrevAndNext();


        //проверяю, все ли обработано
        //это нужно сделать ТОЛЬКО В САМОМ КОНЦЕ,
        //т.е. после этого кода больше НИКАКОГО РАЗБОРА
        boolean isProcessedFullNotService = true;
        for (AddressParserItem item : parserItems) {
            if (!item.isService() && !item.isProcessed()) {
                isProcessedFullNotService = false;
                break;
            }
        }

        boolean isProcessedFull = true;
        for (AddressParserItem item : parserItems) {
            if (!item.isProcessed()) {
                isProcessedFull = false;
                break;
            }
        }

        //закрою неиспользованные сервисные слова
        if (isProcessedFullNotService && !isProcessedFull) {
            for (AddressParserItem item : parserItems) {
                if (item.isService() && !item.isProcessed()) {
                    item.setProcessed(true, AddressParserOperation.PROCESS_OPERATION_CLOSE_NOT_USED_SERVICE_9);
                    item.setProcessedWithoutValue(true);
                }
            }

//            isProcessedFull = true;
        }

        address.setProcessedFullNotService(isProcessedFullNotService);
        address.setProcessedFull(isProcessedFull);
    }

    public Address getAddress() {
        return address;
    }

    public ArrayList<AddressParserItem> getParserItems() {
        return parserItems;
    }

    private void processExclusion() {
        ArrayList<AliasValue> aliasValues = new ArrayList<AliasValue>();

        aliasValues.add(new AliasValue("г. н.п.", "нп."));     //населенный пункт
        aliasValues.add(new AliasValue("п.г.т.", "пгт."));  //поселок городского типа
        aliasValues.add(new AliasValue("н.п.", "нп."));     //населенный пункт
        aliasValues.add(new AliasValue("н.н.", "нп."));     //населенный пункт
        aliasValues.add(new AliasValue("н.а.", "нп."));     //населенный пункт
        aliasValues.add(new AliasValue("н. п.", "нп."));     //населенный пункт
        aliasValues.add(new AliasValue("Н.П.", "нп."));     //населенный пункт
        aliasValues.add(new AliasValue("н.п,", "нп.,"));     //населенный пункт
        aliasValues.add(new AliasValue(" н,п.", "нп."));     //населенный пункт
        aliasValues.add(new AliasValue("н\\п", "нп."));     //населенный пункт
        aliasValues.add(new AliasValue("н/п", "нп."));     //населенный пункт
        aliasValues.add(new AliasValue("н.п ", "нп."));     //населенный пункт
        aliasValues.add(new AliasValue("г.п.", "гп."));     //городской поселок
        aliasValues.add(new AliasValue("Г.П.", "гп."));     //городской поселок
        aliasValues.add(new AliasValue("к.п.", "кп."));     //курортный поселок
        aliasValues.add(new AliasValue("Б-р", "б-р"));
        aliasValues.add(new AliasValue("Ул.", "ул."));
        aliasValues.add(new AliasValue("ПГТ", "пгт"));
        aliasValues.add(new AliasValue("ПР-т", "пр-т"));
        aliasValues.add(new AliasValue("М-Н", "м-н"));
        aliasValues.add(new AliasValue("1ый", "1-ый"));
        aliasValues.add(new AliasValue("а.г.", "аг."));
        aliasValues.add(new AliasValue("в.ч.", "вч."));
        aliasValues.add(new AliasValue(" д.д.", " д. "));
        aliasValues.add(new AliasValue(" д.д ", " д. "));
        aliasValues.add(new AliasValue("ПЕР.", "пер."));

        aliasValues.add(new AliasValue("УЛИЦА", "улица"));
        aliasValues.add(new AliasValue("Школа-интернат", "школа-интернат"));
        aliasValues.add(new AliasValue("Проспект", "проспект"));
        aliasValues.add(new AliasValue("Область", "область"));
        aliasValues.add(new AliasValue("Бульвар", "бульвар"));
        aliasValues.add(new AliasValue("Пер.", "пер."));
        aliasValues.add(new AliasValue("дом д.", "д."));
        aliasValues.add(new AliasValue("газ.", "газеты"));
        aliasValues.add(new AliasValue("аг.гор.", "агрогородок "));
        aliasValues.add(new AliasValue("Республика Беларусь", "Беларусь"));
        aliasValues.add(new AliasValue("республика беларусь", "Беларусь"));
        aliasValues.add(new AliasValue("республика Беларусь", "Беларусь"));
        aliasValues.add(new AliasValue(" Гостиниц ", " Гостинец "));
        aliasValues.add(new AliasValue("Держинский", "Дзержинский"));
        aliasValues.add(new AliasValue("Молодеченский", "Молодечненский"));
        aliasValues.add(new AliasValue("Сморгоньский", "Сморгонский"));
        aliasValues.add(new AliasValue("Мозырьский", "Мозырский"));
        aliasValues.add(new AliasValue("Речецкий", "Речицкий"));
        aliasValues.add(new AliasValue("Волковыский", "Волковысский"));

        aliasValues.add(new AliasValue("улица неизвестна,", ""));
        aliasValues.add(new AliasValue("улицы нет,", ""));
        aliasValues.add(new AliasValue("Улицы нет,", ""));
        aliasValues.add(new AliasValue("неизвестна ", ""));
        aliasValues.add(new AliasValue("неизвестна,", ""));
        aliasValues.add(new AliasValue("неизвестно,", ""));
        aliasValues.add(new AliasValue("Не известна", ""));
        aliasValues.add(new AliasValue("Неизвестна,", ""));
        aliasValues.add(new AliasValue("НЕ ИЗВЕСТНА", ""));
        aliasValues.add(new AliasValue("не известна", ""));
        aliasValues.add(new AliasValue("не извесна", ""));
        aliasValues.add(new AliasValue("не  известна", ""));
        aliasValues.add(new AliasValue("г. Не известно", ""));
        aliasValues.add(new AliasValue("Не известно", ""));
        aliasValues.add(new AliasValue("не известно", ""));
        aliasValues.add(new AliasValue("не указана", ""));
        aliasValues.add(new AliasValue("неуказана", ""));
        aliasValues.add(new AliasValue("не указано", ""));
        aliasValues.add(new AliasValue("не указан", ""));
        aliasValues.add(new AliasValue("не указаны", ""));
        aliasValues.add(new AliasValue("без улицы", ""));
        aliasValues.add(new AliasValue("нет улицы", ""));
        aliasValues.add(new AliasValue("xxx", ""));
        aliasValues.add(new AliasValue(" нет,", ""));
        aliasValues.add(new AliasValue("корп. .,", ""));
        aliasValues.add(new AliasValue("корп.  .,", ""));
        aliasValues.add(new AliasValue("корп..,", ""));
        aliasValues.add(new AliasValue("корп.,", ""));
        aliasValues.add(new AliasValue("(частный дом)", ""));
        aliasValues.add(new AliasValue("(частный)", ""));
        aliasValues.add(new AliasValue("(част.)", ""));
        aliasValues.add(new AliasValue("частный дом", ""));
        aliasValues.add(new AliasValue("част дом", ""));
        aliasValues.add(new AliasValue("част.дом", ""));
        aliasValues.add(new AliasValue("част. дом", ""));
        aliasValues.add(new AliasValue(" -,", ""));
        aliasValues.add(new AliasValue(" нету,", ""));
        aliasValues.add(new AliasValue(" ,", ","));
        aliasValues.add(new AliasValue(".,", ","));
        aliasValues.add(new AliasValue("\"", ""));
        aliasValues.add(new AliasValue("70лет", "70 лет"));
        aliasValues.add(new AliasValue("60лет", "60 лет"));
        aliasValues.add(new AliasValue("50лет", "50 лет"));
        aliasValues.add(new AliasValue("40лет", "40 лет"));
        aliasValues.add(new AliasValue("30лет", "30 лет"));
        aliasValues.add(new AliasValue("20лет", "20 лет"));

        for (AliasValue aliasValue : aliasValues)
            if (realStrExclusion.toLowerCase().indexOf(aliasValue.getAlias().toLowerCase()) > -1) {
                realStrExclusion = realStrExclusion.replaceAll(
                        "(?i)" + aliasValue.getAlias().
                                replaceAll("\\.", "\\\\.").
                                replaceAll("\\(", "\\\\(").
                                replaceAll("\\)", "\\\\)"),
                        aliasValue.getValue());
            }
    }

    private boolean isIndex(String str) {
        Pattern pattern = Pattern.compile("\\d{6}");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    private void setTypeAndTypeValue(AddressParserItem addressParserItem) {
        AddressParserItem next;
        AliasValueType aliasValueType = new AliasValueType("", AddressParserItemTypeValue.UNKNOW, "");
        boolean isAliasValueType;

        for (AliasValueType aliasValue : AddressParserHelper.aliasValues) {
            isAliasValueType = false;

            if (addressParserItem.getText().equalsIgnoreCase(aliasValue.getAlias()) && !Character.isUpperCase(addressParserItem.getText().charAt(0))) {

                if (aliasValue.getTypeValue() == AddressParserItemTypeValue.UNKNOW) {
                    //дом
                    if ("д".equalsIgnoreCase(addressParserItem.getText())) {
                        next = getNextItem(addressParserItem);
                        if (next != null) {
                            if (next.isBeginWithNumber()) {
                                aliasValueType.setTypeValue(AddressParserItemTypeValue.house);
                                aliasValueType.setTypeValue2("дом");
                            } else {
                                aliasValueType.setTypeValue(AddressParserItemTypeValue.city);
                                aliasValueType.setTypeValue2("д");
                            }

                            isAliasValueType = true;
                        }
                    }

                    //квартира
                    if ("кв".equalsIgnoreCase(addressParserItem.getText())) {
                        next = getNextItem(addressParserItem);
                        if (next != null) {
                            if (next.isBeginWithNumber()) {
                                aliasValueType.setTypeValue(AddressParserItemTypeValue.flat);
                                aliasValueType.setTypeValue2("квартира");
                            } else {
                                aliasValueType.setTypeValue(AddressParserItemTypeValue.street);
                                aliasValueType.setTypeValue2("квартал");
                            }

                            isAliasValueType = true;
                        }
                    }
                }

                if (isAliasValueType) {
                    if (!isContainValueType(aliasValueType.getTypeValue())) {
                        addressParserItem.setType(AddressParserItemType.service);
                        addressParserItem.setTypeValue(aliasValueType.getTypeValue());
                        addressParserItem.setTypeValue2(aliasValueType.getTypeValue2());
                    }
                } else {
                    if (!isContainValueType(aliasValue.getTypeValue())) {
                        addressParserItem.setType(AddressParserItemType.service);
                        addressParserItem.setTypeValue(aliasValue.getTypeValue());
                        addressParserItem.setTypeValue2(aliasValue.getTypeValue2());
                    }
                }

                break;
            }
        }
    }

    private void setCharAfter(AddressParserItem addressParserItem, int start) {
        int index;

        index = realStrExclusion.indexOf(addressParserItem.getText(), start);
        if (realStrExclusion.length() > (index + addressParserItem.getText().length()))
            addressParserItem.setCharAfter(String.valueOf(realStrExclusion.charAt(index + addressParserItem.getText().length())));
    }

    private void setCharBefore(AddressParserItem addressParserItem, int start) {
        int index;

        index = realStrExclusion.indexOf(addressParserItem.getText(), start);
        if ((index - 1) > 0)
            addressParserItem.setCharBefore(String.valueOf(realStrExclusion.charAt(index - 1)));
    }

    public String getRealStr() {
        return realStr;
    }

    public String getRealStrExclusion() {
        return realStrExclusion;
    }

    public String getRealStr_() {
        return realStr_;
    }

    private AddressProcessCityWithGEOResult processCityWithGEO(String city) {
        AddressProcessCityWithGEOResult result = new AddressProcessCityWithGEOResult();

        ViewEntryCollection vec = null;
        String cityType = "";
        boolean equal = true;

        ViewEntryCollection vecDist = null;
        ViewEntry ve = null;
        ViewEntry vetmp = null;
        Document noteCity = null;

        try {
            vec = viewGEO.getAllEntriesByKey(city, true);

            if (vec.getCount() > 0) {
                if (vec.getCount() == 1) {
                    ve = vec.getFirstEntry();

                    noteCity = ve.getDocument();

                    result.setFind(true);
                    result.setCityType(noteCity.getItemValueString("cityType"));
                    result.setCity(noteCity.getItemValueString("title"));
                }

                if (vec.getCount() > 1) {
                    ve = vec.getFirstEntry();
                    if (noteCity != null) noteCity.recycle();
                    noteCity = ve.getDocument();

                    result.setFind(true);
                    result.setCity(noteCity.getItemValueString("title"));

                    if (!address.getDistrict().isEmpty()) {
                        Vector key = new Vector();
                        key.addElement(address.getCity());
                        key.addElement(address.getDistrict());
                        vecDist = viewGEO.getAllEntriesByKey(key, true);

                        if (vecDist.getCount() == 1) {
                            ve = vecDist.getFirstEntry();

                            if (noteCity != null) noteCity.recycle();
                            noteCity = ve.getDocument();

                            result.setCityType(noteCity.getItemValueString("cityType"));
                        }

                        if (vecDist.getCount() > 1) {
                            ve = vecDist.getFirstEntry();

                            if (noteCity != null) noteCity.recycle();
                            noteCity = ve.getDocument();

                            cityType = noteCity.getItemValueString("cityType");
                            vetmp = vecDist.getNextEntry();
                            ve.recycle();
                            ve = vetmp;
                            while (ve != null) {

                                if (noteCity != null) noteCity.recycle();
                                noteCity = ve.getDocument();

                                if (!cityType.equals(noteCity.getItemValueString("cityType"))) {
                                    equal = false;
                                    break;
                                }

                                vetmp = vecDist.getNextEntry();
                                ve.recycle();
                                ve = vetmp;
                            }

                            if (equal) {
                                result.setCityType(cityType);
                            }
                        }
                    } else {
                        ve = vec.getFirstEntry();

                        if (noteCity != null) noteCity.recycle();
                        noteCity = ve.getDocument();

                        cityType = noteCity.getItemValueString("cityType");
                        vetmp = vec.getNextEntry();
                        ve.recycle();
                        ve = vetmp;
                        while (ve != null) {

                            if (noteCity != null) noteCity.recycle();
                            noteCity = ve.getDocument();

                            if (!cityType.equals(noteCity.getItemValueString("cityType"))) {
                                equal = false;
                                break;
                            }

                            vetmp = vec.getNextEntry();
                            ve.recycle();
                            ve = vetmp;
                        }

                        if (equal) {
                            result.setCityType(cityType);
                        }
                    }
                }
            }
        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (noteCity != null) {
                    noteCity.recycle();
                }
                if (vetmp != null) {
                    vetmp.recycle();
                }
                if (ve != null) {
                    ve.recycle();
                }
                if (vecDist != null) {
                    vecDist.recycle();
                }
                if (vec != null) {
                    vec.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }

        return result;
    }

    private AddressProcessStreetWithGEOResult processStreetWithGEO(String streetTitle) {
        AddressProcessStreetWithGEOResult result = new AddressProcessStreetWithGEOResult();
        ViewEntry ve = null;
        Document note = null;

        try {
            ve = viewGEOStreet.getEntryByKey(streetTitle, true);

            if (ve != null) {
                note = ve.getDocument();
                result.setFind(true);
                result.setStreet(note.getItemValueString("title"));
            }

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (note != null) {
                    note.recycle();
                }
                if (ve != null) {
                    ve.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }

        return result;
    }

    private AddressProcessStreetResult processStreetNext(AddressParserItem item) {
        AddressProcessStreetWithGEOResult resultGEO;
        AddressProcessStreetResult result = new AddressProcessStreetResult();
        AddressParserItem next;
        String street = "";

        street = replaceStreetSuffix(item.getText());
        resultGEO = processStreetWithGEO(street);
        if (resultGEO.isFind()) {
            result.setFind(true);
            result.setStreet(resultGEO.getStreet());
        }
        result.getItems().add(item);

        if (!",".equals(item.getCharAfter())) {
            next = getNextItem(item);
            while (next != null && !next.isService() && !next.isProcessed()) {
                street = street + " " + replaceStreetSuffix(next.getText());
                resultGEO = processStreetWithGEO(street);
                if (resultGEO.isFind()) {
                    result.setFind(true);
                    result.setStreet(resultGEO.getStreet());
                } else {
                    result.setFind(false);
                }
                result.getItems().add(next);

                if (",".equals(next.getCharAfter())) {
                    break;
                }
                next = getNextItem(next);
            }
        }

        return result;
    }

    private AddressProcessStreetResult processStreetPrev(AddressParserItem item) {
        AddressProcessStreetWithGEOResult resultGEO;
        AddressProcessStreetResult result = new AddressProcessStreetResult();
        AddressParserItem prev;
        String street = "";

        street = replaceStreetSuffix(item.getText());
        resultGEO = processStreetWithGEO(street);
        if (resultGEO.isFind()) {
            result.setFind(true);
            result.setStreet(resultGEO.getStreet());
        }
        result.getItems().add(item);

        prev = getPrevItem(item);
        while (prev != null && !",".equals(prev.getCharAfter()) && !prev.isService() && !prev.isProcessed()) {

            street = replaceStreetSuffix(prev.getText()) + " " + street;
            resultGEO = processStreetWithGEO(street);
            if (resultGEO.isFind()) {
                result.setFind(true);
                result.setStreet(resultGEO.getStreet());
            } else {
                result.setFind(false);
            }
            result.getItems().add(prev);

            prev = getPrevItem(prev);
        }

        return result;
    }

    private String replaceStreetSuffix(String street) {
        return StringUtils.replaceEach(street,
                new String[]{
                        "-й", "-Й", "-ый", "-ЫЙ",
                        "-ая", "-АЯ", "-я", "-Я",
                        "-ое", "-ОЕ", "-ой", "-ОЙ",
                        "-ого", "-ОГО", "-го", "-ГО",
                        "-лет", "-ЛЕТ", "-летия", "-ЛЕТИЯ",
                        "-е", "-Е"},
                new String[]{
                        "", "", "", "",
                        "", "", "", "",
                        "", "", "", "",
                        "", "", "", "",
                        "", "", "", "",
                        "", ""});
    }

    private AddressProcessCityResult processCityNext(AddressParserItem item) {
        AddressProcessCityWithGEOResult resultGEO;
        AddressProcessCityResult result = new AddressProcessCityResult();
        AddressParserItem next;
        String city = "";

        city = item.getText();
        resultGEO = processCityWithGEO(city);
        if (resultGEO.isFind()) {
            result.setFind(true);
            result.setCity(resultGEO.getCity());
            result.setCityType(resultGEO.getCityType());
        }
        result.getItems().add(item);

        if (!",".equals(item.getCharAfter())) {
            next = getNextItem(item);
            while (next != null && !next.isService() && !next.isProcessed()) {
                city = city + " " + next.getText();
                resultGEO = processCityWithGEO(city);
                if (resultGEO.isFind()) {
                    result.setFind(true);
                    result.setCity(resultGEO.getCity());
                    result.setCityType(resultGEO.getCityType());
                } else {
                    result.setFind(false);
                }
                result.getItems().add(next);


                if (",".equals(next.getCharAfter())) {
                    break;
                }
                next = getNextItem(next);
            }
        }

        return result;
    }

    private AddressProcessCityResult processCityPrev(AddressParserItem item) {
        AddressProcessCityWithGEOResult resultGEO;
        AddressProcessCityResult result = new AddressProcessCityResult();
        AddressParserItem prev;
        String city = "";

        city = item.getText();
        resultGEO = processCityWithGEO(city);
        if (resultGEO.isFind()) {
            result.setFind(true);
            result.setCity(resultGEO.getCity());
            result.setCityType(resultGEO.getCityType());
        }
        result.getItems().add(item);

        prev = getPrevItem(item);
        while (prev != null && !",".equals(prev.getCharAfter()) && !prev.isService() && !prev.isProcessed()) {

            city = prev.getText() + " " + city;
            resultGEO = processCityWithGEO(city);
            if (resultGEO.isFind()) {
                result.setFind(true);
                result.setCity(resultGEO.getCity());
                result.setCityType(resultGEO.getCityType());
            } else {
                result.setFind(false);
            }
            result.getItems().add(prev);

            prev = getPrevItem(prev);
        }

        return result;
    }

    private AddressParserItem getPrevProcessedItem(AddressParserItem item) {
        AddressParserItem prev;
        AddressParserItem returnItem = null;

        prev = getPrevItem(item);

        while (prev != null) {

            if (prev.isProcessed()) {
                returnItem = prev;

                break;
            }

            prev = getPrevItem(prev);
        }

        return returnItem;
    }

    private AddressParserItem getNextProcessedItem(AddressParserItem item) {
        AddressParserItem next;
        AddressParserItem returnItem = null;

        next = getNextItem(item);

        while (next != null) {

            if (next.isProcessed()) {
                returnItem = next;

                break;
            }

            next = getNextItem(next);
        }

        return returnItem;
    }

    private AddressParserItem getPrevServiceItem(AddressParserItem item) {
        AddressParserItem prev;
        AddressParserItem returnItem = null;

        prev = getPrevItem(item);
        while (prev != null) {

            if (prev.isService()) {
                returnItem = prev;

                break;
            }

            prev = getPrevItem(prev);
        }

        return returnItem;
    }

    private AddressParserItem getNextServiceItem(AddressParserItem item) {
        AddressParserItem next;
        AddressParserItem returnItem = null;

        next = getNextItem(item);
        while (next != null) {

            if (next.isService()) {
                returnItem = next;

                break;
            }

            next = getNextItem(next);
        }

        return returnItem;
    }

    private AddressParserItem getPrevItem(AddressParserItem item) {
        AddressParserItem prev = null;

        if (item.getNumber() > 1) {
            prev = parserItems.get(item.getNumber() - 2);
        }

        return prev;
    }

    private AddressParserItem getNextItem(AddressParserItem item) {
        AddressParserItem next = null;

        if (parserItems.size() > item.getNumber()) {
            next = parserItems.get(item.getNumber());
        }

        return next;
    }

    private String clearDraft(String text) {
        return StringUtils.replaceEach(text,
                new String[]{
                        "общ", "общежитие",
                        "частный",
                        "-",
                        "нет",
                        "ч"},
                new String[]{
                        "", "",
                        "",
                        "",
                        "",
                        ""});
    }

    private boolean isContainValueType(AddressParserItemTypeValue valueType) {
        for (AddressParserItem item : parserItems) {
            if (item.getTypeValue() == valueType) {
                return true;
            }
        }

        return false;
    }

    private void processServices() {
        AddressProcessCityResult processCityResult;
        AddressProcessStreetResult processStreetResult;
        AddressProcessCityWithGEOResult processCityWithGEOResult;
        AddressProcessStreetWithGEOResult processStreetWithGEOResult;

        AddressParserItem next;
        AddressParserItem prev;

        //пробегусь по служебным типам с конца в начало
        for (int j = parserItems.size() - 1; j > -1; j--) {
            AddressParserItem item = parserItems.get(j);
            if (item.isService()) {
                //поиск области
                if (AddressParserItemTypeValue.region.equals(item.getTypeValue())) {
                    if (item.getNumber() > 1) {
                        prev = parserItems.get(item.getNumber() - 2);
                        if (!prev.isService())
                            if (AddressParserHelper.regions.contains(prev.getText().toLowerCase())) {
                                item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_REGION_2);
                                prev.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_REGION_2);
                                prev.setTypeValue(item.getTypeValue());

                                address.setRegion(prev.getText(), AddressParserOperation.PROCESS_SERVICES_REGION_2);
                            } else {
                                if (parserItems.size() > item.getNumber()) {
                                    next = parserItems.get(item.getNumber());
                                    if (!next.isService())
                                        if (AddressParserHelper.regions.contains(next.getText().toLowerCase())) {
                                            item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_REGION_2);
                                            next.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_REGION_2);
                                            next.setTypeValue(item.getTypeValue());

                                            address.setRegion(next.getText(), AddressParserOperation.PROCESS_SERVICES_REGION_2);
                                        }
                                }
                            }
                    }
                }

                //поиск района
                if (AddressParserItemTypeValue.district.equals(item.getTypeValue())) {
                    if (item.getNumber() > 1) {
                        prev = parserItems.get(item.getNumber() - 2);
                        if (!prev.isService())
                            if (AddressParserHelper.districts.contains(prev.getText().toLowerCase())) {
                                item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_DISTRICT_2);
                                prev.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_DISTRICT_2);
                                prev.setTypeValue(item.getTypeValue());

                                address.setDistrict(prev.getText(), AddressParserOperation.PROCESS_SERVICES_DISTRICT_2);
                            } else {
                                if (parserItems.size() > item.getNumber()) {
                                    next = parserItems.get(item.getNumber());
                                    if (!next.isService())
                                        if (AddressParserHelper.districts.contains(next.getText().toLowerCase())) {
                                            item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_DISTRICT_2);
                                            next.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_DISTRICT_2);
                                            next.setTypeValue(item.getTypeValue());

                                            address.setDistrict(next.getText(), AddressParserOperation.PROCESS_SERVICES_DISTRICT_2);
                                        }
                                }
                            }
                    }
                }

                //поиск города
                if (AddressParserItemTypeValue.city.equals(item.getTypeValue())) {
                    if (address.getCity().isEmpty()) {
                        next = getNextItem(item);
                        if (next != null && !next.isService() && !next.isProcessed() && !",".equalsIgnoreCase(item.getCharAfter())) {
                            processCityResult = processCityNext(next);

                            if (!processCityResult.isFind()) {
                                item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_CITY_NEXT_NOT_FOUND_2);
                                next.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_CITY_NEXT_NOT_FOUND_2);
                                next.setTypeValue(item.getTypeValue());

                                address.setCityType(item.getTypeValue2(), AddressParserOperation.PROCESS_SERVICES_CITY_NEXT_NOT_FOUND_2);
                                address.setCity(next.getText(), AddressParserOperation.PROCESS_SERVICES_CITY_NEXT_NOT_FOUND_2);
                            } else {
                                item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_CITY_NEXT_FOUND_2);

                                address.setCityType(item.getTypeValue2().isEmpty() ? processCityResult.getCityType() : item.getTypeValue2(), AddressParserOperation.PROCESS_SERVICES_CITY_NEXT_FOUND_2);
                                address.setCity(processCityResult.getCity(), AddressParserOperation.PROCESS_SERVICES_CITY_NEXT_FOUND_2);
                                processCityResult.processAllItems(AddressParserOperation.PROCESS_SERVICES_CITY_NEXT_FOUND_2);
                            }
                        }
                    }

                    if (address.getCity().isEmpty()) {
                        prev = getPrevItem(item);
                        if (prev != null && !prev.isService() && !prev.isProcessed() && !",".equalsIgnoreCase(prev.getCharAfter())) {
                            processCityResult = processCityPrev(prev);

                            if (!processCityResult.isFind()) {
                                item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_CITY_PREV_NOT_FOUND_2);
                                prev.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_CITY_PREV_NOT_FOUND_2);
                                prev.setTypeValue(item.getTypeValue());

                                address.setCityType(item.getTypeValue2(), AddressParserOperation.PROCESS_SERVICES_CITY_PREV_NOT_FOUND_2);
                                address.setCity(prev.getText(), AddressParserOperation.PROCESS_SERVICES_CITY_PREV_NOT_FOUND_2);
                            } else {
                                item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_CITY_PREV_FOUND_2);

                                address.setCityType(item.getTypeValue2().isEmpty() ? processCityResult.getCityType() : item.getTypeValue2(), AddressParserOperation.PROCESS_SERVICES_CITY_PREV_FOUND_2);
                                address.setCity(processCityResult.getCity(), AddressParserOperation.PROCESS_SERVICES_CITY_PREV_FOUND_2);
                                processCityResult.processAllItems(AddressParserOperation.PROCESS_SERVICES_CITY_PREV_FOUND_2);
                            }
                        }
                    }
                }

                //поиск улицы
                if (AddressParserItemTypeValue.street.equals(item.getTypeValue())) {
                    //ищу после служебного слова
                    if (address.getStreet().isEmpty()) {
                        next = getNextItem(item);
                        if (next != null && !next.isService() && !next.isProcessed() && !",".equals(item.getCharAfter())) {
                            processStreetResult = processStreetNext(next);

                            if (!processStreetResult.isFind()) {//если таки не нашли улицу, то присваиваю только первое следующее слово
                                item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_STREET_NEXT_NOT_FOUND_2);
                                next.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_STREET_NEXT_NOT_FOUND_2);
                                next.setTypeValue(AddressParserItemTypeValue.street);

                                address.setStreetType(item.getTypeValue2(), AddressParserOperation.PROCESS_SERVICES_STREET_NEXT_NOT_FOUND_2);
                                address.setStreet(next.getText(), AddressParserOperation.PROCESS_SERVICES_STREET_NEXT_NOT_FOUND_2);
                            } else {
                                item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_STREET_NEXT_FOUND_2);

                                address.setStreetType(item.getTypeValue2(), AddressParserOperation.PROCESS_SERVICES_STREET_NEXT_FOUND_2);
                                address.setStreet(processStreetResult.getStreet(), AddressParserOperation.PROCESS_SERVICES_STREET_NEXT_FOUND_2);
                                processStreetResult.processAllItems(AddressParserOperation.PROCESS_SERVICES_STREET_NEXT_FOUND_2);
                            }
                        }
                    }

                    //ищу до служебного слова
                    if (address.getStreet().isEmpty()) {
                        prev = getPrevItem(item);
                        if (prev != null && !prev.isService() && !prev.isProcessed() && !",".equalsIgnoreCase(prev.getCharAfter())) {
                            processStreetResult = processStreetPrev(prev);

                            if (!processStreetResult.isFind()) {//если таки не нашли улицу, то присваиваю только первое предыдущее слово
                                item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_STREET_PREV_NOT_FOUND_2);
                                prev.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_STREET_PREV_NOT_FOUND_2);
                                prev.setTypeValue(AddressParserItemTypeValue.street);

                                address.setStreetType(item.getTypeValue2(), AddressParserOperation.PROCESS_SERVICES_STREET_PREV_NOT_FOUND_2);
                                address.setStreet(prev.getText(), AddressParserOperation.PROCESS_SERVICES_STREET_PREV_NOT_FOUND_2);
                            } else {
                                item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_STREET_PREV_FOUND_2);

                                address.setStreetType(item.getTypeValue2(), AddressParserOperation.PROCESS_SERVICES_STREET_PREV_FOUND_2);
                                address.setStreet(processStreetResult.getStreet(), AddressParserOperation.PROCESS_SERVICES_STREET_PREV_FOUND_2);
                                processStreetResult.processAllItems(AddressParserOperation.PROCESS_SERVICES_STREET_PREV_FOUND_2);
                            }
                        }
                    }
                }

                //поиск дома
                if (AddressParserItemTypeValue.house.equals(item.getTypeValue())) {
                    if (parserItems.size() > item.getNumber()) {
                        next = parserItems.get(item.getNumber());
                        if (!next.isService()) {
                            item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_HOUSE_FOUND_2);
                            next.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_HOUSE_FOUND_2);
                            next.setTypeValue(AddressParserItemTypeValue.house);

                            address.setHouse(next.getText(), AddressParserOperation.PROCESS_SERVICES_HOUSE_FOUND_2);
                        }
                    }
                }

                String build = "";
                //поиск корпуса
                if (AddressParserItemTypeValue.build.equals(item.getTypeValue())) {
                    if (parserItems.size() > item.getNumber()) {
                        next = parserItems.get(item.getNumber());
                        if (!next.isService()) {
                            build = clearDraft(next.getText());

                            item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_BUILD_FOUND_2);
                            next.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_BUILD_FOUND_2);
                            next.setTypeValue(AddressParserItemTypeValue.build);

                            if (!build.isEmpty()) {
                                address.setBuilding(next.getText(), AddressParserOperation.PROCESS_SERVICES_BUILD_FOUND_2);
                            }
                        }
                    }
                }

                //поиск квартиры
                if (AddressParserItemTypeValue.flat.equals(item.getTypeValue())) {
                    if (parserItems.size() > item.getNumber()) {
                        next = parserItems.get(item.getNumber());
                        if (!next.isService()) {
                            item.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_FLAT_FOUND_2);
                            next.setProcessed(true, AddressParserOperation.PROCESS_SERVICES_FLAT_FOUND_2);
                            next.setTypeValue(AddressParserItemTypeValue.flat);

                            address.setFlat(next.getText(), AddressParserOperation.PROCESS_SERVICES_FLAT_FOUND_2);
                        }
                    }
                }
            }
        }
    }

    private void processHouseBuildFlat() {
        AddressParserItem next;
        AddressParserItem prev;
        ArrayList<AddressParserItem> items = new ArrayList<AddressParserItem>();
        boolean isCanHouseBuildFlat;

        //пытаюсь найти дом, корпус и квартиру
        for (AddressParserItem item : parserItems) {
            isCanHouseBuildFlat = true;

            if (!item.isService() && !item.isProcessed() && item.isBeginWithNumber()) {

                next = getNextProcessedItem(item);

                if (next != null &&
                        (
                                next.getTypeValue() == AddressParserItemTypeValue.index ||
                                        next.getTypeValue() == AddressParserItemTypeValue.country ||
                                        next.getTypeValue() == AddressParserItemTypeValue.region ||
                                        next.getTypeValue() == AddressParserItemTypeValue.district ||
                                        next.getTypeValue() == AddressParserItemTypeValue.city ||
                                        next.getTypeValue() == AddressParserItemTypeValue.street ||
                                        next.getTypeValue() == AddressParserItemTypeValue.house
                        )) {
                    isCanHouseBuildFlat = false;
                }

                if (isCanHouseBuildFlat) {

                    next = getNextServiceItem(item);

                    if (next != null &&
                            (
                                    next.getTypeValue() == AddressParserItemTypeValue.index ||
                                            next.getTypeValue() == AddressParserItemTypeValue.country ||
                                            next.getTypeValue() == AddressParserItemTypeValue.region ||
                                            next.getTypeValue() == AddressParserItemTypeValue.district ||
                                            next.getTypeValue() == AddressParserItemTypeValue.city ||
                                            next.getTypeValue() == AddressParserItemTypeValue.street ||
                                            next.getTypeValue() == AddressParserItemTypeValue.house
                            )) {
                        isCanHouseBuildFlat = false;
                    }
                }

                if (isCanHouseBuildFlat) {
                    items.add(item);
                }
            }
        }

        for (AddressParserItem item : items) {
            prev = getPrevProcessedItem(item);
            if (prev != null) {
                if (prev.getTypeValue() == AddressParserItemTypeValue.street
                        || (prev.getTypeValue() == AddressParserItemTypeValue.city && !isContainValueType(AddressParserItemTypeValue.street))) {
                    item.setProcessed(true, AddressParserOperation.PROCESS_HOUSE_BUILD_FLAT_5);
                    item.setTypeValue(AddressParserItemTypeValue.house);

                    address.setHouse(item.getText(), AddressParserOperation.PROCESS_HOUSE_BUILD_FLAT_5);
                }

                if (prev.getTypeValue() == AddressParserItemTypeValue.house) {
                    if (items.size() == items.indexOf(item) + 1) {
                        item.setTypeValue(AddressParserItemTypeValue.flat);
                        address.setFlat(item.getText(), AddressParserOperation.PROCESS_HOUSE_BUILD_FLAT_5);
                    } else {
                        item.setTypeValue(AddressParserItemTypeValue.build);
                        address.setBuilding(item.getText(), AddressParserOperation.PROCESS_HOUSE_BUILD_FLAT_5);
                    }

                    item.setProcessed(true, AddressParserOperation.PROCESS_HOUSE_BUILD_FLAT_5);
                }

                if (prev.getTypeValue() == AddressParserItemTypeValue.build) {
                    item.setProcessed(true, AddressParserOperation.PROCESS_HOUSE_BUILD_FLAT_5);
                    item.setTypeValue(AddressParserItemTypeValue.flat);

                    address.setFlat(item.getText(), AddressParserOperation.PROCESS_HOUSE_BUILD_FLAT_5);
                }
            }
        }

        String build = "";
        //пробую обработать корпуса
        for (AddressParserItem item : parserItems) {
            if (!item.isService() && !item.isProcessed()) {
                prev = getPrevProcessedItem(item);
                if (prev != null) {
                    if (prev.getTypeValue() == AddressParserItemTypeValue.house) {
                        build = clearDraft(item.getText());

                        if (!build.isEmpty()) {
                            item.setTypeValue(AddressParserItemTypeValue.build);

                            if (address.getBuilding().isEmpty()) {
                                address.setBuilding(item.getText(), AddressParserOperation.PROCESS_HOUSE_BUILD_FLAT_5);
                            }
                        }

                        item.setProcessed(true, AddressParserOperation.PROCESS_HOUSE_BUILD_FLAT_5);
                    }
                }
            }
        }
    }

    private void processRegionDistrict() {
        AddressParserItem prev;

        boolean isCanRegion;
        boolean isCanDistrict;

        //область и район
        for (AddressParserItem item : parserItems) {
            isCanRegion = true;
            isCanDistrict = true;

            if (!item.isService() && !item.isProcessed()) {
                if (AddressParserHelper.regions.contains(item.getText().toLowerCase())) {
                    if (address.getRegion().isEmpty()) {
                        prev = getPrevProcessedItem(item);

                        if (prev != null &&
                                (
                                        prev.getTypeValue() == AddressParserItemTypeValue.district ||
                                                prev.getTypeValue() == AddressParserItemTypeValue.city ||
                                                prev.getTypeValue() == AddressParserItemTypeValue.street ||
                                                prev.getTypeValue() == AddressParserItemTypeValue.house ||
                                                prev.getTypeValue() == AddressParserItemTypeValue.build ||
                                                prev.getTypeValue() == AddressParserItemTypeValue.flat
                                )) {
                            isCanRegion = false;
                        }

                        if (isCanRegion) {
                            address.setRegion(item.getText(), AddressParserOperation.PROCESS_REGION_DISTRICT_4);
                            item.setProcessed(true, AddressParserOperation.PROCESS_REGION_DISTRICT_4);
                            item.setTypeValue(AddressParserItemTypeValue.region);
                        }
                    }

                    continue;
                }

                if (AddressParserHelper.districts.contains(item.getText().toLowerCase())) {
                    if (address.getDistrict().isEmpty()) {
                        prev = getPrevProcessedItem(item);

                        if (prev != null &&
                                (
                                        prev.getTypeValue() == AddressParserItemTypeValue.city ||
                                                prev.getTypeValue() == AddressParserItemTypeValue.street ||
                                                prev.getTypeValue() == AddressParserItemTypeValue.house ||
                                                prev.getTypeValue() == AddressParserItemTypeValue.build ||
                                                prev.getTypeValue() == AddressParserItemTypeValue.flat
                                )) {
                            isCanDistrict = false;
                        }

                        if (isCanDistrict) {
                            address.setDistrict(item.getText(), AddressParserOperation.PROCESS_REGION_DISTRICT_4);
                            item.setProcessed(true, AddressParserOperation.PROCESS_REGION_DISTRICT_4);
                            item.setTypeValue(AddressParserItemTypeValue.district);
                        }
                    }

                    continue;
                }
            }
        }
    }

    private void processCountry() {
        AddressProcessCityResult processCityResult;
        AddressProcessCityWithGEOResult processCityWithGEOResult;

        boolean isBelarus = false;
        AddressParserItem itemBelarus = null;

        //сначала поищу все н.п., кроме Беларусь
        //потому что это может быть и н.п. и страна
        for (AddressParserItem item : parserItems) {
            if (!item.isService() && !item.isProcessed()) {
                if (!"Беларусь".equalsIgnoreCase(item.getText())) {
                    if (address.getCity().isEmpty()) {
                        processCityResult = processCityNext(item);
                        if (processCityResult.isFind()) {
                            address.setCityType(processCityResult.getCityType(), AddressParserOperation.PROCESS_COUNTRY_NOT_3);
                            address.setCity(processCityResult.getCity(), AddressParserOperation.PROCESS_COUNTRY_NOT_3);
                            processCityResult.processAllItems(AddressParserOperation.PROCESS_COUNTRY_NOT_3);
                        }
                    }
                } else {
                    isBelarus = true;
                    itemBelarus = item;
                }

                if ("РБ".equalsIgnoreCase(item.getText())) {
                    address.setCountry("Беларусь", AddressParserOperation.PROCESS_COUNTRY_NOT_3);
                    item.setProcessed(true, AddressParserOperation.PROCESS_COUNTRY_NOT_3);
                    item.setTypeValue(AddressParserItemTypeValue.country);
                }
            }
        }

        //а теперь, если город все еще пуст и было указано Беларусь, то считаю, что это н.п.
        if (isBelarus) {
            if (address.getCity().isEmpty()) {
                processCityWithGEOResult = processCityWithGEO(itemBelarus.getText());
                if (processCityWithGEOResult.isFind()) {
                    address.setCity(processCityWithGEOResult.getCity(), AddressParserOperation.PROCESS_COUNTRY_3);
                    address.setCityType(processCityWithGEOResult.getCityType(), AddressParserOperation.PROCESS_COUNTRY_3);
                } else {
                    address.setCity(itemBelarus.getText(), AddressParserOperation.PROCESS_COUNTRY_3);
                }
            } else {
                if (address.getCountry().isEmpty()) {
                    address.setCountry(itemBelarus.getText().trim(), AddressParserOperation.PROCESS_COUNTRY_3);
                    itemBelarus.setTypeValue(AddressParserItemTypeValue.country);
                }
            }

            itemBelarus.setProcessed(true, AddressParserOperation.PROCESS_COUNTRY_3);
        }
    }

    private void processStreetOnly() {
        AddressParserItem next;
        AddressParserItem prev;
        AddressProcessStreetResult processStreetResult;

        if (!address.getStreet().isEmpty()) {
            return;
        }

        boolean isCanBeStreet;
        //пытаюсь найти улицы
        for (AddressParserItem item : parserItems) {
            isCanBeStreet = true;
            if (!item.isService() && !item.isProcessed()) {
                if (address.getStreet().isEmpty()) {
                    processStreetResult = processStreetNext(item);

                    if (processStreetResult.isFind()) {
                        next = getNextItem(item);
                        if (next != null &&
                                (
                                        next.getTypeValue() == AddressParserItemTypeValue.index ||
                                                next.getTypeValue() == AddressParserItemTypeValue.country ||
                                                next.getTypeValue() == AddressParserItemTypeValue.region ||
                                                next.getTypeValue() == AddressParserItemTypeValue.district ||
                                                next.getTypeValue() == AddressParserItemTypeValue.city
                                )
                                ) {
                            isCanBeStreet = false;
                        }

                        prev = getPrevItem(item);
                        if (prev != null &&
                                (
                                        prev.getTypeValue() == AddressParserItemTypeValue.house ||
                                                prev.getTypeValue() == AddressParserItemTypeValue.build ||
                                                prev.getTypeValue() == AddressParserItemTypeValue.house
                                )
                                ) {
                            isCanBeStreet = false;
                        }

                        if (isCanBeStreet) {
                            address.setStreet(processStreetResult.getStreet(), AddressParserOperation.PROCESS_STREET_ONLY_6);
                            processStreetResult.processAllItems(AddressParserOperation.PROCESS_STREET_ONLY_6);
                        }

                    }
                }
            }
        }
    }

    private void transpositionStreetTypeAndPartOfStreet() {
        //пытаюсь найти улицы путем перестановки слов, начинающихся с цифры,
        //которые находятся перед "," и после них служебное слово с типом = улица
        //это нужно ОБЯЗАТЕЛЬНО ПЕРЕД последующим разбором улиц
        //todo:переставить местами

        AddressParserItem item4Transposition = new AddressParserItem(0, "");
        AddressParserItem prev;
        AddressParserItem next;

        for (AddressParserItem item : parserItems) {
            if (!item.isService() && !item.isProcessed() && item.isBeginWithNumber()) {
                prev = getPrevItem(item);
                next = getNextItem(item);
                if(
                        prev != null &&
                                next != null &&
                                ",".equalsIgnoreCase(prev.getCharAfter()) &&
                                item.isBeginWithNumber() &&
                                next.isService() &&
                                next.getTypeValue() == AddressParserItemTypeValue.street){

                    item4Transposition.setNumber(next.getNumber());
                    item4Transposition.setIndex(next.isIndex());
                    item4Transposition.setCharBefore(next.getCharBefore());
                    item4Transposition.setCharAfter(next.getCharBefore());
                    item4Transposition.setText(next.getText());
                    item4Transposition.setType(next.getType());
                    item4Transposition.setTypeValue(next.getTypeValue());
                    item4Transposition.setTypeValue2(next.getTypeValue2());
                    item4Transposition.setBeginWithNumber(next.isBeginWithNumber());
                    item4Transposition.setProcessed(next.isProcessed());
                    item4Transposition.setProcessedWithoutValue(next.isProcessedWithoutValue());
                    item4Transposition.setOperation(next.getOperation());
                    item4Transposition.setFoundInCategory(next.getFoundInCategory());

                    next.setText(item.getText());
                    next.setType(item.getType());
                    next.setTypeValue(item.getTypeValue());
                    next.setTypeValue2(item.getTypeValue2());
                    next.setBeginWithNumber(item.isBeginWithNumber());
                    next.setProcessed(item.isProcessed());
                    next.setProcessedWithoutValue(item.isProcessedWithoutValue());
                    next.setOperation(item.getOperation());
                    next.setFoundInCategory(item.getFoundInCategory());

                    item.setText(item4Transposition.getText());
                    item.setType(item4Transposition.getType());
                    item.setTypeValue(item4Transposition.getTypeValue());
                    item.setTypeValue2(item4Transposition.getTypeValue2());
                    item.setBeginWithNumber(item4Transposition.isBeginWithNumber());
                    item.setProcessed(item4Transposition.isProcessed());
                    item.setProcessedWithoutValue(item4Transposition.isProcessedWithoutValue());
                    item.setOperation(item4Transposition.getOperation());
                    item.setFoundInCategory(item4Transposition.getFoundInCategory());
                }
            }
        }
    }

    private void processRestWithCityAndStreet() {
        AddressProcessCityResult processCityResult;
        AddressProcessStreetResult processStreetResult;
        AddressProcessCityWithGEOResult processCityWithGEOResult;
        AddressProcessStreetWithGEOResult processStreetWithGEOResult;

        AddressParserItem prev;
        AddressParserItem next;

        String city = "";
        String street = "";

        //пытаюсь разобрать оставшиеся слова (только в разрезе Н.П. и улицы)
        for (AddressParserItem item : parserItems) {
            if (!item.isService() && !item.isProcessed()) {
                prev = getPrevItem(item);
                if (prev != null && !prev.isService()) {
                    if (prev.getTypeValue() == AddressParserItemTypeValue.city) {
                        if (address.getCity().isEmpty()) {
                            city = prev.getText() + " " + item.getText();

                            processCityWithGEOResult = processCityWithGEO(city);
                            if (processCityWithGEOResult.isFind()) {
                                item.setProcessed(true, AddressParserOperation.PROCESS_REST_CITY_STREET_PREV_7);
                                item.setTypeValue(prev.getTypeValue());

                                address.setCity(city, AddressParserOperation.PROCESS_REST_CITY_STREET_PREV_7);
                            }
                        }
                    }

                    if (prev.getTypeValue() == AddressParserItemTypeValue.street) {
                        if (address.getStreet().isEmpty()) {
                            street = prev.getText() + " " + item.getText();
                            processStreetWithGEOResult = processStreetWithGEO(street);
                            if (processStreetWithGEOResult.isFind()) {
                                item.setProcessed(true, AddressParserOperation.PROCESS_REST_CITY_STREET_PREV_7);
                                item.setTypeValue(prev.getTypeValue());

                                address.setStreet(processStreetWithGEOResult.getStreet(), AddressParserOperation.PROCESS_REST_CITY_STREET_PREV_7);
                            }
                        }
                    }
                }

                if (!item.isProcessed()) {//если все еще не разобран, то беру следующие за ним слова
                    next = getNextItem(item);
                    if (next != null && !next.isService()) {
                        if (next.getTypeValue() == AddressParserItemTypeValue.city) {
                            if (address.getCity().isEmpty()) {
                                processCityResult = processCityNext(item);
                                if (processCityResult.isFind()) {
                                    address.setCity(processCityResult.getCity(), AddressParserOperation.PROCESS_REST_CITY_STREET_NEXT_7);
                                    address.setCityType(processCityResult.getCityType(), AddressParserOperation.PROCESS_REST_CITY_STREET_NEXT_7);
                                    processCityResult.processAllItems(AddressParserOperation.PROCESS_REST_CITY_STREET_NEXT_7);
                                }
                            }
                        }
                        if (next.getTypeValue() == AddressParserItemTypeValue.street) {
                            if (address.getStreet().isEmpty()) {
                                processStreetResult = processStreetNext(item);
                                if (processStreetResult.isFind()) {
                                    address.setStreet(processStreetResult.getStreet(), AddressParserOperation.PROCESS_REST_CITY_STREET_NEXT_7);
                                    processStreetResult.processAllItems(AddressParserOperation.PROCESS_REST_CITY_STREET_NEXT_7);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void processCityAndStreetWithPrevAndNext() {
        AddressParserItem prev;
        AddressParserItem next;

        //пытаюсь найти улицы и города, учитывая предыдущие и следующие значения
        for (AddressParserItem item : parserItems) {
            if (!item.isService() && !item.isProcessed()) {
                if (address.getStreet().isEmpty()) {
                    prev = getPrevItem(item);
                    next = getNextItem(item);

                    if (prev != null && next != null && prev.getTypeValue() == AddressParserItemTypeValue.city && next.getTypeValue() == AddressParserItemTypeValue.house) {
                        address.setStreet(item.getText(), AddressParserOperation.PROCESS_CITY_STREET_WITH_PREV_AND_NEXT_8);
                        item.setProcessed(true, AddressParserOperation.PROCESS_CITY_STREET_WITH_PREV_AND_NEXT_8);
                        item.setTypeValue(AddressParserItemTypeValue.street);
                    }
                }

                if (address.getCity().isEmpty()) {
                    prev = getPrevItem(item);
                    next = getNextItem(item);

                    if (prev != null && next != null && prev.getTypeValue() == AddressParserItemTypeValue.district && next.getTypeValue() == AddressParserItemTypeValue.street) {
                        address.setCity(item.getText(), AddressParserOperation.PROCESS_CITY_STREET_WITH_PREV_AND_NEXT_8);
                        item.setProcessed(true, AddressParserOperation.PROCESS_CITY_STREET_WITH_PREV_AND_NEXT_8);
                        item.setTypeValue(AddressParserItemTypeValue.city);
                    }
                }
            }
        }
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}