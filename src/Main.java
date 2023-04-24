import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // write your code here

        JFrame frame = new JFrame("Zombie");
        DrawPanel panel = new DrawPanel(Main.class.getResource("background.jpg"),  ZombieFactory.INSTANCE);
        frame.setContentPane(panel);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                panel.RUN_THREAD_FLAG = false;
               panel.crossHair.timer.cancel();

            }
        });
    }

}
