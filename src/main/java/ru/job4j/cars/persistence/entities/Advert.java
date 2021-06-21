package ru.job4j.cars.persistence.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Сущность Объявление.
 */
@Entity
@Table(name = "advert")
public class Advert {

    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Заголовок объявления.
     */
    @Column
    private String summary;

    /**
     * Описания объявления.
     */
    @Column
    private String description;

    /**
     * Отметка о закрытии объявления.
     */
    @Column
    private boolean closed;

    /**
     * Пользователь, которому принадлежит объявление.
     */
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    /**
     * Автомобиль, которому принадлежит объявление.
     */
    @OneToOne
    @JoinColumn(name = "car_id", unique = true)
    private Car car;

    /**
     * Дата создания объявления.
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    /**
     * Дата закрытия объявления.
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedOn;

    /**
     * Цена автомобиля в объявлении.
     */
    @Column
    private int price;

    /**
     * Список фото, принадлежащих объявлению.
     */
    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();

    public Advert() {
    }

    public Advert(String summary, String description,
                  User user, Car car, Date createdOn, int price) {
        this.summary = summary;
        this.description = description;
        this.closed = false;
        this.user = user;
        this.car = car;
        this.createdOn = createdOn;
        this.closedOn = new Date(0);
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(Date closedOn) {
        this.closedOn = closedOn;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }
}