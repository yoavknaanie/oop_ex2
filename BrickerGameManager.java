package bricker;

import bricker.brick_strategies.AddPuckStrategy;
import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import java.awt.*;
import java.util.Random;

import danogl.gui.UserInputListener;

public class BrickerGameManager extends GameManager {
    private static final int DISTANCE_PADDLE_FROM_BOTTOM = 30;
    private static final int BORDER_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int BALL_RADIUS = 35;
    private static final int PUCK_RADIUS = (int) Math.ceil(0.75 * BALL_RADIUS);
    private static final float BALL_SPEED = 350;
    private static final int BRICK_HEIGHT = 15;
    private static final int HEART_WIDTH = 15;
    private static final int HEART_HEIGHT = 20;
    private static final int PIXEL = 1;
    private static final int W_ASCII = 87;
    private static final int STARTING_NUM_LIVES = 3;
    private static final Renderable BORDER_RENDERABLE =
            new RectangleRenderable(new Color(80, 140, 250));
    private static final int PUCKS_NUM = 2;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private int numBricksPerRow = 8;
    private int numRows = 7;
    private int life = STARTING_NUM_LIVES;
    private static final int MAX_LIVES = 4;
    private GameObject[] life_array = new GameObject[MAX_LIVES];
    private GameObject livesNumberObject;
    private int numBricks = numBricksPerRow*numRows;
    private UserInputListener inputListener;
    private ImageReader imageReader;
    private TextRenderable textOfNumberOfLives;
    private final int MAX_NUM_PUCKS = numBricks*numRows*4;
    private Puck[] puckArr = new Puck[MAX_NUM_PUCKS];
    private SoundReader soundReader;
    public ImageReader getImageReader(){
        return imageReader;
    }
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int numBricksPerRow, int numRows) {
        super(windowTitle, windowDimensions);
        this.numBricksPerRow = numBricksPerRow;
        this.numRows = numRows;
        this.numBricks = numBricksPerRow * numRows;
    }

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowController = windowController;
        //initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        this.inputListener = inputListener;
        //create ball
        createBall();
        //create paddles
        Renderable paddleImage = imageReader.readImage(
                "assets/paddle.png", false);
        createUserPaddle(paddleImage);
        //createAIPaddle(windowDimensions, paddleImage);
        //create bricks
        createBricks();
        //create borders
        createBorders(windowDimensions);
        // init lives
        initLives();
    }


    private void initLives() {
//        todo export to another class
        Vector2 location = new Vector2(BORDER_WIDTH + PIXEL,
                windowDimensions.y() - HEART_HEIGHT);
        Renderable heartImage =
                imageReader.readImage("assets/heart.png", true);
        Vector2 dimensions = new Vector2(HEART_WIDTH, HEART_HEIGHT);
        textOfNumberOfLives = new TextRenderable(String.format("%d", life));
        this.livesNumberObject = new GameObject(location, dimensions, textOfNumberOfLives);
        setTextColor(textOfNumberOfLives);
        gameObjects().addGameObject(livesNumberObject, Layer.UI);

        for(int i=0; i<life; i++){
            location = location.add(new Vector2(HEART_WIDTH + PIXEL, 0));
            this.life_array[i] = new GameObject(location, dimensions, heartImage);
            gameObjects().addGameObject(life_array[i], Layer.UI);
        }
    }

    private void removeLives() {
        gameObjects().removeGameObject(this.livesNumberObject, Layer.UI);
        for(int i=0; i < life; i++){
            gameObjects().removeGameObject(life_array[i], Layer.UI);
            life_array[i] = null;
        }
    }

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


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
    }

    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();

        String prompt = "";
        if(numBricks == 0 || inputListener.isKeyPressed(W_ASCII)) {
            //we win
            prompt = "You win!";
        }
        if(ballHeight > windowDimensions.y()) {
            //we lost 1 life
            if (life > 0) {
                removeLives();
                life--;
                // start another game
                initLives();
                setBall(ball, false);
            }
            if (life == 0) {
                prompt = "You Lose!";
            }
        }
        if(!prompt.isEmpty()) {
            prompt += " Play again?";
            if(windowController.openYesNoDialog(prompt)) {
                life = STARTING_NUM_LIVES;
                numBricks = numBricksPerRow*numRows;
                windowController.resetGame();
            }
            else
                windowController.closeWindow();
        }
    }

    private void createBall() {
        Renderable ballImage =
                imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
        setBall(ball, false);
        gameObjects().addGameObject(ball);
    }

    public void createPucks() {
        Renderable puckImage =
                imageReader.readImage("assets/mockBall.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        for (int i =0; i < 2; i++) {
            Puck puck = new Puck(
                    Vector2.ZERO, new Vector2(PUCK_RADIUS, PUCK_RADIUS), puckImage, collisionSound);
            setBall(puck,true);
            gameObjects().addGameObject(puck);
        }
    }

    private void setBall(Ball ball, boolean puckAngle) {
        Vector2 windowDimensions = windowController.getWindowDimensions();
        ball.setCenter(windowDimensions.mult(0.5f));
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (puckAngle) {
            Random random = new Random();
            double angle = random.nextDouble() * Math.PI;
            ballVelX = (float)Math.cos(angle) * BALL_SPEED;
            ballVelY = (float)Math.sin(angle) * BALL_SPEED;
        }
        else {
            if(rand.nextBoolean())
                ballVelX *= -1;
            if(rand.nextBoolean())
                ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    private void createBricks() {
        Renderable brickImage =
                imageReader.readImage("assets/brick.png", true);
        CollisionStrategy collisionStrategy = new AddPuckStrategy(this);
//        CollisionStrategy collisionStrategy = new BasicCollisionStrategy(this);

        Vector2 location = Vector2.ZERO;
        location = location.add(new Vector2(BORDER_WIDTH + PIXEL, BORDER_WIDTH+PIXEL));
        float distanceBetweenBricks =
                (windowDimensions.x() - ((BORDER_WIDTH+PIXEL)*2)) / this.numBricksPerRow;
        // todo 2 magic_number?
        Vector2 brickDim = new Vector2(distanceBetweenBricks - PIXEL, BRICK_HEIGHT);
        for (int curRow=0; curRow < this.numRows; curRow++) {
            for (int curBrick=0; curBrick < this.numBricksPerRow; curBrick++) {
                Brick brick = new Brick(
                        location,
                        brickDim,
                        brickImage,
                        collisionStrategy);
                gameObjects().addGameObject(brick);
                location = location.add(new Vector2(distanceBetweenBricks, 0));
            }
            location = new Vector2(BORDER_WIDTH + PIXEL, BORDER_WIDTH+(BRICK_HEIGHT+PIXEL)*(curRow+1));
        }
    }

    private void createUserPaddle(Renderable paddleImage) {
        GameObject userPaddle = new UserPaddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener);

        userPaddle.setCenter(
                new Vector2(windowDimensions.x()/2,
                        (int)windowDimensions.y()-DISTANCE_PADDLE_FROM_BOTTOM));
        gameObjects().addGameObject(userPaddle);
    }

    private void createAIPaddle(Vector2 windowDimensions, Renderable paddleImage) {
        GameObject aiPaddle = new AIPaddle(
                Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                ball);
        aiPaddle.setCenter(
                new Vector2(windowDimensions.x()/2, 30));
        gameObjects().addGameObject(aiPaddle);
    }

    private void createBorders(Vector2 windowDimensions) {
        // wall border:
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        BORDER_RENDERABLE),
                Layer.STATIC_OBJECTS
        );
        gameObjects().addGameObject(
                new GameObject(
                        new Vector2(windowDimensions.x()-BORDER_WIDTH, 0),
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        BORDER_RENDERABLE),
                Layer.STATIC_OBJECTS
        );
        // ceiling border:
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        new Vector2(windowDimensions.x(), BORDER_WIDTH),
                        BORDER_RENDERABLE),
                Layer.STATIC_OBJECTS
        );
    }

    public void removeObject(GameObject gameObject) {
        this.gameObjects().removeGameObject(gameObject);
        numBricks--;
    }

    public static void main(String[] args) {
        new BrickerGameManager(
                "Bricker",
                new Vector2(700, 500),3,3).run();
    }
}
