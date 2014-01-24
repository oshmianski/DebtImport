package by.oshmianski.objects.addressParser;

import by.oshmianski.objects.Address;
import by.oshmianski.objects.AliasValue;
import by.oshmianski.utils.MyLog;
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

        realStr_ = realStrExclusion.replaceAll("\\.", "_").replaceAll(",", "_").replaceAll(" ", "_");

        String[] addressArray = realStr_.split("_");
        int i = 1;
        int start = 0;
        for (String addr : addressArray) {
            if (!addr.isEmpty() && !"-".equals(addr)) {
                AddressParserItem addressParserItem = new AddressParserItem(i, addr);
                if (isIndex(addr)) {
                    addressParserItem.setIndex(true);
                    address.setIndex(addr);
                    addressParserItem.setProcessed(true);
                }
                if (Character.isDigit(addr.charAt(0))) addressParserItem.setBeginWithNumber(true);

                setTypeAndTypeValue(addressParserItem);
                setCharAfter(addressParserItem, start);
                setCharBefore(addressParserItem, start);

                parserItems.add(addressParserItem);
                i++;
                start = start + addr.length() + 1;  //+1 для _
            }
        }

        AddressParserItem prev;
        AddressParserItem next;

        //пытаюсь разрешить UNKNOW
        for (AddressParserItem item : parserItems) {
            if (item.getTypeValue() == AddressParserItemTypeValue.UNKNOW) {
                //дом
                if ("д".equalsIgnoreCase(item.getText())) {
                    if (parserItems.size() > item.getNumber()) {
                        next = parserItems.get(item.getNumber());
                        if (next.isBeginWithNumber()) {
                            item.setTypeValue(AddressParserItemTypeValue.house);
                            item.setTypeValue2("дом");
                        } else {
                            item.setTypeValue(AddressParserItemTypeValue.city);
                            item.setTypeValue2("д");
                        }
                    }
                }

                //квартира
                if ("кв".equalsIgnoreCase(item.getText())) {
                    if (parserItems.size() > item.getNumber()) {
                        next = parserItems.get(item.getNumber());
                        if (next.isBeginWithNumber()) {
                            item.setTypeValue(AddressParserItemTypeValue.flat);
                            item.setTypeValue2("квартира");
                        } else {
                            item.setTypeValue(AddressParserItemTypeValue.street);
                            item.setTypeValue2("квартал");
                        }
                    }
                }
            }
        }

        //пробегусь по служебным типам
        for (AddressParserItem item : parserItems) {
            if (item.isService()) {
                //поиск области
                if (AddressParserItemTypeValue.region.equals(item.getTypeValue())) {
                    if (item.getNumber() > 0) {
                        prev = parserItems.get(item.getNumber() - 2);
                        if (!prev.isService())
                            if (AddressParserHelper.regions.contains(prev.getText().toLowerCase())) {
                                item.setProcessed(true);
                                prev.setProcessed(true);

                                address.setRegion(prev.getText());
                            } else {
                                if (parserItems.size() > item.getNumber()) {
                                    next = parserItems.get(item.getNumber());
                                    if (!next.isService())
                                        if (AddressParserHelper.regions.contains(next.getText().toLowerCase())) {
                                            item.setProcessed(true);
                                            next.setProcessed(true);

                                            address.setRegion(next.getText());
                                        }
                                }
                            }
                    }
                }

                //поиск района
                if (AddressParserItemTypeValue.district.equals(item.getTypeValue())) {
                    if (item.getNumber() > 0) {
                        prev = parserItems.get(item.getNumber() - 2);
                        if (!prev.isService())
                            if (AddressParserHelper.districts.contains(prev.getText().toLowerCase())) {
                                item.setProcessed(true);
                                prev.setProcessed(true);

                                address.setDistrict(prev.getText());
                            } else {
                                if (parserItems.size() > item.getNumber()) {
                                    next = parserItems.get(item.getNumber());
                                    if (!next.isService())
                                        if (AddressParserHelper.districts.contains(next.getText().toLowerCase())) {
                                            item.setProcessed(true);
                                            next.setProcessed(true);

                                            address.setDistrict(next.getText());
                                        }
                                }
                            }
                    }
                }

                //поиск города
                if (AddressParserItemTypeValue.city.equals(item.getTypeValue())) {
                    if (address.getCity().isEmpty())
                        if (parserItems.size() > item.getNumber()) {
                            next = parserItems.get(item.getNumber());
                            if (!next.isService() && !next.isProcessed()) {
                                processCityNext(next);

                                if (address.getCity().isEmpty()) {
                                    item.setProcessed(true);
                                    next.setProcessed(true);

                                    address.setCityType(item.getTypeValue2());
                                    address.setCity(next.getText());
                                } else {
                                    if (address.getCityType().isEmpty()) address.setCityType(item.getTypeValue2());
                                    item.setProcessed(true);
                                }
                            }
                        }

                    if (address.getCity().isEmpty())
                        if (item.getNumber() > 0) {
                            prev = parserItems.get(item.getNumber() - 2);
                            if (!prev.isService() && !prev.isProcessed()) {
                                processCityPrev(prev);

                                if (address.getCity().isEmpty()) {
                                    item.setProcessed(true);
                                    prev.setProcessed(true);

                                    address.setCityType(item.getTypeValue2());
                                    address.setCity(prev.getText());
                                } else {
                                    if (address.getCityType().isEmpty()) address.setCityType(item.getTypeValue2());
                                    item.setProcessed(true);
                                }
                            }
                        }
                }

                //поиск улицы
                if (AddressParserItemTypeValue.street.equals(item.getTypeValue())) {
                    //ищу после служебного слова
                    if (address.getStreet().isEmpty())
                        if (parserItems.size() > item.getNumber()) {
                            next = parserItems.get(item.getNumber());
                            if (!next.isService() && !next.isProcessed() && !",".equals(item.getCharAfter())) {
                                processStreetNext(next);

                                if (address.getStreet().isEmpty()) {//если таки не нашли улицу, то присваиваю только первое следующее слово
                                    item.setProcessed(true);
                                    next.setProcessed(true);

                                    address.setStreetType(item.getTypeValue2());
                                    address.setStreet(next.getText());
                                } else {
                                    address.setStreetType(item.getTypeValue2());
                                    item.setProcessed(true);
                                }
                            }
                        }

                    //ищу до служебного слова
                    if (address.getStreet().isEmpty())
                        if (item.getNumber() > 0) {
                            prev = parserItems.get(item.getNumber() - 2);
                            if (!prev.isService() && !prev.isProcessed()) {
                                processStreetPrev(prev);

                                if (address.getStreet().isEmpty()) {//если таки не нашли улицу, то присваиваю только первое предыдущее слово
                                    item.setProcessed(true);
                                    prev.setProcessed(true);

                                    address.setStreetType(item.getTypeValue2());
                                    address.setStreet(prev.getText());
                                } else {
                                    address.setStreetType(item.getTypeValue2());
                                    item.setProcessed(true);
                                }
                            }
                        }
                }

                //поиск дома
                if (AddressParserItemTypeValue.house.equals(item.getTypeValue())) {
                    if (parserItems.size() > item.getNumber()) {
                        next = parserItems.get(item.getNumber());
                        if (!next.isService()) {
                            item.setProcessed(true);
                            next.setProcessed(true);

                            address.setHouse(next.getText());
                        }
                    }
                }

                //поиск корпуса
                if (AddressParserItemTypeValue.build.equals(item.getTypeValue())) {
                    if (parserItems.size() > item.getNumber()) {
                        next = parserItems.get(item.getNumber());
                        if (!next.isService()) {
                            item.setProcessed(true);
                            next.setProcessed(true);

                            address.setBuilding(next.getText());
                        }
                    }
                }

                //поиск квартиры
                if (AddressParserItemTypeValue.flat.equals(item.getTypeValue())) {
                    if (parserItems.size() > item.getNumber()) {
                        next = parserItems.get(item.getNumber());
                        if (!next.isService()) {
                            item.setProcessed(true);
                            next.setProcessed(true);

                            address.setFlat(next.getText());
                        }
                    }
                }
            }
        }

        boolean isBelarus = false;
        AddressParserItem itemBelarus = null;

        for (AddressParserItem item : parserItems) {
            if (AddressParserHelper.regions.contains(item.getText().toLowerCase())) {
                address.setRegion(item.getText());
                item.setProcessed(true);

                continue;
            }

            if (AddressParserHelper.districts.contains(item.getText().toLowerCase())) {
                address.setDistrict(item.getText());
                item.setProcessed(true);

                continue;
            }


            if (!item.isService() && !item.isProcessed()) {
                //сначала поищу все н.п., кроме Беларусь
                //потому что это может быть и н.п. и страна
                if (!"Беларусь".equalsIgnoreCase(item.getText())) {
                    if (address.getCity().isEmpty()) {
                        processCityNext(item);
                    }
                } else {
                    isBelarus = true;
                    itemBelarus = item;
                }
            }
        }

        //а теперь, если город все еще пуст и бало указано Беларусь, то считаю, что это н.п.
        if (isBelarus)
            if (address.getCity().isEmpty()) {
                processCityWithGEO(address, itemBelarus.getText());
            } else {
                if (address.getCountry().isEmpty()) {
                    address.setCountry(itemBelarus.getText().trim());
                    itemBelarus.setProcessed(true);
                }
            }


        //пытаюсь найти улицы
        for (AddressParserItem item : parserItems) {
            if (!item.isService() && !item.isProcessed()) {
                if (address.getStreet().isEmpty()) {
                    processStreetNext(item);
                }
            }
        }
    }

    public Address getAddress() {
        return address;
    }

    public ArrayList<AddressParserItem> getParserItems() {
        return parserItems;
    }

    private void processExclusion() {
        ArrayList<AliasValue> aliasValues = new ArrayList<AliasValue>();

        aliasValues.add(new AliasValue("н.п.", "нп."));     //населенный пункт
        aliasValues.add(new AliasValue("г.п.", "гп."));     //городской поселок
        aliasValues.add(new AliasValue("п.г.т.", "пгт."));  //поселок городского типа
        aliasValues.add(new AliasValue("к.п.", "кп."));     //курортный поселок

        for (AliasValue aliasValue : aliasValues)
            if (realStrExclusion.toLowerCase().indexOf(aliasValue.getAlias().toLowerCase()) > -1) {
                realStrExclusion = realStrExclusion.replaceAll("(?i)" + aliasValue.getAlias().replaceAll("\\.", "\\\\."), aliasValue.getValue());
            }
    }

    private boolean isIndex(String str) {
        Pattern pattern = Pattern.compile("\\d{6}");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    private void setTypeAndTypeValue(AddressParserItem addressParserItem) {
        for (AliasValueType aliasValue : AddressParserHelper.aliasValues)
            if (addressParserItem.getText().equalsIgnoreCase(aliasValue.getAlias()) && !Character.isUpperCase(addressParserItem.getText().charAt(0))) {
                addressParserItem.setType(AddressParserItemType.service);
                addressParserItem.setTypeValue(aliasValue.getTypeValue());
                addressParserItem.setTypeValue2(aliasValue.getTypeValue2());

                break;
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

    private boolean processCityWithGEO(Address address, String city) {
        ViewEntryCollection vec = null;
        String cityType = "";
        boolean equal = true;

        ViewEntryCollection vecDist = null;
        ViewEntry ve = null;
        ViewEntry vetmp = null;

        boolean retVal = false;

        try {
            vec = viewGEO.getAllEntriesByKey(city, true);

            if (vec.getCount() > 0) {
                if (vec.getCount() == 1) {
                    ve = vec.getFirstEntry();
                    Vector vals = ve.getColumnValues();

                    address.setCityType(vals.elementAt(3).toString());
                    address.setCity(vals.elementAt(0).toString());

                    retVal = true;
                }

                if (vec.getCount() > 1) {
                    ve = vec.getFirstEntry();
                    Vector vals = ve.getColumnValues();

                    address.setCity(vals.elementAt(0).toString());

                    if (!address.getDistrict().isEmpty()) {
                        Vector key = new Vector();
                        key.addElement(address.getCity());
                        key.addElement(address.getDistrict());
                        vecDist = viewGEO.getAllEntriesByKey(key, true);

                        if (vecDist.getCount() == 1) {
                            ve = vecDist.getFirstEntry();
                            vals = ve.getColumnValues();

                            address.setCityType(vals.elementAt(3).toString());
                        }

                        if (vecDist.getCount() > 1) {
                            ve = vecDist.getFirstEntry();
                            vals = ve.getColumnValues();

                            cityType = vals.elementAt(3).toString();
                            vetmp = vecDist.getNextEntry();
                            ve.recycle();
                            ve = vetmp;
                            while (ve != null) {
                                vals = ve.getColumnValues();

                                if (!cityType.equals(vals.elementAt(3).toString())) {
                                    equal = false;
                                    break;
                                }

                                vetmp = vecDist.getNextEntry();
                                ve.recycle();
                                ve = vetmp;
                            }

                            if (equal) {
                                address.setCityType(cityType);
                            }
                        }
                    } else {
                        ve = vec.getFirstEntry();
                        vals = ve.getColumnValues();

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

                    retVal = true;
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

        return retVal;
    }

    private boolean processStreetWithGEO(String streetTitle) {
        ViewEntry ve = null;

        try {
            ve = viewGEOStreet.getEntryByKey(streetTitle, true);

            if (ve != null) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (ve != null) {
                    ve.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }

        return false;
    }

    private void processStreetNext(AddressParserItem item) {
        AddressParserItem item2 = null;
        AddressParserItem item3 = null;
        AddressParserItem item4 = null;
        String street = "";

        if (item.isBeginWithNumber()) {
            street = replaceStreetSuffix(item.getText());
        } else {
            street = item.getText();
        }
        //todo: если начинается на цифру, то нужно поискать дальше на совпадение
        if (processStreetWithGEO(street) && !item.isBeginWithNumber()) {
            address.setStreet(street);
            item.setProcessed(true);
        } else {
            if (parserItems.size() > item.getNumber() && !",".equals(item.getCharAfter())) {
                item2 = parserItems.get(item.getNumber());  //второе
                if (!item2.isService()) {
                    street = street + " " + item2.getText();
                    if (processStreetWithGEO(street)) {
                        address.setStreet(street);
                        item.setProcessed(true);
                        item2.setProcessed(true);
                    } else {
                        if (parserItems.size() > item2.getNumber() && !",".equals(item2.getCharAfter())) {
                            item3 = parserItems.get(item2.getNumber()); //третье
                            if (!item3.isService()) {
                                street = street + " " + item3.getText();
                                if (processStreetWithGEO(street)) {
                                    address.setStreet(street);
                                    item.setProcessed(true);
                                    item2.setProcessed(true);
                                    item3.setProcessed(true);
                                } else {
                                    if (parserItems.size() > item3.getNumber() && !",".equals(item3.getCharAfter())) {
                                        item4 = parserItems.get(item3.getNumber()); //четвертое
                                        if (!item4.isService()) {
                                            street = street + " " + item4.getText();
                                            if (processStreetWithGEO(street)) {
                                                address.setStreet(street);
                                                item.setProcessed(true);
                                                item2.setProcessed(true);
                                                item3.setProcessed(true);
                                                item4.setProcessed(true);
                                            } else {

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void processStreetPrev(AddressParserItem item) {
        AddressParserItem item2 = null;
        AddressParserItem item3 = null;
        AddressParserItem item4 = null;
        String street = "";

        if (item.isBeginWithNumber()) {
            street = replaceStreetSuffix(item.getText());
        } else {
            street = item.getText();
        }
        //todo: если начинается на цифру, то нужно поискать дальше на совпадение
        if (processStreetWithGEO(street) && street.length() > 1) {
            address.setStreet(street);
            item.setProcessed(true);
        } else {
            if (item.getNumber() > 0) {
                item2 = parserItems.get(item.getNumber() - 2);
                if (!item2.isService() && !",".equals(item2.getCharAfter())) {
                    if (item2.isBeginWithNumber()) {
                        street = replaceStreetSuffix(item2.getText()) + " " + street;
                    } else {
                        street = item2.getText() + " " + street;
                    }

                    if (processStreetWithGEO(street)) {
                        address.setStreet(street);
                        item.setProcessed(true);
                        item2.setProcessed(true);
                    } else {
                        if (item2.getNumber() > 0) {
                            item3 = parserItems.get(item2.getNumber() - 2);
                            if (!item3.isService() && !",".equals(item3.getCharAfter())) {
                                if (item3.isBeginWithNumber()) {
                                    street = replaceStreetSuffix(item3.getText()) + " " + street;
                                } else {
                                    street = item3.getText() + " " + street;
                                }
                                if (processStreetWithGEO(street)) {
                                    address.setStreet(street);
                                    item.setProcessed(true);
                                    item2.setProcessed(true);
                                    item3.setProcessed(true);
                                } else {
                                    if (item3.getNumber() > 0) {
                                        item4 = parserItems.get(item2.getNumber() - 2);
                                        if (!item4.isService() && !",".equals(item4.getCharAfter())) {
                                            if (item4.isBeginWithNumber()) {
                                                street = replaceStreetSuffix(item4.getText()) + " " + street;
                                            } else {
                                                street = item4.getText() + " " + street;
                                            }
                                            if (processStreetWithGEO(street)) {
                                                address.setStreet(street);
                                                item.setProcessed(true);
                                                item2.setProcessed(true);
                                                item3.setProcessed(true);
                                                item4.setProcessed(true);
                                            } else {

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
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

    private void processCityNext(AddressParserItem item) {
        AddressParserItem item2 = null;
        AddressParserItem item3 = null;
        String city = "";

        city = item.getText();
        if (processCityWithGEO(address, city)) {
            item.setProcessed(true);
        } else {
            if (parserItems.size() > item.getNumber() && !",".equals(item.getCharAfter())) {
                item2 = parserItems.get(item.getNumber());  //второе
                if (!item2.isService()) {
                    city = city + " " + item2.getText();
                    if (processCityWithGEO(address, city)) {
                        item.setProcessed(true);
                        item2.setProcessed(true);
                    } else {
                        if (parserItems.size() > item2.getNumber() && !",".equals(item2.getCharAfter())) {
                            item3 = parserItems.get(item2.getNumber());  //третье
                            if (!item3.isService()) {
                                city = city + " " + item3.getText();
                                if (processCityWithGEO(address, city)) {
                                    item.setProcessed(true);
                                    item2.setProcessed(true);
                                    item3.setProcessed(true);
                                } else {

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void processCityPrev(AddressParserItem item) {
        AddressParserItem item2 = null;
        AddressParserItem item3 = null;
        String city = "";

        boolean processWith2Words = false;

        //Получу сразу второе слева слово, если возможно и проверю на вхождение в служебные
        if (item.getNumber() > 0) {
            item2 = parserItems.get(item.getNumber() - 2);
            if (!item2.isService() && !",".equals(item2.getCharAfter())) {
                if (AddressParserHelper.cityFirst.contains(item2.getText().toLowerCase())) {
                    processWith2Words = true;
                }
            }
        }

        if (processWith2Words) {
            city = item2.getText() + " " + item.getText();
            if (processCityWithGEO(address, city)) {
                item.setProcessed(true);
                item2.setProcessed(true);
            }
        } else {
            city = item.getText();
            if (processCityWithGEO(address, city)) {
                item.setProcessed(true);
            } else {
                if (item.getNumber() > 0) {
                    item2 = parserItems.get(item.getNumber() - 2);
                    if (!item2.isService() && !",".equals(item2.getCharAfter())) {
                        city = item2.getText() + " " + city;
                        if (processCityWithGEO(address, city)) {
                            item.setProcessed(true);
                            item2.setProcessed(true);
                        } else {
                            if (item2.getNumber() > 0) {
                                item3 = parserItems.get(item2.getNumber() - 2);
                                if (!item3.isService() && !",".equals(item3.getCharAfter())) {
                                    city = item3.getText() + " " + city;
                                    if (processCityWithGEO(address, city)) {
                                        item.setProcessed(true);
                                        item2.setProcessed(true);
                                        item3.setProcessed(true);
                                    } else {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
