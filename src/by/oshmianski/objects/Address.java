package by.oshmianski.objects;

import by.oshmianski.objects.addressParser.AddressParserOperation;
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
    private AddressParserOperation indexOperation;
    private AddressParserOperation countryOperation;
    private AddressParserOperation regionOperation;
    private AddressParserOperation districtOperation;
    private AddressParserOperation unitOperation;
    private AddressParserOperation cityTypeOperation;
    private AddressParserOperation cityOperation;
    private AddressParserOperation streetTypeOperation;
    private AddressParserOperation streetOperation;
    private AddressParserOperation houseOperation;
    private AddressParserOperation buildingOperation;
    private AddressParserOperation flatOperation;
    private boolean isProcessedFull;
    private boolean isProcessedFullNotService;

    public Address() {
        this("", "", "", "", "", "", "", "", "", "", "", "");
    }

    public Address(
            String index,
            String country,
            String region,
            String district,
            String unit,
            String cityType,
            String city,
            String streetType,
            String street,
            String house,
            String building,
            String flat) {

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

    public void setIndex(String index, AddressParserOperation indexOperation) {
        this.index = index;
        this.indexOperation = indexOperation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country, AddressParserOperation countryOperation) {
        this.country = country;
        this.countryOperation = countryOperation;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region, AddressParserOperation regionOperation) {
        this.region = WordUtils.capitalizeFully(region);
        this.regionOperation = regionOperation;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district, AddressParserOperation districtOperation) {
        this.district = WordUtils.capitalizeFully(district);
        this.districtOperation = districtOperation;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit, AddressParserOperation unitOperation) {
        this.unit = unit;
        this.unitOperation = unitOperation;
    }

    public String getCityType() {
        return cityType;
    }

    public void setCityType(String cityType, AddressParserOperation cityTypeOperation) {
        this.cityType = cityType == null ? "" : cityType;
        this.cityTypeOperation = cityTypeOperation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city, AddressParserOperation cityOperation) {
        this.city = city == null ? "" : city;
        this.cityOperation = cityOperation;
    }

    public String getStreetType() {
        return streetType;
    }

    public void setStreetType(String streetType, AddressParserOperation streetTypeOperation) {
        this.streetType = streetType == null ? "" : streetType;
        this.streetTypeOperation = streetTypeOperation;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street, AddressParserOperation streetOperation) {
        this.street = org.apache.commons.lang3.StringUtils.replaceEach(
                WordUtils.capitalizeFully(street, '-', ' ', '.'),
                new String[]{
                        "Бсср",
                        "Ссср"},
                new String[]{
                        "БССР",
                        "СССР"});

        this.streetOperation = streetOperation;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house, AddressParserOperation houseOperation) {
        this.house = house;
        this.houseOperation = houseOperation;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat, AddressParserOperation flatOperation) {
        this.flat = flat;
        this.flatOperation = flatOperation;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building, AddressParserOperation buildingOperation) {
        this.building = building;
        this.buildingOperation = buildingOperation;
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

    public AddressParserOperation getIndexOperation() {
        return indexOperation;
    }

    public AddressParserOperation getCountryOperation() {
        return countryOperation;
    }

    public AddressParserOperation getRegionOperation() {
        return regionOperation;
    }

    public AddressParserOperation getDistrictOperation() {
        return districtOperation;
    }

    public AddressParserOperation getUnitOperation() {
        return unitOperation;
    }

    public AddressParserOperation getCityTypeOperation() {
        return cityTypeOperation;
    }

    public AddressParserOperation getCityOperation() {
        return cityOperation;
    }

    public AddressParserOperation getStreetTypeOperation() {
        return streetTypeOperation;
    }

    public AddressParserOperation getStreetOperation() {
        return streetOperation;
    }

    public AddressParserOperation getHouseOperation() {
        return houseOperation;
    }

    public AddressParserOperation getBuildingOperation() {
        return buildingOperation;
    }

    public AddressParserOperation getFlatOperation() {
        return flatOperation;
    }
}
