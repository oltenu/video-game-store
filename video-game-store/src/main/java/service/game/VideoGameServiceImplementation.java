package service.game;

import model.VideoGame;
import model.validator.Notification;
import repository.game.VideoGameRepository;

import java.util.List;
import java.util.Optional;

public class VideoGameServiceImplementation implements VideoGameService {
    private final VideoGameRepository videoGameRepository;

    public VideoGameServiceImplementation(VideoGameRepository videoGameRepository) {
        this.videoGameRepository = videoGameRepository;
    }

    @Override
    public List<VideoGame> findAll() {
        return videoGameRepository.findAll();
    }

    @Override
    public VideoGame findById(Long id) {
        return videoGameRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Game with id: %d not found".formatted(id)));
    }

    @Override
    public Notification<Boolean> save(VideoGame videoGame) {
        Notification<Boolean> saveNotification = new Notification<>();

        if (videoGameRepository.save(videoGame)) {
            saveNotification.setResult(Boolean.TRUE);
        } else {
            saveNotification.setResult(Boolean.FALSE);
            saveNotification.addError("Something went wrong!");
        }

        return saveNotification;
    }

    @Override
    public void deleteById(Long id) {
        videoGameRepository.deleteById(id);
    }

    @Override
    public Notification<Boolean> update(VideoGame videoGame) {
        Notification<Boolean> resultNotification = new Notification<>();

        Long id = videoGame.getId();
        Optional<VideoGame> game = videoGameRepository.findById(id);

        if (game.isPresent()) {
            resultNotification.setResult(Boolean.TRUE);
        } else {
            resultNotification.setResult(Boolean.FALSE);
            resultNotification.addError("Select an existing game to edit!");
        }

        return resultNotification;
    }

    @Override
    public void removeAll() {
        videoGameRepository.removeAll();
    }
}
