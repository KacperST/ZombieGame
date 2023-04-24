import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DrawPanel  extends JPanel implements CrossHairListener {
    BufferedImage background;
    Zombie zombie;
    final CopyOnWriteArrayList<Sprite> sprites = new CopyOnWriteArrayList<>(new ArrayList<>());
    SpriteFactory factory;
    CrossHair crossHair;
    int points;
    int missed;
    static volatile boolean RUN_THREAD_FLAG= true;


    DrawPanel(URL backgroundImagageURL, SpriteFactory factory) {
        try {
            background = ImageIO.read(backgroundImagageURL);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.factory = factory;
        crossHair = new CrossHair(this);
        crossHair.addCrossHairListener(this);
        addMouseMotionListener(crossHair);
        addMouseListener(crossHair);
        new AnimationThread().start();

        points = 0;
        missed = -1;

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;



        g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        for (var i : sprites) {
            i.draw(g2d, this);
        }

        crossHair.draw(g2d);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g2d.setColor(Color.white);
        g2d.drawString("Hit: " + points , 70, 20);
        g2d.drawString("Missed: " + missed , 70, 60);
        g2d.setColor(Color.white);

    }

    @Override
    public void onShotsFired(int x, int y) {
        Collections.reverse(sprites);
         for (var i : sprites) {
            if (i.isHit(x, y)) {
                points++;
                sprites.remove(i);
                this.repaint();
                break;
            }
        }
    }

    public List<Sprite> sortSprites() {
        class Comparison implements Comparator<Sprite> {

            @Override
            public int compare(Sprite o1, Sprite o2) {
                if (o1.isCloser(o2)) return 1;
                else return -1;
            }
        }
        Comparison a = new Comparison();
        sprites.sort(a);
        return sprites;
    }


    class AnimationThread extends Thread {
        public void run() {
                for (int i = 0;RUN_THREAD_FLAG ; i++) {
                    synchronized (sprites) {
                        for (int j = 0; j < sprites.size(); ) {
                            if (!sprites.get(j).isVisible()) {
                                sprites.remove(j);
                                missed++;
                                continue;
                            }
                            sprites.get(j).next();
                            j++;
                        }
                        repaint();
                        try {
                            sleep(1000 / 30);  // 30 klatek na sekundę
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (i % 20 == 0) {
                            sprites.add(factory.newSprite(getWidth(), (int) (0.6 * getHeight())));
                            sortSprites();
                        }
                    }
                }
            }
    }
}
