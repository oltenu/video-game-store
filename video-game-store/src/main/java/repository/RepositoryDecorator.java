package repository;


public abstract class RepositoryDecorator<T> extends AbstractRepository<T> {
    protected AbstractRepository<T> decoratedRepository;

    public RepositoryDecorator(AbstractRepository<T> decoratedRepository, Class<T> classType) {
        super(classType);
        this.decoratedRepository = decoratedRepository;
    }
}
