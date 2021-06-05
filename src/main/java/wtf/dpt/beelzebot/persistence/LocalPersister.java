package wtf.dpt.beelzebot.persistence;

public abstract class LocalPersister<T extends Object> {

    public abstract boolean save(T savedObject);

    public abstract T load();
}
