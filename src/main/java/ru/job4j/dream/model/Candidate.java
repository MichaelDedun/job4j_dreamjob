package ru.job4j.dream.model;

import java.util.Objects;

public class Candidate {
    private int id;
    private String name;
    private Long photoId;
    private Integer cityId;

    public Candidate(int id, String name, Long photoId, Integer cityId) {
        this.id = id;
        this.name = name;
        this.photoId = photoId;
        this.cityId = cityId;
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

    public Long getPhotoId() {
        return photoId;
    }

    public Candidate setPhotoId(Long photoId) {
        this.photoId = photoId;
        return this;
    }

    public Integer getCityId() {
        return cityId;
    }

    public Candidate setCityId(Integer cityId) {
        this.cityId = cityId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return id == candidate.id &&
                Objects.equals(name, candidate.name) &&
                Objects.equals(photoId, candidate.photoId) &&
                Objects.equals(cityId, candidate.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, photoId, cityId);
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", photoId=" + photoId +
                ", cityId=" + cityId +
                '}';
    }
}
