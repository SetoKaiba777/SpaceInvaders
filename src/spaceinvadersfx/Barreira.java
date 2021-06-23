package spaceinvadersfx;

import javafx.scene.paint.Color;

/**
 * Entidade que representa a barreira presente no jogo.
 * 
 * @author Caio E. Oliveira
 */
public class Barreira {
    /** Matriz dos pontos onde as barreiras deverão ser colocadas*/
    private int[][] barreira;
    /**Cor da barreira*/
    private Color color;
    /**posição em x*/
    private double locationX;
    /**posição em y*/
    private double locationY;
    
    /**
	 * Cria uma nova entidade do tipo Barreira
	 * 
	 * @param
	 */
    public Barreira() {
        this.barreira = new int[][]{
                {0, 0, 1, 1, 1, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 0, 0, 0, 0, 1, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 1, 1}
        };
        this.color = Color.rgb(21, 255, 42);
    }
    /** 
         * Retorna a barreira
         * 
         * @return a barreira  
         */
    public int[][] getBarreira() {
        return barreira;
    }
    /** 
         * Retorna a cor da barreira
         * 
         * @return A cor da barreira  
         */
    public Color getColor() {
        return color;
    }
    /** 
         * Retorna a posição X da barreira
         * 
         * @return posição X da barreira  
         */
    public double getLocationX() {
        return locationX;
    }
    
    /** 
         * Retorna a pontuacao associada com o alien
         * 
         * @return A pontuacao do inimigo  
         */
    public double getLocationY() {
        return locationY;
    }
    
    /** 
         * Deleta os tijolos adjacentes ao levar tiro
         * 
         * @param row linha
         * @param col coluna
         */
    public void deleteBricksAround(int row, int col) {
        barreira[row][col] = 0;
        if (row < barreira.length - 1) {
            barreira[row + 1][col] = 0;
            if (col < barreira[0].length - 1) {
                barreira[row][col + 1] = 0;
            }
            if (col > 0) {
                barreira[row][col - 1] = 0;
            }
        }
    }
    
    /** 
         * Diz a posicao X da barreira
         * 
         * @param locationX posição X
         */
    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    /** 
         * Diz a posicao Y da barreira
         * 
         * @param locationy posição Y
         */
    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }
}
