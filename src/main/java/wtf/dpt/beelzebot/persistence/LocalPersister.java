package wtf.dpt.beelzebot.persistence;

public abstract class LocalPersister<T extends Object> {

    public abstract void save(T savedObject);

    public abstract T load();
}
