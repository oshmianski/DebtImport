package by.oshmianski.objects;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 9/16/13
 * Time: 11:33 AM
 */
public enum Status {
    ERROR(0),
    WARNING(10),
    OK(20);

    int statusOrdinal = 0;

    Status(int i) {
        statusOrdinal = i;
    }

    public static Status[] STATUS_INDEXED = new Status[]{null, ERROR, WARNING, OK};

}
