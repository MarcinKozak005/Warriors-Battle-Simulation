package Simulation;

import Exceptions.VictoryException;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Simulation extends Canvas implements Runnable
{
    Handler handler;
    Menu menu;
    public static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private Thread thread;
    private boolean running = false;

    public enum STATE {
        Menu,
        KarksiBattleSimulation,
        FullPowerSimulation
    }

    public STATE simulationState = STATE.Menu;

    public Simulation()
    {
        handler = new Handler();

        menu = new Menu(this, handler);
        this.addMouseListener(menu);

//        ta sekcja jest teraz w Menu mousePressed
//        if (simulationState == STATE.main.java.Simulation) {
//
//            Regiment r1 = new Regiment(300, 300, Alliance.Red, "Secondary", handler);
//            r1.formationSquare(10, false);
//            Regiment r3 = new Regiment(600, 300, Alliance.Red, "Primary", handler);
//            r3.formationSquare(20, false);
//
//            Regiment r4 = new Regiment(600, 600, Alliance.Blue, "Primary", handler);
//            r4.formationSquare(22, false);
//
//
//            // Side Attacking +-
//        /*Regiment r1 = new Regiment(500,500, Alliance.Red,"Main", handler);
//        r1.formationSquare(20, false);
//
//        Regiment r3 = new Regiment(500,300, Alliance.Blue, "Main", handler);
//        r3.formationSquare(17, false );
//        Regiment r4 = new Regiment(700,500, Alliance.Blue, "Left", handler);
//        r4.formationSquare(10, false );*/
//
//
//            handler.addRegiment(r1);
//            handler.addRegiment(r3);
//            handler.addRegiment(r4);
//        }

        new Window(SCREEN_WIDTH, SCREEN_HEIGHT, "Warriors main.java.Simulation.main.java.Simulation", this);
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
//                System.out.println("FPS: "+ frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick(){
        if (simulationState == STATE.KarksiBattleSimulation || simulationState == STATE.FullPowerSimulation)
            handler.tick();
        else
            menu.tick();
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

        if (simulationState == STATE.KarksiBattleSimulation || simulationState == STATE.FullPowerSimulation)
            handler.render(g);
        else
            menu.render(g);


        g.dispose();
        bs.show();
    }

    public static void main(String[] args)
    {
        new Simulation();
    }

}
