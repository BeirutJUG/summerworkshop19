package example.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * This is an embeddable type whose lifecycle is bound to another entity.
 */
@Embeddable
public class Address {

    @Column(nullable = false) // Maps to NOT NULL when creating schema
    private String streetName;

    @Column(nullable = false, length = 20) // Override VARCHAR(255)
    private String city;

    /**
     * Hibernate relies on a no-argument constructor to create instances and
     * fill their properties.
     */
    protected Address() {
    }

    /**
     * Another constructor for convenience
     */
    public Address(String streetName, String city) {
        this.streetName = streetName;
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
