package fr.reniti.generator.storage.models;

import com.google.gson.annotations.Expose;

public class Profile {

    @Expose
    private final String uuid;

    @Expose
    private final String firstname;

    @Expose
    private final String lastname;

    @Expose
    private final String birthday;

    @Expose
    private final String address;

    @Expose
    private final String city;

    @Expose
    private final String zipcode;

    /**
     * Constructor
     * @param uuid Uuid
     * @param firstname Firstname
     * @param lastname Lastname
     * @param birthday Birthday
     * @param address Address
     * @param city City
     * @param zipcode Zipcode
     */
    public Profile(String uuid,
                   String firstname,
                   String lastname,
                   String birthday,
                   String address,
                   String city,
                   String zipcode)
    {
        this.uuid = uuid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
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

    public String getZipcode() {
        return zipcode;
    }
}
