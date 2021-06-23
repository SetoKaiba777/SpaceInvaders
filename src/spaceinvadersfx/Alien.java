/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvadersfx;

/**
 * Entidade que representa um dos aliens.
 * 
 * @author Caio E. Oliveira
 */
public class Alien extends Sprite {
    
    /**
	 * Cria uma nova entidade do tipo alien
	 * 
	 * @param x Posicão x inicial do alien
	 * @param y Posicão y inicial do alien
         * @param imagePath Caminho que leva ao sprite que deveria ser mostrado para esse alien
	 */
    Alien(int x, int y, String imagePath){
        this.setImage(imagePath);
        this.setPosition(x, y);
    }
    
}
