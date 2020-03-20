import javax.swing.*;
import java.awt.*;

public class Window extends Canvas
{
    public Window(int width, int height, String title, Game game)
    {
        JFrame frame = new JFrame(title); // $: frame of our window

        frame.setPreferredSize(new Dimension(width,height));
        frame.setMaximumSize(new Dimension(width,height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // $: close on red X; top right
        frame.setResizable(false); // $ No to resize
        frame.setLocationRelativeTo(null); // $ Window starts in the middle of the Screen
        frame.add(game);
        frame.setVisible(true);
        game.start();
    }
}
