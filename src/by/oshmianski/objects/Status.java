package by.oshmianski.objects;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 11:33 AM
 */
public enum Status {
    ERROR(0),
//    WARNING_ADDRESS_NO_DISTRICT(50),
    WARNING_ADDRESS_NO_CITY(51),
    WARNING_ADDRESS_NO_STREET(52),
    WARNING_ADDRESS_NO_HOUSE(53),
    WARNING(100),
    OK(200);

    int statusOrdinal = 0;

    Status(int i) {
        statusOrdinal = i;
    }

//    public static Status[] STATUS_INDEXED = new Status[]{null, ERROR, WARNING_ADDRESS_NO_DISTRICT, WARNING_ADDRESS_NO_CITY, WARNING_ADDRESS_NO_STREET, WARNING_ADDRESS_NO_HOUSE, WARNING, OK};
    public static Status[] STATUS_INDEXED = new Status[]{null, ERROR, WARNING_ADDRESS_NO_CITY, WARNING_ADDRESS_NO_STREET, WARNING_ADDRESS_NO_HOUSE, WARNING, OK};

}
