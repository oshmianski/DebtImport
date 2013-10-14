package by.oshmianski.objects;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 14.10.13
 * Time: 15:45
 */
public class Passport {
    private String passType;
    private String passNum;
    private String passDate;
    private String passOrg;

    public Passport(String passType, String passNum, String passDate, String passOrg) {
        this.passType = passType;
        this.passNum = passNum;
        this.passDate = passDate;
        this.passOrg = passOrg;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    public String getPassNum() {
        return passNum;
    }

    public void setPassNum(String passNum) {
        this.passNum = passNum;
    }

    public String getPassDate() {
        return passDate;
    }

    public void setPassDate(String passDate) {
        this.passDate = passDate;
    }

    public String getPassOrg() {
        return passOrg;
    }

    public void setPassOrg(String passOrg) {
        this.passOrg = passOrg;
    }
}
