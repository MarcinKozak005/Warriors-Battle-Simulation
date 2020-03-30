import java.awt.*;
import java.awt.image.BufferStrategy;

public class Simulation extends Canvas implements Runnable
{
    Handler handler;
    private static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private Thread thread;
    private boolean running = false;

    public Simulation()
    {
        handler = new Handler();
        // TODO Czy handlera nie zrobić jako Singleton lub coś podobnego? Za duzo przekazywania go ...?

        Regiment r1 = new Regiment(300,300, Alliance.Blue, handler);
        /*r1.addArmyUnit(new Infantry(300,290));
        r1.addArmyUnit(new Infantry(300,300));
        r1.addArmyUnit(new Infantry(300,310));*/
        //r1.populateRegimentWithUnits(3);
        r1.formationSquare(10);


        Regiment r2 = new Regiment(600,300, Alliance.Red, handler);
        /*r2.addArmyUnit(new Infantry(600,275));
        r2.addArmyUnit(new Infantry(620,325));
        r2.addArmyUnit(new Infantry(600,325));*/
        //r2.populateRegimentWithUnits(25);
        r2.formationSquare(10);

        handler.addSimulationObject(r1);
        handler.addSimulationObject(r2);

        new Window(WIDTH, HEIGHT, "Warriors Simulation", this);
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
        double amountOfTicks = 60.0;
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
                tick();
                delta--;
            }
            if(running)
                render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                //System.out.println("FPS: "+ frames);
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
        g.fillRect(0,0, WIDTH, HEIGHT);

        handler.render(g);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args)
    {
        new Simulation();
    }

}
