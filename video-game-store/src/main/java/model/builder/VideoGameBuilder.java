package model.builder;

import model.VideoGame;

import java.time.LocalDate;

public class VideoGameBuilder {
    private final VideoGame videoGame;

    public VideoGameBuilder() {
        this.videoGame = new VideoGame();
    }

    public VideoGameBuilder setId(Long id) {
        videoGame.setId(id);

        return this;
    }

    public VideoGameBuilder setName(String name) {
        videoGame.setName(name);

        return this;
    }

    public VideoGameBuilder setDescription(String description) {
        videoGame.setDescription(description);

        return this;
    }

    public VideoGameBuilder setAmount(Integer amount) {
        videoGame.setAmount(amount);

        return this;
    }

    public VideoGameBuilder setPrice(Double price) {
        videoGame.setPrice(price);

        return this;
    }

    public VideoGameBuilder setReleaseDate(LocalDate releaseDate) {
        videoGame.setReleasedDate(releaseDate);

        return this;
    }

    public VideoGame build() {
        return videoGame;
    }
}
