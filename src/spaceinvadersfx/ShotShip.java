/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvadersfx;

import javafx.scene.canvas.GraphicsContext;

/**
 * A entidade representa um tiro da nave do jogador
 * 
 * @author Caio E. Oliveira
 */
public class ShotShip extends Sprite {
    
    /**
	 * Cria um novo tiro da nave do jogador
	 * 
	 * @param ship Nave da qual o tiro sai
	 * @param gc renderizador do tiro da nave
	 */
    ShotShip(Ship ship, GraphicsContext gc){
        this.setImage("/images/rocket.png");
        this.setPosition(ship.getPositionX() + 10, ship.getPositionY() - 20);
        this.setVelocity(0, -350);
        this.render(gc);
    }
    
}
