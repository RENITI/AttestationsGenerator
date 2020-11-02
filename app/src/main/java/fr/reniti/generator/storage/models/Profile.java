package fr.reniti.generator.storage.models;

import com.owlike.genson.annotation.JsonProperty;

public class Profile {

    @JsonProperty(serialize = true, deserialize = true)
    private String uuid;

    @JsonProperty(serialize = true, deserialize = true)
    private String firstname;

    @JsonProperty(serialize = true, deserialize = true)
    private String lastname;

    @JsonProperty(serialize = true, deserialize = true)
    private String birthday;

    @JsonProperty(serialize = true, deserialize = true)
    private String placeofbirth;

    @JsonProperty(serialize = true, deserialize = true)
    private String address;

    @JsonProperty(serialize = true, deserialize = true)
    private String city;

    @JsonProperty(serialize = true, deserialize = true)
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
    public Profile(@JsonProperty(value="uuid") String uuid,
                   @JsonProperty(value="firstname") String firstname,
                   @JsonProperty(value="lastname") String lastname,
                   @JsonProperty(value="birthday") String birthday,
                   @JsonProperty(value="placeofbirth") String placeofbirth,
                   @JsonProperty(value="address") String address,
                   @JsonProperty(value="city") String city,
                   @JsonProperty(value="zipcode") String zipcode)
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
