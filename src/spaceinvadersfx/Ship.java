/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvadersfx;
import javafx.scene.canvas.GraphicsContext;

/**
 * Entidade que representa a Nave do jogador.
 * 
 * @author Caio E. Oliveira
 */
public class Ship extends Sprite {
    
    /**
	 * Cria uma nova entidade do tipo nave
	 * 
	 * @param APP_WIDTH largura da nave
	 * @param SPACE espaco da entidade
	 * @param APP_HEIGHT altura da nave
	 * @param gc renderizador do sprite
	 */
    Ship(int APP_WIDTH, int SPACE, int APP_HEIGHT,GraphicsContext gc){
        this.setImage("/images/tank.png");
        this.setPosition(APP_WIDTH/2 - SPACE/2, APP_HEIGHT - SPACE);
        this.render(gc);
    }
    
}
