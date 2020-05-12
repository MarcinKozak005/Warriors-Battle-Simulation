package Simulation;

import Enums.Alliance;
import Exceptions.VictoryException;
import SimulationObjects.Regiment;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.InvalidClassException;

public class Simulation extends Canvas implements Runnable
{
    Handler handler;
    public static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private Thread thread;
    private boolean running = false;

    public Simulation()
    {
        handler = new Handler();

        Regiment r1 = new Regiment(300,300, Alliance.Red,"Right", handler);
        r1.formationSquare(10, false);
//        r1.addArmyUnit(new Infantry(300,300));
//        Regiment r2 = new Regiment(300,600, Alliance.Red, "Left", handler);
//        r2.formationSquare(5, false);
//        r1.addArmyUnit(new Infantry(300,300));


        Regiment r3 = new Regiment(600,300, Alliance.Red, "Main", handler);
        r3.formationSquare(20, false );
//        r2.addArmyUnit(new Infantry(600,300));
        Regiment r4 = new Regiment(600,600, Alliance.Blue, "Main", handler);
        r4.formationSquare(22, false );
//        r2.addArmyUnit(new Infantry(600,300));

        //Regiment r3 = new Regiment(600, 600 , Alliance.Red, handler);
        //SimulationObjects.Regiment r3 = new SimulationObjects.Regiment(600,450, Enums.Alliance.Red, handler);
        //r2.addArmyUnit(new SimulationObjects.Infantry(1600,700));
        //r2.addArmyUnit(new SimulationObjects.Infantry(1620,700));
        //r2.addArmyUnit(new SimulationObjects.Infantry(1640,700));
        //r2.populateRegimentWithUnits(25);
        //r3.formationSquare(5,false);


        handler.addRegiment(r1);
//        handler.addRegiment(r2);
        handler.addRegiment(r3);
        handler.addRegiment(r4);

        new Window(SCREEN_WIDTH, SCREEN_HEIGHT, "Warriors Simulation.Simulation", this);
    }

    public synchronized void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop()
    {
        try{
            thread.join();
            running = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >=1)
            {
                try { tick(); }
                catch (VictoryException e){ stop(); }
                delta--;
            }
            if(running)
                render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                System.out.println("FPS: "+ frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick(){
        handler.tick();
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs==null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.black);
        g.fillRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);

        handler.render(g);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args)
    {
        new Simulation();
    }

}
