import java.awt.*;
import java.awt.image.BufferStrategy;

public class Simulation extends Canvas implements Runnable
{
    Handler handler;
    private static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    // Apka działa na razie(?) na 1 wątku
    private Thread thread;
    // Czy wątek chodzi
    private boolean running = false;

    public Simulation()
    {
        // Handler -> obsluguje wszystkie obiekty
        // TODO Czy handlera nie zrobić jako Singleton lub coś podobnego? Za duzo przekazywania go ...?
        handler = new Handler();
        Regiment r1 = new Regiment(100,100, Alliance.Blue, handler);
        Regiment r2 = new Regiment(300,100, Alliance.Red, handler);
        r1.addArmyUnit(new Infantry(100,100));
        r2.addArmyUnit(new Infantry(300,200));
        handler.addSimulationObject(r1);
        handler.addSimulationObject(r2);

        // Window to nasza klasa
        new Window(WIDTH, HEIGHT, "Warriors Simulation", this);
    }

    // To Ci na razie nie powiem xD
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
        // Game Loop -> tego nie wytłumacze zbytnio xD
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
        // End Game Loop
    }

    // Co ma się dziać w "takcie zegarowym" symulacji
    private void tick(){
        handler.tick();
    }

    private void render(){
        // Te buffer strategy to też średnio wiem co to xD (z tutoriala)
        BufferStrategy bs = this.getBufferStrategy();
        if(bs==null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        //Rysowanie tła
        g.setColor(Color.black);
        g.fillRect(0,0, WIDTH, HEIGHT);

        //Renderowanie całej reszty
        handler.render(g);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args)
    {
        new Simulation();
    }

}
