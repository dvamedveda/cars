package ru.job4j.cars.persistence.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Сущность Пользователь.
 */
@Entity
@Table(name = "users")
public class User implements IEntity {

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
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
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

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", created=" + created
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
        User user = (User) o;
        return id == user.id
                && Objects.equals(name, user.name)
                && Objects.equals(created.getTime(), user.created.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, created.getTime());
    }
}