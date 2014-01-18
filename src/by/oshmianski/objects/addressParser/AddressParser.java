package by.oshmianski.objects.addressParser;

import by.oshmianski.objects.Address;
import by.oshmianski.objects.AliasValue;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vintselovich on 24.12.13.
 */
public class AddressParser {
    private String realStr = "";
    private String realStrExclusion = "";
    private String realStr_ = "";
    private ArrayList<AddressParserItem> parserItems = new ArrayList<AddressParserItem>();
    private Address address = new Address();

    public AddressParser(String realStr) {
        this.realStr = realStr;
        realStrExclusion = realStr;
    }

    public void parse() {
        processExclusion();

        realStr_ = realStrExclusion.replaceAll("\\.", "_").replaceAll(",", "_").replaceAll(" ", "_");

        String[] addressArray = realStr_.split("_");
        int i = 1;
        int start = 0;
        for (String addr : addressArray) {
            if (!addr.isEmpty()) {
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


        for (AliasValueType aliasValue : aliasValues)
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
}
