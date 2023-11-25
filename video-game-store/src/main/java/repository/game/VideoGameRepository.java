package repository.game;

import model.VideoGame;

import java.util.List;
import java.util.Optional;

public interface VideoGameRepository {
    List<VideoGame> findAll();

    Optional<VideoGame> findById(Long id);

    boolean save(VideoGame videoGame);

    void deleteById(Long id);

    boolean update(VideoGame videoGame);

    void removeAll();
}
