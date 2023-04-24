import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public enum ZombieFactory implements SpriteFactory{
    INSTANCE;
    @Override
    public Sprite newSprite(int x, int y) {
        double scale = new Random().nextDouble(1.8) + 0.2;
        Zombie z = null;
        try {
            z = new Zombie(x,y,scale, ImageIO.read(Objects.requireNonNull(getClass().getResource("zombieee.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return z;
    }
}
