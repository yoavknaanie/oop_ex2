package bricker;

import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.CollisionStrategyFactory;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import java.awt.*;
import java.util.Random;
import danogl.gui.UserInputListener;
import java.util.concurrent.ThreadLocalRandom;

// todo: the last brick disapear
// todo: make sure extraPaddle disapear after 4 collissions
// todo  Checks the puck is out from the window bounds.

// double brick

public class BrickerGameManager extends GameManager {
    public static final String HEART_IMAGE_PATH ="assets/heart.png";
    public static final String BALL_IMAGE_PATH = "assets/ball.png";
    public static final String BALL_TAG = "ball_tag";
    public static final String PACK_TAG = "pack_tag";
    public static final String EXTRA_PADDLE_TAG = "extra_paddle";
    public static final String USER_PADDLE_TAG = "user_paddle";
    public static final String BRICK_TAG = "brick_tag";
    private static final int DISTRIBUTION_CONST = 6;
    private static final int DISTANCE_PADDLE_FROM_BOTTOM = 30;
    private static final int BORDER_WIDTH = 20;
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int BALL_RADIUS = 35;
    private static final int PUCK_RADIUS = (int) Math.ceil(0.75 * BALL_RADIUS);
    private static final float BALL_SPEED = 250;
//    private static final float BALL_SPEED = 200;
    private static final int BRICK_HEIGHT = 15;
    public static final int HEART_WIDTH = 15;
    public static final int HEART_HEIGHT = 20;
    public static final Vector2 HEART_DIM = new Vector2(HEART_WIDTH, HEART_HEIGHT);
    private static final int PIXEL = 1;
    private static final int W_ASCII = 87;
//    turbo consts
    public static final float MULT_CONST = (float) 1.4;
    private static final String RED_BALL_PATH = "assets/redball.png";
    private static final int NO_TURBO_CONST = -10;
    private static final int MINUS_BRICK = -1;
    private int collisionAtStartTurbo = NO_TURBO_CONST;
    private static final int STARTING_NUM_LIVES = 3;
    private static final Renderable BORDER_RENDERABLE =
            new RectangleRenderable(new Color(80, 140, 250));
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private int numBricksPerRow = 8;
    private int numRows = 7;
    private int life = STARTING_NUM_LIVES;
    private static final int MAX_LIVES = 4;
    private GameObject[] life_array = new GameObject[MAX_LIVES];
    private GameObject livesNumberObject;
    private danogl.util.Counter numBricks;
    private UserInputListener inputListener;
    private ImageReader imageReader;
    private TextRenderable textOfNumberOfLives;
    private final int MAX_NUM_PUCKS = numBricksPerRow*numRows*4;
    private Puck[] puckArr = new Puck[MAX_NUM_PUCKS];
    private SoundReader soundReader;


