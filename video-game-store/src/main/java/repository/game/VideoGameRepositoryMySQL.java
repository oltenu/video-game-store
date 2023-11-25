package repository.game;

import model.VideoGame;
import repository.AbstractRepository;

import java.sql.Connection;

public class VideoGameRepositoryMySQL extends AbstractRepository<VideoGame> implements VideoGameRepository {
    public VideoGameRepositoryMySQL(Connection connection) {
        super(connection, VideoGame.class);
    }
}
