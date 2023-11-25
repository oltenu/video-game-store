package service.game;

import model.VideoGame;
import repository.game.VideoGameRepository;

import java.util.List;

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
                () -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public boolean save(VideoGame videoGame) {
        return videoGameRepository.save(videoGame);
    }

    @Override
    public void deleteById(Long id) {
        videoGameRepository.deleteById(id);
    }

    @Override
    public boolean update(VideoGame videoGame) {
        return videoGameRepository.update(videoGame);
    }

    @Override
    public void removeAll() {
        videoGameRepository.removeAll();
    }
}
