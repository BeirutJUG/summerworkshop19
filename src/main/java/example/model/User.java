package example.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    private Address address;

    @AttributeOverrides({
        @AttributeOverride(name = "streetName",
                column = @Column(name = "BILLING_STREET_NAME", nullable = false)), // you have to specify nullable=false again!
        @AttributeOverride(name = "city",
                column = @Column(name = "BILLING_CITY"))
    })
    private Address billingAddress;

    @OneToMany(mappedBy = "purchasedBy", cascade = CascadeType.PERSIST)
    protected Set<Purchase> purchases = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

	public Set<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(Set<Purchase> purchases) {
		this.purchases = purchases;
	}

	public void addPurchase(Purchase purchase) {
		purchases.add(purchase);
		purchase.setPurchasedBy(this);
	}

}
