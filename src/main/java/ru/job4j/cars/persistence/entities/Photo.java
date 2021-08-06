package ru.job4j.cars.persistence.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

/**
 * Сущность Фото для автомобиля.
 */
@Entity
@Table(name = "photo")
public class Photo implements IEntity {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Название фото.
     */
    @Column
    private String name;

    /**
     * Данные фото.
     */
    @Column
    private byte[] data;

    /**
     * Объявление, которому принадлежит фото.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advert_id")
    @JsonBackReference
    private Advert advert;

    public Photo() {
    }

    public Photo(String name, byte[] data, Advert advert) {
        this.name = name;
        this.data = data;
        this.advert = advert;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Advert getAdvert() {
        return advert;
    }

    public void setAdvert(Advert advert) {
        this.advert = advert;
    }

    @Override
    public String toString() {
        return "Photo{"
                + "id=" + id
                + ", name='" + name + '\''
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
        Photo photo = (Photo) o;
        return id == photo.id
                && Objects.equals(name, photo.name)
                && Objects.equals(advert, photo.advert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, advert);
    }
}