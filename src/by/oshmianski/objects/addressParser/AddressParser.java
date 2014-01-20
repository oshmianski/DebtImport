package by.oshmianski.objects.addressParser;

import by.oshmianski.objects.Address;
import by.oshmianski.objects.AliasValue;
import by.oshmianski.utils.MyLog;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vintselovich on 24.12.13.
 */
public class AddressParser {
    private View viewGEO;

    private String realStr = "";
    private String realStrExclusion = "";
    private String realStr_ = "";
    private ArrayList<AddressParserItem> parserItems = new ArrayList<AddressParserItem>();
    private Address address = new Address();

    public AddressParser(String realStr, View viewGEO) {
        this.realStr = realStr;
        realStrExclusion = realStr;

        this.viewGEO = viewGEO;
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

                setTypeAndtypeValue(addressParserItem);
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
                        if (Character.isDigit(next.getText().charAt(0))) {
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
                        if (Character.isDigit(next.getText().charAt(0))) {
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

                boolean cityGood = false;
                //поиск города
                if (AddressParserItemTypeValue.city.equals(item.getTypeValue())) {
                    if (!cityGood)
                        if (parserItems.size() > item.getNumber()) {
                            next = parserItems.get(item.getNumber());
                            if (!next.isService() && !next.isProcessed()) {
                                item.setProcessed(true);
                                next.setProcessed(true);

                                address.setCityType(item.getTypeValue2());
                                address.setCity(next.getText());

                                cityGood = true;
                            }
                        }

                    if (!cityGood)
                        if (item.getNumber() > 0) {
                            prev = parserItems.get(item.getNumber() - 2);
                            if (!prev.isService() && !prev.isProcessed()) {
                                item.setProcessed(true);
                                prev.setProcessed(true);

                                address.setCityType(item.getTypeValue2());
                                address.setCity(prev.getText());

                                cityGood = true;
                            }
                        }
                }

                boolean streetGood = false;
                //поиск улицы
                if (AddressParserItemTypeValue.street.equals(item.getTypeValue())) {
                    if (!streetGood)
                        if (parserItems.size() > item.getNumber()) {
                            next = parserItems.get(item.getNumber());
                            if (!next.isService() && !next.isProcessed()) {
                                item.setProcessed(true);
                                next.setProcessed(true);

                                address.setStreetType(item.getTypeValue2());
                                address.setStreet(next.getText());

                                streetGood = true;
                            }
                        }

                    if (!streetGood)
                        if (item.getNumber() > 0) {
                            prev = parserItems.get(item.getNumber() - 2);
                            if (!prev.isService() && !prev.isProcessed()) {
                                item.setProcessed(true);
                                prev.setProcessed(true);

                                address.setStreetType(item.getTypeValue2());
                                address.setStreet(prev.getText());

                                streetGood = true;
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
                if (address.getCity().isEmpty())
                    processCityWithGEO(address, item);
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

    private void setTypeAndtypeValue(AddressParserItem addressParserItem) {
        for (AliasValueType aliasValue : AddressParserHelper.aliasValues)
            if (addressParserItem.getText().equalsIgnoreCase(aliasValue.getAlias())) {
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


    private void processCityWithGEOVec(Address address, AddressParserItem item, ViewEntryCollection vec) {
        String cityType = "";
        boolean equal = true;

        ViewEntryCollection vecDist = null;
        ViewEntry ve = null;
        ViewEntry vetmp = null;

        try {
            if (vec.getCount() == 1) {
                ve = vec.getFirstEntry();
                Vector vals = ve.getColumnValues();

                address.setCityType(vals.elementAt(3).toString());
                address.setCity(vals.elementAt(0).toString());

                item.setProcessed(true);

                return;
            }

            if (vec.getCount() > 1) {
                ve = vec.getFirstEntry();
                Vector vals = ve.getColumnValues();

                address.setCity(vals.elementAt(0).toString());
                item.setProcessed(true);

                if (!address.getDistrict().isEmpty()) {
                    Vector key = new Vector();
                    key.addElement(address.getCity());
                    key.addElement(address.getDistrict());
                    vecDist = viewGEO.getAllEntriesByKey(key, true);

                    if (vecDist.getCount() == 1) {
                        ve = vecDist.getFirstEntry();
                        vals = ve.getColumnValues();

                        address.setCityType(vals.elementAt(3).toString());

                        return;
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

                        return;
                    }
                }

                return;
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
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }
    }

    private void processCityWithGEO(Address address, AddressParserItem item) {
        ViewEntryCollection vec = null;

        try {
            vec = viewGEO.getAllEntriesByKey(item.getText(), true);

            if (vec.getCount() > 0) {
                processCityWithGEOVec(address, item, vec);
            } else {
//                vec = viewGEO.getAllEntriesByKey(item.getText(), false);
//                if (vec.getCount() > 0) {
//                    processCityWithGEOVec(address, item, vec);
//                }
            }

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (vec != null) {
                    vec.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }
    }
}
