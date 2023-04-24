import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CrossHair implements MouseMotionListener, MouseListener {

    DrawPanel parent;
    Timer timer = new Timer("Timer");

    CrossHair(DrawPanel parent) {
        this.parent = parent;
    }

    /* x, y to współrzedne celownika
       activated - flaga jest ustawiana po oddaniu strzału (naciśnięciu przyciku myszy)
    */
    int x;
    int y;
    boolean activated = false;
    List<CrossHairListener> listeners = new ArrayList<CrossHairListener>();
    void addCrossHairListener(CrossHairListener e){
        listeners.add(e);
    }
    void notifyListeners(){
        for(var e:listeners)
            e.onShotsFired(x,y);
    }
    void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        if(activated)g.setColor(Color.RED);
        else g.setColor(Color.WHITE);
        AffineTransform mat = g2d.getTransform();
        g2d.translate(x,y );
        for(int i=0;i<4;i++){
            g2d.rotate(2*Math.PI/4);
            g2d.drawLine(0,5,0,10);
        }
        g2d.setTransform(mat);


    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e){
        x = e.getX();
        y = e.getY();
        parent.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e){
        x = e.getX();
        y = e.getY();
        if(e.getButton() != 0) {
            activated = true;
        }
        parent.onShotsFired(x,y);
        parent.repaint();
        Timer timer = new Timer("Timer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activated=false;
                parent.repaint();
                timer.cancel();
            }
        },300);

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}