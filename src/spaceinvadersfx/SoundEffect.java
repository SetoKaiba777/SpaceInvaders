package spaceinvadersfx;

import javafx.scene.media.AudioClip;

/**
 * A entidade responsavel por manejar efeitos sonoros
 * 
 * @author Caio E. Oliveira
 */
public class SoundEffect {
    /**Arquivo de som*/
    private AudioClip soundEffect;

    /**
	 * Cria um novo efeito sonoro
	 * 
	 * @param filePath caminho para o arquivo de som que será usado como efeito sonoro
	 */
    public SoundEffect(String filePath) {
        soundEffect = new AudioClip(getClass().getResource(filePath).toExternalForm());
    }
    
    /**irá tocar o efeito sonoro*/
    public void playClip() {
        soundEffect.play();
    }
}