    public ImageReader getImageReader(){
        return imageReader;
    }
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int numBricksPerRow, int numRows) {
        super(windowTitle, windowDimensions);
        this.numBricksPerRow = numBricksPerRow;
        this.numRows = numRows;
        this.numBricks = new danogl.util.Counter(numBricksPerRow * numRows);
    }

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.numBricks = new danogl.util.Counter(numBricksPerRow * numRows);
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
        //create paddle
        createUserPaddle();
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
                imageReader.readImage(HEART_IMAGE_PATH, true);
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

    public void addLife() {
        if (life <= 3) {
            removeLives();
            life++;
            initLives();
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
        checkTurbo();
        checkPucks();
    }

    private void checkPucks() {
        for(Ball puck: puckArr) {
            if (puck != null) {
                if (puck.getCenter().x() > windowDimensions.x() ||
                        puck.getCenter().x() < 0 ||
                        puck.getCenter().y() > windowDimensions.y() ||
                        puck.getCenter().y() < 0) {
                    removeObject(puck);
                }
            }
        }
    }

    private void checkTurbo() {
        if (collisionAtStartTurbo + 6 == ball.getCollisionCounter()) {
            collisionAtStartTurbo = NO_TURBO_CONST;
            Renderable ballImg = this.getImageReader().readImage(BALL_IMAGE_PATH, true);
            ball.renderer().setRenderable(ballImg);
            ball.setVelocity(ball.getVelocity().mult(1/MULT_CONST));
        }
    }

    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();

        String prompt = "";
        if(numBricks.value() <= 0 || inputListener.isKeyPressed(W_ASCII)) {
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
                numBricks = new danogl.util.Counter(numBricksPerRow*numRows);
                windowController.resetGame();
            }
            else
                windowController.closeWindow();
        }
    }

    private void createBall() {
        Renderable ballImage =
                imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
        setBall(ball, false);
        ball.setTag(BALL_TAG);
        gameObjects().addGameObject(ball);
    }
    public void createPucks(Vector2 brickLocation) {
        Renderable puckImage =
                imageReader.readImage("assets/mockBall.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        for (int i =0; i < 2; i++) {
            Puck puck = new Puck(
                    Vector2.ZERO, new Vector2(PUCK_RADIUS, PUCK_RADIUS), puckImage, collisionSound);
            setBall(puck,true);
            puck.setTag(PACK_TAG);
            gameObjects().addGameObject(puck);
            puck.setCenter(brickLocation);
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

    public static BrickType getRandomBrickType() {
        BrickType[] samplingArray = new BrickType[] {
                BrickType.ADD_PACK_TYPE,
                BrickType.ADD_PADDLE_TYPE,
                BrickType.TURBO_TYPE,
                BrickType.EXTRA_LIFE,
                BrickType.DOUBLE_TYPE,
                BrickType.BASIC_TYPE,
                BrickType.BASIC_TYPE,
                BrickType.BASIC_TYPE,
                BrickType.BASIC_TYPE,
                BrickType.BASIC_TYPE,
        };
        int randomNumber = ThreadLocalRandom.current().nextInt(samplingArray.length);
        return samplingArray[randomNumber];
//        return BrickType.DOUBLE_TYPE;
    }

    private void createBricks() {
        Renderable brickImage =
                imageReader.readImage("assets/brick.png", true);
        Vector2 location = Vector2.ZERO;
        location = location.add(new Vector2(BORDER_WIDTH + PIXEL, BORDER_WIDTH+PIXEL));
        float distanceBetweenBricks =
                (windowDimensions.x() - ((BORDER_WIDTH+PIXEL)*2)) / this.numBricksPerRow;
        // todo 2 magic_number?
        Vector2 brickDim = new Vector2(distanceBetweenBricks - PIXEL, BRICK_HEIGHT);
        CollisionStrategyFactory collisionStrategyFactory =
                new CollisionStrategyFactory(this);
        BrickType brickType;
        CollisionStrategy collisionStrategy;

        for (int curRow=0; curRow < this.numRows; curRow++) {
            for (int curBrick=0; curBrick < this.numBricksPerRow; curBrick++) {
                brickType = getRandomBrickType();
                collisionStrategy = collisionStrategyFactory.getCollisionStrategy(brickType);
                Brick brick = new Brick(location, brickDim, brickImage, collisionStrategy);
//                brick.setTopLeftCorner(location);
                gameObjects().addGameObject(brick);
                location = location.add(new Vector2(distanceBetweenBricks, 0));
            }
            location = new Vector2(BORDER_WIDTH + PIXEL, BORDER_WIDTH+(BRICK_HEIGHT+PIXEL)*(curRow+1));
        }
    }

    private void createUserPaddle() {
        Renderable paddleImage = imageReader.readImage(
                "assets/paddle.png", false);
        GameObject userPaddle = new UserPaddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener,
                BORDER_WIDTH,
                (int) windowDimensions.x() - BORDER_WIDTH);

        userPaddle.setCenter(
                new Vector2(windowDimensions.x()/2,
                        (int)windowDimensions.y()-DISTANCE_PADDLE_FROM_BOTTOM));
        userPaddle.setTag(USER_PADDLE_TAG);
        gameObjects().addGameObject(userPaddle);
    }

    public void createExtraPaddle() {
        Renderable paddleImage = imageReader.readImage(
                "assets/paddle.png", false);
        GameObject extraPaddle = new ExtraPaddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener,
                BORDER_WIDTH,
                (int) windowDimensions.x() - BORDER_WIDTH,
                this);

        extraPaddle.setCenter(
                new Vector2(windowDimensions.x()/2, (int)(windowDimensions.y()/2)));
        extraPaddle.setTag(EXTRA_PADDLE_TAG);
        gameObjects().addGameObject(extraPaddle);
    }

    public GameObjectCollection getGameObjects() {
        return gameObjects();
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

    public void removeBrick(GameObject gameObject) {
        if (removeObject(gameObject)){
            numBricks.increaseBy(MINUS_BRICK);
            System.out.println(numBricks.value());
        }
    }

    public void addGameObject(GameObject obj, int layer) {
        gameObjects().addGameObject(obj, layer);
    }

    public boolean removeObject(GameObject gameObject) {
        return this.gameObjects().removeGameObject(gameObject);
    }



// todo erease
    public GameObject findObject(String tag) {
        for (GameObject obj: gameObjects()) {
            if (tag.equals(obj.getTag())) {
                return obj;
            }
        }
        return null;
    }

    public void startTurbo() {
        if (collisionAtStartTurbo == NO_TURBO_CONST)
        {
            ball.setVelocity(ball.getVelocity().mult(MULT_CONST));
            Renderable redBallImage = getImageReader().readImage(RED_BALL_PATH, true);
            ball.renderer().setRenderable(redBallImage);
            collisionAtStartTurbo = this.ball.getCollisionCounter();
        }
    }

    public Renderable getHeartImage() {
        return this.getImageReader().readImage(HEART_IMAGE_PATH, false);
    }

    public static void main(String[] args) {
        new BrickerGameManager(
                "Bricker",
                new Vector2(700, 500)).run();
    }
}
