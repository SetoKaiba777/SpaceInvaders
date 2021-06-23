package spaceinvadersfx;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Lida com os sprites que serão mostrados na tela.
 * 
 * @author Caio E. Oliveira
 */
public class Sprite {
    /**Imagem a ser colocada no sprite*/
    private Image image;
    /**posição inicial x do sprite*/
    private double positionX;
    /**posição inicial y do sprite*/
    private double positionY;
    /**velocidade x de movimento do sprite*/
    private double velocityX;
    /**velocidade y de movimento do sprite*/
    private double velocityY;
    /**largura do sprite*/
    private double width;
    /**altura do sprite*/
    private double height;

    /**
	 * Cria um novo Sprite
	 * 
	 */
    public Sprite() {
        this.positionX = 0;
        this.positionY = 0;
        this.velocityX = 0;
        this.velocityY = 0;
    }

    /**
	 * Coloca nova imagem
	 * 
         * @param image a imagem a ser colocada
	 */
    public void setImage(Image image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }
    /**
	 * Coloca nova imagem por arquivo
	 * 
         * @param filename o nome do arquivo da imagem
	 */
    public void setImage(String filename) {
        Image i = new Image(getClass().getResource(filename).toExternalForm());
        Image toReturn = new Image(filename, i.getWidth()/3, i.getHeight()/3, true, false);
        setImage(toReturn);
    }
    
    /**
	 * Coloca nova imagem em gif por arquivo
	 * 
         * @param gif o gif a ser colocado
         * @param filename o nome do arquivo da imagem
	 */
    public void setImage(boolean gif, String filename) {
        ImageView i = new ImageView(new Image(getClass().getResource(filename).toExternalForm()));
        Image toReturn = new Image(filename);
        setImage(toReturn);
    }
    
    /**
	 * Recebe a imagem do sprite
	 * 
         * @return a imagem do sprite
	 */
    public Image getImage() {
        return image;
    }
    
    /**
	 * Recebe a posicao X do sprite
	 * 
         * @return a  posicao X do sprite
	 */
    public double getPositionX() {
        return positionX;
    }

    /**
	 * Recebe a posicao Y do sprite
	 * 
         * @return a posicao Y do sprite
	 */
    public double getPositionY() {
        return positionY;
    }
    
    /**
	 * Recebe a largura do sprite
	 * 
         * @return a largura do sprite
	 */
    public double getWidth() {
        return width;
    }
    
    /**
	 * Recebe a altura do sprite
	 * 
         * @return a altura do sprite
	 */
    public double getHeight() {
        return height;
    }
    
    /**
	 * Coloca a posicao do sprite
	 * 
         * @param x posicao no eixo x
         * @param y posicao no eixo y
	 */
    public void setPosition(double x, double y) {
        this.positionX = x;
        this.positionY = y;
    }
    
    /**
	 * Define a veocidade do sprite
	 * 
         * @param x velocidade no eixo x
         * @param y velocidade no eixo y
	 */
    public void setVelocity(double x, double y) {
        this.velocityX = x;
        this.velocityY = y;
    }
    
    /**
	 * aumenta a velocidade do sprite
	 * 
         * @param x velocidade no eixo x
         * @param y velocidade no eixo y
	 */
    public void addVelocity(double x, double y) {
        velocityX += x;
    }

    /**
	 * Atualiza a posicao do sprite 
	 * 
         * @param time tempo para atuaizar a posicao do sprite 
	 */
    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }
    
    /**
	 * Renderizador do sprite
	 * 
         * @param gc renderiza a imagem do sprite
	 */
    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY);
    }
    
    /**
	 * Recebe a hitbox dos sprites
	 * 
         * @return retangulo q representa a hitbox
	 */
    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }
    
    /**
	 * Interseccao entre sprites diferentes
	 * 
         * @param s Sprite que intercepta
         * @return a interseccao entre as hitboxes
	 */
    public boolean intersects(Sprite s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

}
