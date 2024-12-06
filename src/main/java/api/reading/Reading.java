package api.reading;

import java.util.UUID;

public class Reading {

    private UUID id;
    private UUID customer_id;
    private TypeOfMeter type;
    private double reading;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerID() {
        return customer_id;
    }

    public void setCustomerID(UUID customer_id) {
        this.customer_id = customer_id;
    }

    public TypeOfMeter getType() {
        return type;
    }

    public void setType(TypeOfMeter type) {
        this.type = type;
    }

    public double getReading() {
        return reading;
    }

    public void setReading(double reading) {
        this.reading = reading;
    }
}
