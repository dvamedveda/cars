package ru.job4j.cars.persistence.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Сущность Пользователь.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Имя пользователя.
     */
    @Column(unique = true)
    private String name;

    /**
     * Дата создания пользователя.
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    /**
     * Список объявлений пользователя.
     */
    @OneToMany(mappedBy = "user")
    private List<Advert> adverts = new ArrayList<>();

    public User() {
    }

    public User(String name, Date created) {
        this.name = name;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void addAdvert(Advert advert) {
        this.adverts.add(advert);
    }

    public List<Advert> getAdverts() {
        return this.adverts;
    }
}