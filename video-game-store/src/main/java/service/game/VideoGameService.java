package service.game;

import model.VideoGame;
import model.validator.Notification;

import java.util.List;

public interface VideoGameService {
    List<VideoGame> findAll();

    VideoGame findById(Long id);

    boolean save(VideoGame videoGame);

    void deleteById(Long id);

    boolean update(VideoGame videoGame);

    void removeAll();

}
