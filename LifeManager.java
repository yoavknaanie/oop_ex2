package bricker;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;


public class LifeManager {
    public static final String HEART_IMAGE_PATH ="assets/heart.png";
    public static final int STARTING_NUM_LIVES = 3;
    private final Vector2 windowDimensions;
    private final ImageReader imageReader;
    private final BrickerGameManager brickerGameManager;
    private int life = STARTING_NUM_LIVES;
    private static final int MAX_LIVES = 4;
    private GameObject[] life_array = new GameObject[MAX_LIVES];
    private GameObject livesNumberObject;
    public static final int HEART_WIDTH = 15;
    public static final int HEART_HEIGHT = 20;
    public static final Vector2 HEART_DIM = new Vector2(HEART_WIDTH, HEART_HEIGHT);
    private TextRenderable textOfNumberOfLives;


    public LifeManager(Vector2 windowDimensions, ImageReader imageReader,
                       BrickerGameManager brickerGameManager) {
        this.windowDimensions = windowDimensions;
        this.imageReader = imageReader;
        this.brickerGameManager = brickerGameManager;
    }

    public void initLives() {
        Vector2 location = new Vector2(brickerGameManager.BORDER_WIDTH + brickerGameManager.PIXEL,
                windowDimensions.y() - HEART_HEIGHT);
        Renderable heartImage =
                imageReader.readImage(HEART_IMAGE_PATH, true);
        Vector2 dimensions = new Vector2(HEART_WIDTH, HEART_HEIGHT);
        textOfNumberOfLives = new TextRenderable(String.format("%d", life));
        this.livesNumberObject = new GameObject(location, dimensions, textOfNumberOfLives);
        setTextColor(textOfNumberOfLives);
        brickerGameManager.addGameObject(livesNumberObject, Layer.UI);

        for(int i=0; i<life; i++){
            location = location.add(new Vector2(HEART_WIDTH + brickerGameManager.PIXEL, 0));
            this.life_array[i] = new GameObject(location, dimensions, heartImage);
            brickerGameManager.addGameObject(life_array[i], Layer.UI);
        }
    }

    public void removeLives() {
        brickerGameManager.removeObject(this.livesNumberObject, Layer.UI);
        for(int i=0; i < life; i++){
            brickerGameManager.removeObject(life_array[i], Layer.UI);
            life_array[i] = null;
        }
    }


    public void addLife() {
        if (life <= 3) {
            removeLives();
            life++;
            initLives();
        }
    }

    public int getLife() {return life;}

    public void setLife(int life) {this.life = life;}

    private void setTextColor(TextRenderable numberOfLives) {
        switch (life) {
            case 3:
                numberOfLives.setColor(Color.GREEN);
                break;
            case 2:
                numberOfLives.setColor(Color.YELLOW);
                break;
            case 1:
                numberOfLives.setColor(Color.RED);
                break;
        }
    }

    public void minusLife() {
        life--;
    }

    /**
     * getter for heart image
     * @return Renderable return heart image
     */
    public Renderable getHeartImage() {
        return this.imageReader.readImage(LifeManager.HEART_IMAGE_PATH, false);
    }
}
