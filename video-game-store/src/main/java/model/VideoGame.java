package model;

import model.annotations.DateType;
import model.annotations.Id;
import model.annotations.MapToDatabase;

import java.time.LocalDate;
import java.util.Objects;

@MapToDatabase(columnName = "video_game")
public class VideoGame {
    @Id
    private Long id;
    private String name;
    private String description;
    private Integer amount;
    private Double price;
    @DateType
    @MapToDatabase(columnName = "released_date")
    private LocalDate releasedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(LocalDate releasedDate) {
        this.releasedDate = releasedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoGame videoGame = (VideoGame) o;
        return Objects.equals(id, videoGame.id) && Objects.equals(name, videoGame.name) && Objects.equals(description, videoGame.description) && Objects.equals(amount, videoGame.amount) && Objects.equals(price, videoGame.price) && Objects.equals(releasedDate, videoGame.releasedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, amount, price, releasedDate);
    }

    @Override
    public String toString() {
        return "VideoGame{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", releasedDate=" + releasedDate +
                '}';
    }
}
