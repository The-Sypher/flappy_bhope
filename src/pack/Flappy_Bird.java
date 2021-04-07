package pack;

import java.awt.*;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.io.File;

public class Flappy_Bird implements ActionListener, MouseListener, KeyListener
{
    public Random rand;

    public final int WIDTH = 1200, HEIGHT = 700;

    public int ticks, ymotion, score;

    public ArrayList<Rectangle> columns;

    BufferedImage img;

    public static Flappy_Bird call;

    public boolean gameOver, started;

    public renderer render;

    static String ap;

    public Rectangle bird;

    static void setAP()
    {
        File f = new File("tp.txt");
        ap = f.getAbsolutePath();
        f.delete();
        ap = ap.substring(0, ap.length() - 6);
    }

    public void play_woosh()
    {
        String soundName = ap+"assets\\sounds\\tading.wav";
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }

        catch (IOException e){e.printStackTrace();}
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }
    public void play_tading()
    {
        String soundName = ap+"assets\\sounds\\woosh.wav";
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }

        catch (IOException e){e.printStackTrace();}
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public Flappy_Bird()
    {
        setAP();
        try
        {
            String path = ap + "assets\\images\\bird.png";
            img = ImageIO.read(new File(path));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        JFrame frame = new JFrame();

        render = new renderer();
        rand = new Random();

        frame.add(render);
        frame.addMouseListener(this);
        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setTitle("Flappy Bird");
        frame.setVisible(true);
        frame.setResizable(false);

        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);

        columns = new ArrayList<>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

    }

    public void set_call(Flappy_Bird set) {
        call = set;
    }

    public void time_start() {
        Timer time = new Timer(20, this);
        time.start();
    }

    public void addColumn(boolean start)
    {
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        if (start)
        {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else
        {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }

    public void jump()
    {
        if (gameOver)
        {

            bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();

            ymotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        }
        if (!started)
        {
            started = true;
        } else if (!gameOver)
        {
            if (ymotion > 0)
            {
                ymotion = 0;
            }
            play_tading();
            ymotion -= 10;

        }
    }

    public void paintColumn(Graphics g, Rectangle column)
    {
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void repaint(Graphics g)
    {
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);//fill background

        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - 120, WIDTH, 150);//make ground

        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);//make grass

        g.setColor(Color.red);
        g.drawImage(img, bird.x, bird.y, null);

        for (int i = 0; i < columns.size(); i++)//loop will run through each column in column array
        {
            paintColumn(g, columns.get(i));//draw column onto frame
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 100));

        if (gameOver)
        {
            g.drawString("Game Over!", WIDTH/2-300, HEIGHT / 2 - 50);
        }

        if (!gameOver)
        {
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }

        if (!started)
        {
            g.drawString("Click to Start", WIDTH/2-300, HEIGHT / 2 - 50);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        ticks++;
        int speed = 10;
        if (started)//check if game is started
        {

            for (int i = 0; i < columns.size(); i++)//move each column 10 pixels every tick
            {
                Rectangle column = columns.get(i);
                column.x -= speed;
            }
            if (ticks % 2 != 1 && ymotion < 15)//gravity and accelaration
            {
                ymotion += 2;
            }

            for (int i = 0; i < columns.size(); i++)//column update
            {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0)//if one set column i.e top and bottom move out of frame
                {
                    columns.remove(column);// remove column from array and thus not be rendered
                    if (column.y == 0)
                    {
                        addColumn(false);//add 1 set of columns
                    }
                }
            }

            bird.y += ymotion;//move bird down by ymotion

            for (Rectangle column : columns)
            {
                if (column.y == 0 && bird.x + (bird.width / 2) > column.x + column.width / 2 - (bird.width / 2)
                        && bird.x + bird.width / 2 < column.x + column.width / 2 + (bird.width / 2))
                //check if bird is in middle of columns
                {
                    score++;
                    play_woosh();

                }
                if (column.intersects(bird))//check if bird has collided with columns
                {
                    gameOver = true;
                    bird.x = column.x - bird.width;

                }
            }

            if (bird.y + bird.height >= HEIGHT - 120 || bird.y <= 0)//check if bird has touched bottom of play area
            {
                gameOver = true;
            }
            if (bird.y + ymotion >= HEIGHT - 120)//if bird is going to fall out of grass
            {
                bird.y = HEIGHT - 120 - bird.height;//set bird at grass
            }
        }
        render.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e)//accomodate mouse click
    {
        jump();
    }

    @Override
    public void keyReleased(KeyEvent e)// space bar or up arrow
    {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP)
        {
            jump();
        }

    }
    //ignore following functions
    //Added coz java giving exception
    @Override
    public void mousePressed(MouseEvent e)
    {
        //Don't Touch Unless you know what you're doing
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        //Don't Touch Unless you know what you're doingP
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        //Don't Touch Unless you know what you're doing
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        //Don't Touch Unless you know what you're doing
    }


    @Override
    public void keyPressed(KeyEvent e)
    {
        //Don't Touch Unless you know what you're doing
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        //Don't Touch Unless you know what you're doing
    }

    public static void main(String args[])
    {
        setAP();
        System.out.println(ap);
        call = new Flappy_Bird();
    }

}