import Flappy_Bird.Flappy_Bird;

public class Main {
    public static void main(String[] args) {
        Flappy_Bird f = new Flappy_Bird();
        f.set_call(f);
        f.time_start();
    }
}
