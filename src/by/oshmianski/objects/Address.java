package by.oshmianski.objects;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 07.10.13
 * Time: 13:51
 */
public class Address {
    private String index;
    private String country;
    private String region;
    private String district;
    private String unit;
    private String cityType;
    private String city;
    private String streetType;
    private String street;
    private String house;
    private String building;
    private String flat;
    private boolean isProcessedFull;
    private boolean isProcessedFullNotService;

    public Address() {
        this("", "", "", "", "", "", "", "", "", "", "", "");
    }

    public Address(String index, String country, String region, String district, String unit, String cityType, String city, String streetType, String street, String house, String building, String flat) {
        this.index = index;
        this.country = country;
        this.region = region;
        this.district = district;
        this.unit = unit;
        this.cityType = cityType;
        this.city = city;
        this.streetType = streetType;
        this.street = street;
        this.house = house;
        this.building = building;
        this.flat = flat;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCityType() {
        return cityType;
    }

    public void setCityType(String cityType) {
        this.cityType = cityType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetType() {
        return streetType;
    }

    public void setStreetType(String streetType) {
        this.streetType = streetType;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = org.apache.commons.lang3.StringUtils.replaceEach(
                WordUtils.capitalizeFully(street, new char[]{'-', ' ', '.'}),
                new String[]{
                        "Бсср",
                        "Ссср"},
                new String[]{
                        "БССР",
                        "СССР"});
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public boolean isProcessedFull() {
        return isProcessedFull;
    }

    public void setProcessedFull(boolean isProcessedFull) {
        this.isProcessedFull = isProcessedFull;
    }

    public boolean isProcessedFullNotService() {
        return isProcessedFullNotService;
    }

    public void setProcessedFullNotService(boolean isProcessedFullNotService) {
        this.isProcessedFullNotService = isProcessedFullNotService;
    }
}
