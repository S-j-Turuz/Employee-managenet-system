public class Tuple<T> {
    //T should be either LocalTime or a Tuple<T>
    public final T _open;
    public final T _close;

    public Tuple(T open){
        _open = open;
        _close = null;
    }
    public Tuple(T open, T close){
        _open = open;
        _close = close;
    }
}
