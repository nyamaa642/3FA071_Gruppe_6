package api.customer;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class Customer {

    private UUID id;
    private String lastName;
    private String firstName;
    private LocalDate birthDate;
    private Gender gender;

    public Customer(UUID id, String lastName, String firstName, LocalDate birthDate, Gender gender) {
        this.id = UUID.randomUUID();
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public Customer() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}


