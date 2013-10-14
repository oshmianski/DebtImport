package by.oshmianski.objects;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 11:33 AM
 */
public enum Status {
    ERROR(0),

    WARNING_ADDRESS_NO_INDEX(50),
    WARNING_ADDRESS_NO_COUTRY(51),
    WARNING_ADDRESS_NO_REGION(52),
    WARNING_ADDRESS_NO_DISTRICT(53),
    WARNING_ADDRESS_NO_CITY(54),
    WARNING_ADDRESS_NO_STREET(55),
    WARNING_ADDRESS_NO_HOUSE(56),

    WARNING_PASSPORT_NO_TYPE(60),
    WARNING_PASSPORT_NO_NUM(61),
    WARNING_PASSPORT_NO_DATE(62),
    WARNING_PASSPORT_NO_ORG(63),

    WARNING(100),

    OK(200),

    INFO(300);

    int statusOrdinal = 0;

    Status(int i) {
        statusOrdinal = i;
    }

    public static Status[] STATUS_INDEXED = new Status[]{
        null,
        ERROR,
        WARNING_ADDRESS_NO_INDEX,
        WARNING_ADDRESS_NO_COUTRY,
        WARNING_ADDRESS_NO_REGION,
        WARNING_ADDRESS_NO_DISTRICT,
        WARNING_ADDRESS_NO_CITY,
        WARNING_ADDRESS_NO_STREET,
        WARNING_ADDRESS_NO_HOUSE,
        WARNING,
        INFO,
        OK};

}
