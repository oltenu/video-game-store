package repository.cache;


import repository.AbstractRepository;
import repository.RepositoryDecorator;

import java.util.List;
import java.util.Optional;

public class RepositoryCacheDecorator<T> extends RepositoryDecorator<T> {
    private final Cache<Long, T> cache;

    public RepositoryCacheDecorator(AbstractRepository<T> repository, Cache<Long, T> cache, Class<T> classType) {
        super(repository, classType);
        this.cache = cache;
    }

    @Override
    public List<T> findAll() {
        if (cache.hasResult()) {
            return cache.load();
        }

        List<T> t = decoratedRepository.findAll();
        cache.save(t);

        return t;
    }

    @Override
    public Optional<T> findById(Long id) {
        if (cache.hasResult()) {
            return Optional.ofNullable(cache.loadMap().get(id));
        }

        return decoratedRepository.findById(id);
    }

    @Override
    public boolean save(T t) {
        cache.invalidateCache();

        return decoratedRepository.save(t);
    }

    @Override
    public boolean update(T t) {
        cache.invalidateCache();

        return decoratedRepository.update(t);
    }

    @Override
    public void deleteById(Long id) {
        cache.invalidateCache();

        decoratedRepository.deleteById(id);
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedRepository.removeAll();
    }
}