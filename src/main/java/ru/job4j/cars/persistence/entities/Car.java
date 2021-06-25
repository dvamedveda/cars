package ru.job4j.cars.persistence.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Сущность Автомобиль.
 */
@Entity
@Table(name = "car")
public class Car implements IEntity {

    /**
     * Идентификатор автомобиля.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Марка автомобиля.
     */
    @Column
    private String brand;

    /**
     * Модель автомобиля.
     */
    @Column
    private String model;

    /**
     * Тип кузова автомобиля.
     */
    @Column
    private String body;

    /**
     * Дата выпуска автомобиля.
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date produced;

    public Car() {
    }

    public Car(String brand, String model, String body, Date produced) {
        this.brand = brand;
        this.model = model;
        this.body = body;
        this.produced = produced;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getProduced() {
        return produced;
    }

    public void setProduced(Date produced) {
        this.produced = produced;
    }

    @Override
    public String toString() {
        return "Car{"
                + "id=" + id
                + ", brand='" + brand + '\''
                + ", model='" + model + '\''
                + ", body='" + body + '\''
                + ", produced=" + produced
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return id == car.id
                && Objects.equals(brand, car.brand)
                && Objects.equals(model, car.model)
                && Objects.equals(body, car.body)
                && Objects.equals(produced.getTime(), car.produced.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, body, produced.getTime());
    }
}