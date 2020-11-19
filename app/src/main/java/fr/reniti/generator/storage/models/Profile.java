package fr.reniti.generator.storage.models;

import com.google.gson.annotations.Expose;

public class Profile {

    @Expose
    private String uuid;

    @Expose
    private String firstname;

    @Expose
    private String lastname;

    @Expose
    private String birthday;

    @Expose
    private String placeofbirth;

    @Expose
    private String address;

    @Expose
    private String city;

    @Expose
    private String zipcode;

    /**
     * Constructor
     * @param uuid
     * @param firstname
     * @param lastname
     * @param birthday
     * @param placeofbirth
     * @param address
     * @param city
     * @param zipcode
     */
    public Profile(String uuid,
                   String firstname,
                   String lastname,
                   String birthday,
                   String placeofbirth,
                   String address,
                   String city,
                   String zipcode)
    {
        this.uuid = uuid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.placeofbirth = placeofbirth;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
    }

    public String getUuid()
    {
        return uuid;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getCity() {
        return city;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPlaceofbirth() {
        return placeofbirth;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPlaceofbirth(String placeofbirth) {
        this.placeofbirth = placeofbirth;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
