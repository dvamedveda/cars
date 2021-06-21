package ru.job4j.cars.persistence.entities;

import javax.persistence.*;

/**
 * Сущность Фото для автомобиля.
 */
@Entity
@Table(name = "photo")
public class Photo {

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
    private String data;

    /**
     * Объявление, которому принадлежит фото.
     */
    @ManyToOne
    @JoinColumn(name = "advert_id")
    private Advert advert;

    public Photo() {
    }

    public Photo(String name, String data, Advert advert) {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Advert getAdvert() {
        return advert;
    }

    public void setAdvert(Advert advert) {
        this.advert = advert;
    }
}