package Simulation;

import Enums.Alliance;
import Enums.ArmyType;
import SimulationObjects.Cavalry;
import SimulationObjects.Infantry;
import SimulationObjects.Musketeer;
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

        // Karksi
        if(isMouseOverArea(mx, my, BUTTON_X, 250, BUTTON_WIDTH, BUTTON_HEIGHT ))
        {
            simulation.simulationState = Simulation.STATE.KarksiBattleSimulation;

            // Sweden
            Regiment Sc1 = new Regiment(700, 300, Alliance.Blue, "Sc1", handler);
            Regiment Sc2 = new Regiment(700, 600, Alliance.Blue, "Sc2", handler);
            Regiment Sm1 = new Regiment(500, 350, Alliance.Blue, "Sm1", handler);
            Regiment Sm2 = new Regiment(500, 550, Alliance.Blue, "Sm2", handler);
            Sc1.formationSquare(22, false, ArmyType.CAVALRY);
            Sc2.formationSquare(22, false, ArmyType.CAVALRY);
            Sm1.formationSquare(16, false, ArmyType.MUSKETEER);
            Sm2.formationSquare(16, false, ArmyType.MUSKETEER);
            handler.addRegiment(Sc1);
            handler.addRegiment(Sc2);
            handler.addRegiment(Sm1);
            handler.addRegiment(Sm2);

            // CommonWealth
            Regiment Pc1 = new Regiment(1000, 350, Alliance.Red, "Pc1", handler);
            Regiment Pc2 = new Regiment(1000, 550, Alliance.Red, "Pc2", handler);
            Regiment Pi1 = new Regiment(1150, 350, Alliance.Red, "Pi1", handler);
            Regiment Pi2 = new Regiment(1150, 550, Alliance.Red, "Pi2", handler);
            Regiment Pm1 = new Regiment(950, 200, Alliance.Red, "Pm1", handler);
            Regiment Pm2 = new Regiment(950, 700, Alliance.Red, "Pm2", handler);
            Pc1.formationSquare(10, false, ArmyType.CAVALRY);
            Pc2.formationSquare(10, false, ArmyType.CAVALRY);
            Pi1.formationSquare(10, false, ArmyType.INFANTRY);
            Pi2.formationSquare(10, false, ArmyType.INFANTRY);
            Pm1.formationSquare(20, false, ArmyType.MUSKETEER);
            Pm2.formationSquare(20, false, ArmyType.MUSKETEER);
            handler.addRegiment(Pc1);
            handler.addRegiment(Pi1);
            handler.addRegiment(Pm1);
            handler.addRegiment(Pc2);
            handler.addRegiment(Pi2);
            handler.addRegiment(Pm2);



        }
        // Full Simulation
        else if (isMouseOverArea(mx, my, BUTTON_X, 350, BUTTON_WIDTH, BUTTON_HEIGHT )) {
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

    public void mouseReleased(MouseEvent e) {

    }

    public void tick() {

    }
    public void render(Graphics g) {
        Font titleFont = new Font("arial", Font.PLAIN, 35);
        Font optionsFont = new Font("arial", Font.PLAIN, 25);
        Font footerFont = new Font("arial", Font.PLAIN, 15);

        g.setColor(Color.yellow);
        g.setFont(titleFont);
        g.drawString("Menu:", BUTTON_X + 147, 175);

        g.setColor(Color.green);
        g.drawRect(BUTTON_X,250, BUTTON_WIDTH, BUTTON_HEIGHT);
        g.setColor(Color.yellow);
        g.setFont(optionsFont);
        g.drawString("Symulacja bitwy pod Karksi", BUTTON_X+40, 295);

        g.setColor(Color.green);
        g.drawRect(BUTTON_X,350, BUTTON_WIDTH, BUTTON_HEIGHT);
        g.setColor(Color.yellow);
        g.setFont(optionsFont);
        g.drawString("Symulacja z pełnym obciążeniem", BUTTON_X+15 , 395);

        g.setColor(Color.RED);
        g.drawRect(BUTTON_X,450, BUTTON_WIDTH, BUTTON_HEIGHT);
        g.setColor(Color.yellow);
        g.setFont(optionsFont);
        g.drawString("Wyjście", BUTTON_X + 160, 495);

        g.setFont(footerFont);
        g.setColor(Color.red);
        int lineHeight = g.getFontMetrics().getHeight();
        int y = SCREEN_HEIGHT-100;
        String footer = "Studio Projektowe 2020\nMarcin Kozak\nZuzanna Obajtek";
        for (String line : footer.split("\n"))
            g.drawString(line, 25, y += lineHeight);
    }
}
