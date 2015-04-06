package by.oshmianski.objects;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 11:33 AM
 */
public enum Status {
    ERROR(0),

    WARNING_EMPTY_FIELD(30),

    WARNING_OBJECT_WILL_NOT_CREATE(40),

    WARNING_ADDRESS_NO_INDEX(50),
    WARNING_ADDRESS_NO_COUTRY(51),
    WARNING_ADDRESS_NO_REGION(52),
    WARNING_ADDRESS_NO_REGION_EXT(521),
    WARNING_ADDRESS_NO_DISTRICT(53),
    WARNING_ADDRESS_NO_CITY_TYPE(54),
    WARNING_ADDRESS_NO_CITY(55),
    WARNING_ADDRESS_NO_STREET_TYPE(56),
    WARNING_ADDRESS_NO_STREET(57),
    WARNING_ADDRESS_NO_HOUSE(58),

    WARNING_PASSPORT_NO_TYPE(60),
    WARNING_PASSPORT_NO_NUM(61),
    WARNING_PASSPORT_NO_DATE(62),
    WARNING_PASSPORT_NO_ORG(63),
    WARNING_PASSPORT_SIMILAR(64),

    WARNING_ALREADY_EXIST_IN_DB(70),
    WARNING_ALREADY_EXIST_IN_PREVIOUS(71),

    WARNING_ADDRESS_NOT_PROCESS_FULL(90),
    WARNING_ADDRESS_NOT_PROCESS_FULL_NOT_SERVICE(91),

    OK(200),

    INFO(300);

    int statusOrdinal = 0;

    Status(int i) {
        statusOrdinal = i;
    }

    public static Status getStatusByAlias(String alias){
        Status status = null;

        if("ERROR".equalsIgnoreCase(alias))
            return Status.ERROR;

        if("WARNING_EMPTY_FIELD".equalsIgnoreCase(alias))
            return Status.WARNING_EMPTY_FIELD;

        if("WARNING_OBJECT_WILL_NOT_CREATE".equalsIgnoreCase(alias))
            return Status.WARNING_OBJECT_WILL_NOT_CREATE;

        if("WARNING_ADDRESS_NO_INDEX".equalsIgnoreCase(alias))
            return Status.WARNING_ADDRESS_NO_INDEX;

        if("WARNING_ADDRESS_NO_COUTRY".equalsIgnoreCase(alias))
            return Status.WARNING_ADDRESS_NO_COUTRY;

        if("WARNING_ADDRESS_NO_REGION".equalsIgnoreCase(alias))
            return Status.WARNING_ADDRESS_NO_REGION;

        if("WARNING_ADDRESS_NO_DISTRICT".equalsIgnoreCase(alias))
            return Status.WARNING_ADDRESS_NO_DISTRICT;

        if("WARNING_ADDRESS_NO_CITY_TYPE".equalsIgnoreCase(alias))
            return Status.WARNING_ADDRESS_NO_CITY_TYPE;

        if("WARNING_ADDRESS_NO_CITY".equalsIgnoreCase(alias))
            return Status.WARNING_ADDRESS_NO_CITY;

        if("WARNING_ADDRESS_NO_STREET_TYPE".equalsIgnoreCase(alias))
            return Status.WARNING_ADDRESS_NO_STREET_TYPE;

        if("WARNING_ADDRESS_NO_STREET".equalsIgnoreCase(alias))
            return Status.WARNING_ADDRESS_NO_STREET;

        if("WARNING_ADDRESS_NO_HOUSE".equalsIgnoreCase(alias))
            return Status.WARNING_ADDRESS_NO_HOUSE;

        if("WARNING_PASSPORT_NO_TYPE".equalsIgnoreCase(alias))
            return Status.WARNING_PASSPORT_NO_TYPE;

        if("WARNING_PASSPORT_NO_NUM".equalsIgnoreCase(alias))
            return Status.WARNING_PASSPORT_NO_NUM;

        if("WARNING_PASSPORT_NO_DATE".equalsIgnoreCase(alias))
            return Status.WARNING_PASSPORT_NO_DATE;

        if("WARNING_PASSPORT_NO_ORG".equalsIgnoreCase(alias))
            return Status.WARNING_PASSPORT_NO_ORG;
        if("WARNING_PASSPORT_SIMILAR".equalsIgnoreCase(alias))
            return Status.WARNING_PASSPORT_SIMILAR;

        if("WARNING_ALREADY_EXIST_IN_DB".equalsIgnoreCase(alias))
            return Status.WARNING_ALREADY_EXIST_IN_DB;

        if("WARNING_ALREADY_EXIST_IN_PREVIOUS".equalsIgnoreCase(alias))
            return Status.WARNING_ALREADY_EXIST_IN_PREVIOUS;

        if("WARNING_ADDRESS_NOT_PROCESS_FULL".equalsIgnoreCase(alias))
            return Status.WARNING_ADDRESS_NOT_PROCESS_FULL;

        if("WARNING_ADDRESS_NOT_PROCESS_FULL_NOT_SERVICE".equalsIgnoreCase(alias))
            return Status.WARNING_ADDRESS_NOT_PROCESS_FULL_NOT_SERVICE;

        if("OK".equalsIgnoreCase(alias))
            return Status.OK;

        if("INFO".equalsIgnoreCase(alias))
            return Status.INFO;

        return status;
    }

    public static Status[] STATUS_INDEXED = new Status[]{
            null,
            ERROR,
            WARNING_EMPTY_FIELD,
            WARNING_OBJECT_WILL_NOT_CREATE,
            WARNING_PASSPORT_SIMILAR,
            WARNING_ADDRESS_NO_INDEX,
            WARNING_ADDRESS_NO_COUTRY,
            WARNING_ADDRESS_NO_REGION,
            WARNING_ADDRESS_NO_DISTRICT,
            WARNING_ADDRESS_NO_CITY_TYPE,
            WARNING_ADDRESS_NO_CITY,
            WARNING_ADDRESS_NO_STREET,
            WARNING_ADDRESS_NO_STREET_TYPE,
            WARNING_ADDRESS_NO_HOUSE,
            WARNING_ALREADY_EXIST_IN_DB,
            WARNING_ALREADY_EXIST_IN_PREVIOUS,
            WARNING_ADDRESS_NOT_PROCESS_FULL,
            WARNING_ADDRESS_NOT_PROCESS_FULL_NOT_SERVICE,
            INFO,
            OK};

}
