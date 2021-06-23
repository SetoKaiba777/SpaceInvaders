package spaceinvadersfx;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Núcleo principal do jogo. 
 * 
 * @author Caio E. Oliveira
 */
public class SpaceInvadersFX extends Application {
    
    private Alien[][] enemies = new Alien[5][11];
    private Alien[][] enemiesMoved = new Alien[5][11];
    private Alien[][] currentEnemies;
    private int SCENE_WIDTH = 600;
    private int APP_HEIGHT = 600;
    private int APP_WIDTH = 800;
    private int SPACE = 40;
    private int coordinateY = 80;
    private int coordinateX = APP_WIDTH/3 - (SPACE*3);
    private int playerLives = 3;
    private int score = 0;
    private int totalEnemies;
    private int currentMoveSound = 0;
    private int rectangleSize = 8;
    private  double i=2.0;
    private double time = 0.0;
    private double elapsedTime, explosionTime, restartTime, lastAlienPosY,
            maxShiftLeft, maxShiftRight;
    private boolean SHIFTING_RIGHT, SHIFTING_LEFT, PLAYER_SHOT,
            GAME_IS_PAUSED, LIFE_END, MISSILE_LAUNCHED, EXPLOSION;
    private Text playerLivesLabel, scoreLabel, pointsLabel, gameOverLabel;
    private GraphicsContext gc;
    private Sprite secondTank, thirdTank, lastAlien, explosion;
    private Ship tank;
    private Canvas gameCanvas;
    private LongValue startNanoTime;
    private AnimationTimer timer;
    private SoundEffect  shootEffect, killEffect, explosionEffect;
    private Group barreiraGroup = new Group();
    private Group secondBarreira = new Group();
    private Group thirdBarreira = new Group();
    private Group fourthBarreira = new Group();
    private ArrayList<Barreira> barreiras = new ArrayList<>();
    private ArrayList<ShotShip> missiles = new ArrayList<>();
    private ArrayList<Sprite> alienBombs = new ArrayList<>();
    private ArrayList<SoundEffect> moveEffects = new ArrayList<>();
    private MenuBox menuBox;
    private Group root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Space Invaders FX");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> {
            timer.stop();
            MainApp.GAME_SET = false;
            primaryStage.close();
        });

        root = new Group();
        gameCanvas = new Canvas(APP_WIDTH, APP_HEIGHT);
        gc = gameCanvas.getGraphicsContext2D();

        Scene mainScene = new Scene(root);
        mainScene.setFill(Color.BLACK);
        setGUI();
        setMenuBox();

        root.getChildren().addAll(gameCanvas, scoreLabel, pointsLabel, playerLivesLabel,
                barreiraGroup, secondBarreira, thirdBarreira, fourthBarreira);
        SHIFTING_RIGHT = true;

        spawnEnemies();
        setMovedEnemies();
        updateCurrentEnemies();
        setBarreiras();
        setPlayer();
        setInitialLives();
        setSoundEffects();
        startGame();

        primaryStage.setScene(mainScene);
        primaryStage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                if (!LIFE_END && tank.getPositionX() > 50 && !GAME_IS_PAUSED) {
                    moveTankLeft();
                }
            } else if (!LIFE_END && e.getCode() == KeyCode.RIGHT) {
                if (tank.getPositionX() < APP_WIDTH - 100 && !GAME_IS_PAUSED) {
                    moveTankRight();
                }
            } else if (e.getCode() == KeyCode.ESCAPE) {
                GAME_IS_PAUSED = !GAME_IS_PAUSED;
                if (GAME_IS_PAUSED) {
                    timer.stop();
                } else {
                    timer.start();
                }
            }
        });

        primaryStage.getScene().setOnKeyReleased(e -> {
            if (!LIFE_END && e.getCode() == KeyCode.SPACE && !MISSILE_LAUNCHED) {
                shootEffect.playClip();
                shootMissile();
                MISSILE_LAUNCHED = true;
            } else if (!LIFE_END && e.getCode() == KeyCode.LEFT) {
                tank.setVelocity(0, 0);
            } else if (!LIFE_END && e.getCode() == KeyCode.RIGHT) {
                tank.setVelocity(0, 0);
            }
        });
        primaryStage.show();
    }

    private void setGUI() {
        playerLivesLabel = new Text("VIDAS:");
        playerLivesLabel.setFill(Color.WHITE);
        playerLivesLabel.setFont(Font.font("Monaco", FontWeight.EXTRA_BOLD, 20));
        playerLivesLabel.setX(SCENE_WIDTH + SPACE);
        playerLivesLabel.setY(30);

        scoreLabel = new Text("Placar:");
        scoreLabel.setFill(Color.WHITE);
        scoreLabel.setFont(Font.font("Monaco", FontWeight.EXTRA_BOLD, 20));
        scoreLabel.setX(10);
        scoreLabel.setY(30);

        pointsLabel = new Text(Integer.toString(score));
        pointsLabel.setFill(Color.LIMEGREEN);
        pointsLabel.setFont(Font.font("Monaco", FontWeight.EXTRA_BOLD, 20));
        pointsLabel.setX(95);
        pointsLabel.setY(30);

        gameOverLabel = new Text("GAME OVER");
        gameOverLabel.setFill(Color.WHITE);
        gameOverLabel.setFont(Font.font("Monaco", FontWeight.THIN, 30));
        gameOverLabel.setX(APP_WIDTH/2 - 2*SPACE);
        gameOverLabel.setY (30);
    }

    private void spawnEnemies() {
        for (int y = 80, i = 0; y < APP_HEIGHT / 2 + SPACE && i < 5; y += SPACE, i++) {
            for (int x = APP_WIDTH/3 - (SPACE*3), j = 0; x < 660 && j < 11; x += SPACE + 20, j++) {
                if (y < 90) {
                    enemies[i][j] = new Alien(x, y, "/images/small_invader_a.png");
                    gc.drawImage(enemies[i][j].getImage(), x, y);
                } else if (y < 200) {
                    enemies[i][j] = new Alien(x, y, "/images/medium_invader_a.png");
                    gc.drawImage(enemies[i][j].getImage(), x, y);
                } else {
                    enemies[i][j] = new Alien(x, y, "/images/large_invader_a.png");
                    gc.drawImage(enemies[i][j].getImage(), x, y);
                }
                totalEnemies++;
            }
        }
    }

    private void setMovedEnemies() {
        for (int y = 80, i = 0; y < APP_HEIGHT / 2 + SPACE && i < 5; y += SPACE, i++) {
            for (int x = APP_WIDTH/3 - (SPACE*3), j = 0; x < 660 && j < 11; x += SPACE + 20, j++) {
                if (y < 90) {
                    enemiesMoved[i][j] = new Alien(x, y, "/images/small_invader_b.png");
                } else if (y < 200) {
                    enemiesMoved[i][j] = new Alien(x, y, "/images/medium_invader_b.png");
                } else {
                    enemiesMoved[i][j] = new Alien(x, y, "/images/large_invader_b.png");
                }
            }
        }
    }


    private void setBarreiras() {
        for (int x = 180, i = 0; x < SCENE_WIDTH; x += 3*SPACE, i++) {
            Barreira barreira = new Barreira();
            int[][] barreiraMatrix = barreira.getBarreira();
            Group group = getBarreiraGroup(x);
            renderBarreira(x, barreira, barreiraMatrix, group);
        }
    }

    private void renderBarreira(int startingX, Barreira barreira, int[][] barreiraMatrix, Group group) {
        barreira.setLocationX(startingX);
        barreira.setLocationY(APP_HEIGHT-150);
        barreiras.add(barreira);
        group.getChildren().clear();

        for (int i = 0, y = APP_HEIGHT - 150; i < barreiraMatrix.length; i++, y += rectangleSize) {
            for (int j = 0, x = startingX; j < barreiraMatrix[0].length && x <= SCENE_WIDTH + SPACE; j++, x += rectangleSize) {
                if (barreiraMatrix[i][j] != 0) {
                    Rectangle rect = new Rectangle();
                    rect.setFill(barreira.getColor());
                    rect.setWidth(rectangleSize);
                    rect.setHeight(rectangleSize);
                    rect.relocate(x, y);
                    group.getChildren().add(rect);
                }
            }
        }
    }

    private void setPlayer() {
        tank = new Ship(APP_WIDTH,SPACE, APP_HEIGHT,gc);
    }

    private void setInitialLives() {
        secondTank = new Sprite();
        secondTank.setImage("/images/tank.png");
        secondTank.setPosition(SCENE_WIDTH + 3 * SPACE, 10);
        secondTank.render(gc);

        thirdTank = new Sprite();
        thirdTank.setImage("/images/tank.png");
        thirdTank.setPosition(SCENE_WIDTH + 4 * SPACE, 10);
        thirdTank.render(gc);
    }
    /**Inicia o jogo*/
    private void startGame() {
        startNanoTime = new LongValue(System.nanoTime());

        timer = new AnimationTimer() {
            public void handle(long now) {
                elapsedTime = (now - startNanoTime.value) / 1000000000.0;
                startNanoTime.value = now;

                gc.clearRect(0, 30, APP_WIDTH, APP_HEIGHT);

                if (explosion != null) {
                    explosion.render(gc);
                }

                lastAlien = getLastAlien();
                checkLastAlienStatus();
                animateEnemies();
                checkMissileStatus();
                checkBombStatus();
                checkTankStatus();
                getMaxShiftSpace();

                time += 1/(totalEnemies * i);

                if (time >= 1.5 && playerLives >= 0 && !LIFE_END) {
                    playMoveEffect();
                    if (SHIFTING_RIGHT) {
                        if (maxShiftRight < 640) {
                            coordinateX += 15;
                        } else {
                            coordinateY += 15;
                            SHIFTING_RIGHT = false;
                        }
                    } else if (!SHIFTING_RIGHT) {
                        if (maxShiftLeft > 80) {
                            coordinateX -= 15;
                        } else {
                            coordinateY += 15;
                            SHIFTING_RIGHT = true;
                        }
                    }
                    updateCurrentEnemies();
                    shootPlayer();
                    time = 0;
                }

                if (EXPLOSION) {
                    explosionTime += 0.020;
                    if (explosionTime >= .50) {
                        EXPLOSION = false;
                        explosion = null;
                        explosionTime = 0;
                    }
                }


                if (LIFE_END && playerLives > 0) {
                    restartTime += 0.010;
                    if (restartTime >= 0.70) {
                        restartTime = 0;
                        restartGame();
                    }
                } else if (LIFE_END && playerLives == 0) {
                    timer.stop();
                    i=2.0;
                    root.getChildren().add(gameOverLabel);
                    root.getChildren().add(menuBox);
                }
            }
        };
        timer.start();
    }
    /**controlador de dificuldade*/
    private void updateTime(){
        i -=0.1;
        if(i <= 0.0){
            i=0.1;
        }        
    }
    
    private void checkLastAlienStatus() {
        if (lastAlien != null) {
            lastAlienPosY = lastAlien.getPositionY();
            if (lastAlienPosY >= APP_HEIGHT - 150 - lastAlien.getHeight()) {
                updatePlayerLives();
                timer.stop();
                updateTime();
                startNewGame();
            }
        }
    }

    private void getMaxShiftSpace() {
        maxShiftLeft = 0.00;
        maxShiftRight = 0.00;
        //looking at the far left side
        for (int i = 0; i < currentEnemies.length; i++) {
            for (int j = 0; j < currentEnemies[0].length; j++) {
                if (currentEnemies[i][j] != null) {
                    if (maxShiftLeft > 0.00) {
                        maxShiftLeft = Math.min(maxShiftLeft, currentEnemies[i][j].getPositionX());
                    } else {
                        maxShiftLeft = currentEnemies[i][j].getPositionX();
                    }
                    break;
                }
            }
        }
        //looking at the far right side
        for (int i = 0; i < currentEnemies.length; i++) {
            for (int j = currentEnemies[0].length - 1; j >= 0; j--) {
                if (currentEnemies[i][j] != null) {
                    if (maxShiftRight > 0.00) {
                        maxShiftRight = Math.max(maxShiftRight, currentEnemies[i][j].getPositionX());
                    } else {
                        maxShiftRight = currentEnemies[i][j].getPositionX();
                    }
                    break;
                }
            }
        }

    }

    private void checkTankStatus() {
        if (tank != null) {
            if (tank.getPositionX() < 50) {
                tank.setPosition(tank.getPositionX() + 1, tank.getPositionY());
                tank.setVelocity(0, 0);
            } else if (tank.getPositionX() > APP_WIDTH - 100) {
                tank.setPosition(tank.getPositionX() - 1, tank.getPositionY());
                tank.setVelocity(0, 0);
            }

            tank.render(gc);
            tank.update(elapsedTime);
        }
    }




    private void checkMissileStatus() {
        if (MISSILE_LAUNCHED) {
            Sprite missile = missiles.get(0);
            missile.render(gc);
            missile.update(elapsedTime);
            if (missileHit() || missile.getPositionY() <= 30 || barreiraHit(missile)) {
                missiles.clear();
                MISSILE_LAUNCHED = false;
                if (totalEnemies == 0) {
                    timer.stop();
                    startNewGame();
                }
            }
        }
    }

    private void checkBombStatus() {
        if (PLAYER_SHOT) {
            Sprite bomb = alienBombs.get(0);
            bomb.render(gc);
            bomb.update(elapsedTime);
            if (bomb.getPositionY() >= APP_HEIGHT - SPACE ||
                    barreiraHit(bomb) || bombsCollide(bomb)) {
                alienBombs.clear();
                PLAYER_SHOT = false;
            }
            if (playerHit(bomb)) {
                LIFE_END = true;
            }
        }
    }

    private boolean bombsCollide(Sprite bomb) {
        if (missiles.size() > 0) {
            if (bomb.intersects(missiles.get(0))) {
                setExplosion(bomb);
                killEffect.playClip();
                missiles.clear();
                MISSILE_LAUNCHED = false;
                return true;
            }
        }
        return false;
    }

    private void updateCurrentEnemies() {
        currentEnemies = currentEnemies == enemies ? enemiesMoved : enemies;
    }

    private void animateEnemies() {
        for (int y = coordinateY, i = 0; y < APP_HEIGHT - 100  && i < 5; y += SPACE, i++) {
            for (int x = coordinateX, j = 0; x < 700 && j < 11; x += SPACE + 20, j++) {
                if (currentEnemies[i][j] != null) {
                    currentEnemies[i][j].setPosition(x, y);
                    if (y < 90) {
                        gc.drawImage(currentEnemies[i][j].getImage(), x, y);
                    } else if (y < 200) {
                        gc.drawImage(currentEnemies[i][j].getImage(), x, y);
                    } else {
                        gc.drawImage(currentEnemies[i][j].getImage(), x, y);
                    }
                }
            }
        }
    }

    private Sprite getLastAlien() {
        for (int i = currentEnemies.length - 1; i >= 0; i --) {
            for (int j = 0; j < currentEnemies[0].length; j++) {
                if (currentEnemies[i][j] != null) {
                    return currentEnemies[i][j];
                }
            }
        }
        return null;
    }


    private void moveTankLeft() {
        tank.setVelocity(-250, 0);
    }

    private void moveTankRight() {
        tank.setVelocity(250, 0);
    }

    private void shootMissile() {
        ShotShip missile = new ShotShip(tank, gc);
        missiles.add(missile);
    }

    private boolean missileHit() {
        for (int i = 0; i < currentEnemies.length; i++) {
            for (int j = 0; j < currentEnemies[0].length; j++) {
                if (currentEnemies[i][j] != null) {
                    if (currentEnemies[i][j].intersects(missiles.get(0))) {
                        switch ((int)currentEnemies[i][j].getWidth()) {
                            case 31:
                                score += 10;
                                break;
                            case 28:
                                score += 20;
                                break;
                            case 21:
                                score += 30;
                                break;
                        }
                        setExplosion(currentEnemies[i][j]);
                        explosion.render(gc);
                        killEffect.playClip();
                        updateTotalScore();
                        totalEnemies--;
                        currentEnemies[i][j] = null;
                        enemies[i][j] = null;
                        enemiesMoved[i][j] = null;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean barreiraHit(Sprite ammo) {
        for (Barreira barreira : barreiras) {
            int[][] matrix = barreira.getBarreira();
            for (int row = 0; row < matrix.length; row++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if (matrix[row][j] != 0) {
                        if (ammo.getPositionX() >= barreira.getLocationX() -5 &&
                                ammo.getPositionX() <= barreira.getLocationX() + j * rectangleSize + 5 &&
                                ammo.getPositionY() >= barreira.getLocationY() - 5 &&
                                ammo.getPositionY() <= barreira.getLocationY() + row * rectangleSize + 5) {
                            barreira.deleteBricksAround(row, j);
                            Group group = getBarreiraGroup((int)barreira.getLocationX());
                            renderBarreira((int)(barreira.getLocationX()), barreira, matrix, group);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private Group getBarreiraGroup(int x) {
        if (x <= 180) {
            return barreiraGroup;
        } else if (x <= 300) {
            return secondBarreira;
        } else if (x <= 420) {
            return thirdBarreira;
        } else {
            return fourthBarreira;
        }
    }
    /**Caso o jogador seja atingido pelo tiro inimigo*/
    private void shootPlayer() {
        AlienShot alienBomb = new AlienShot();
        for (int i = (int)(Math.random() * currentEnemies.length - 1); i >= 0; i--) {
            if (PLAYER_SHOT) {
                break;
            }
            for (int j = (int)(Math.random() * currentEnemies[i].length); j >= 0; j--) {
                if (currentEnemies[i][j] != null) {
                    alienBomb.setPosition(currentEnemies[i][j].getPositionX(),
                            currentEnemies[i][j].getPositionY());
                    alienBomb.setVelocity(0, 350);
                    alienBombs.add(alienBomb);
                    PLAYER_SHOT = true;
                    break;
                }
            }
        }
    }
    /**Caso o player seja atingido*/
    private boolean playerHit(Sprite bomb) {
        if (tank != null && tank.intersects(bomb)) {
            updatePlayerLives();
            setExplosion(tank);
            explosion.render(gc);
            explosionEffect.playClip();
            tank = null;
            return true;
        }
        return false;
    }
    
    /**Ativa o sprite da explosao quando necessario*/
    private void setExplosion(Sprite victim) {
        EXPLOSION = true;
        explosion = new Sprite();
        explosion.setImage(true, "/images/explosion.gif");
        explosion.setPosition(victim.getPositionX() - 10, victim.getPositionY() - 10);
        explosion.render(gc);
    }
    
    /**Atualiza a pontuacao*/
    private void updateTotalScore() {
        pointsLabel.setText(Integer.toString(score));
    }
    
    /**Atualiza as vidas do jogador*/
    private void updatePlayerLives() {
        playerLives--;
        if (playerLives == 2) {
            gc.clearRect(SCENE_WIDTH + 4 * SPACE, 10, APP_WIDTH, 20);
        } else if (playerLives == 1) {
            gc.clearRect(SCENE_WIDTH + 3 * SPACE, 10, APP_WIDTH, 20);
        }
    }
    
    /**Reseta os status do jogador quando necessario*/
    private void resetPlayerStatus() {
        playerLives = 3;
        score = 0;
        updateTotalScore();
        secondTank.render(gc);
        thirdTank.render(gc);
    }
    
    /**Inicia novo jogo*/
    private void startNewGame() {
        timer.stop();
        if (playerLives < 1) {
            removeGameOverItems();
        }
        coordinateY = 80;
        coordinateX = APP_WIDTH/3 - (SPACE*3);
        resetGameVariables();
        spawnEnemies();
        setMovedEnemies();
        updateCurrentEnemies();
        barreiras.clear();
        setBarreiras();
        setPlayer();
        startGame();
    }
    
    /**Remove itens do gameover anterior para um novo jogo ser iniciado*/
    private void removeGameOverItems() {
        resetPlayerStatus();
        root.getChildren().remove(gameOverLabel);
        root.getChildren().remove(menuBox);
    }
    
    /**Reloca os inimigos quando necessario*/
    /*private void relocateEnemies() {
        coordinateY = 80;
        coordinateX = APP_WIDTH/3 - (SPACE*3);
        setPlayer();
        resetGameVariables();
        startGame();
    }
    
    /**Recomeca o jogo*/
    private void restartGame() {
        timer.stop();
        setPlayer();
        resetGameVariables();
        startGame();
    }
    
    /**Recomeca as variaveis de jogo quando necessario*/
    private void resetGameVariables() {
        time = 0;
        LIFE_END = false;
        SHIFTING_LEFT = false;
        PLAYER_SHOT = false;
        MISSILE_LAUNCHED = false;
        EXPLOSION = false;
        missiles.clear();
        alienBombs.clear();
    }
    
    /**Coloca os efeitos sonoros nos locais necessarios*/
    private void setSoundEffects() {
        shootEffect = new SoundEffect("/sounds/shoot.wav");
        killEffect = new SoundEffect("/sounds/alienKilled.wav");
        explosionEffect = new SoundEffect("/sounds/explosion.wav");
        setAlienMoveSounds();
    }
    
    /**Coloca os efeitos sonoros da movimentacao dos aliens nos locais necessarios*/
    private void setAlienMoveSounds() {
        moveEffects.add(new SoundEffect("/sounds/alienMove.wav"));
        moveEffects.add(new SoundEffect("/sounds/alienMove2.wav"));
        moveEffects.add(new SoundEffect("/sounds/alienMove3.wav"));
        moveEffects.add(new SoundEffect("/sounds/alienMove4.wav"));
    }
    
    /**efeitos sonoros de movimentacao*/
    private void playMoveEffect() {
        if (currentMoveSound == moveEffects.size()) {
            currentMoveSound = 0;
        }

        moveEffects.get(currentMoveSound).playClip();
        currentMoveSound++;
    }
    
    /***/
    public void setMenuBox() {
        menuBox = new MenuBox(400, 100);
        menuBox.setTranslateX(200);
        menuBox.setTranslateY(300);

        MenuItem startButton = new MenuItem("NOVO JOGO", 250);
        startButton.setOnAction(() -> startNewGame());

        MenuItem quitGame = new MenuItem("SAIR", 250);
        quitGame.setOnAction(() -> {
            System.exit(0);
        });
        menuBox.addItems(startButton, quitGame);
    }

    public class LongValue {
        public long value;

        public LongValue(long i) {
            this.value = i;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
