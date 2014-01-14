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
        ArrayList<AliasValueType> aliasValues = new ArrayList<AliasValueType>();

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

    private static class AliasValueType {
        private String alias;
        private AddressParserItemTypeValue typeValue;
        private String typeValue2;

        private AliasValueType(String alias, AddressParserItemTypeValue typeValue, String typeValue2) {
            this.alias = alias;
            this.typeValue = typeValue;
            this.typeValue2 = typeValue2;
        }

        public String getAlias() {
            return alias;
        }

        public AddressParserItemTypeValue getTypeValue() {
            return typeValue;
        }

        public String getTypeValue2() {
            return typeValue2;
        }
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
