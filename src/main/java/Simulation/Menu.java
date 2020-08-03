package Simulation;

import Enums.Alliance;
import Enums.ArmyType;
import SimulationObjects.Regiment;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends MouseAdapter {
    public static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static final int BUTTON_WIDTH = 390;
    private static final int BUTTON_HEIGHT = 75;
    private static final int BUTTON_X = (SCREEN_WIDTH-BUTTON_WIDTH)/2;

    Simulation simulation;
    Handler handler;

    public Menu(Simulation simulation, Handler handler) {
        this.simulation = simulation;
        this.handler = handler;
    }

    private boolean isMouseOverArea(int mx, int my, int x, int y, int width, int height) {
        return mx > x && mx < x + width && my > y && my < y + height;
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (isMouseOverArea(mx, my, BUTTON_X, 350, BUTTON_WIDTH, BUTTON_HEIGHT )) {
            simulation.simulationState = Simulation.STATE.FullPowerSimulation;

            Regiment r1 = new Regiment(300, 300, Alliance.Red, "Secondary", handler);
            Regiment r4 = new Regiment(600, 600, Alliance.Blue, "Primary", handler);

            r1.formationSquare(10, false, ArmyType.INFANTRY);
            r4.formationSquare(22, false, ArmyType.INFANTRY);

            handler.addRegiment(r1);
            handler.addRegiment(r4);
        }
        else if (isMouseOverArea(mx, my, BUTTON_X, 450, BUTTON_WIDTH, BUTTON_HEIGHT )) {
            System.exit(0);
        }
    }

    public void tick() {}

    public void render(Graphics g) {
        Font titleFont = new Font("arial", Font.PLAIN, 35);
        Font optionsFont = new Font("arial", Font.PLAIN, 25);
        Font footerFont = new Font("arial", Font.PLAIN, 15);

        g.setColor(Color.yellow);
        g.setFont(titleFont);
        g.drawString("Menu:", BUTTON_X + 147, 175);

        g.setColor(Color.green);
        g.drawRect(BUTTON_X,350, BUTTON_WIDTH, BUTTON_HEIGHT);
        g.setColor(Color.yellow);
        g.setFont(optionsFont);
        g.drawString("Example Simulation", BUTTON_X+80 , 395);

        g.setColor(Color.RED);
        g.drawRect(BUTTON_X,450, BUTTON_WIDTH, BUTTON_HEIGHT);
        g.setColor(Color.yellow);
        g.setFont(optionsFont);
        g.drawString("Exit", BUTTON_X + 160, 495);

        g.setFont(footerFont);
        g.setColor(Color.red);
        int lineHeight = g.getFontMetrics().getHeight();
        int y = SCREEN_HEIGHT-100;
        String footer = "Studio Projektowe 2020\nMarcin Kozak\nZuzanna Obajtek";
        for (String line : footer.split("\n"))
            g.drawString(line, 25, y += lineHeight);
    }
}
