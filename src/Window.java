import javax.swing.*;
import java.awt.*;

public class Window extends Canvas
{
    public Window(int width, int height, String title, Simulation simulation)
    {
        JFrame frame = new JFrame(title); //  Ramka naszego Window (?)

        frame.setPreferredSize(new Dimension(width,height));
        frame.setMaximumSize(new Dimension(width,height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Zamykanie na czerwonym 'X' w top right
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Okno będzie na środku ekranu
        frame.add(simulation);
        frame.setVisible(true);
        simulation.start();
    }
}
