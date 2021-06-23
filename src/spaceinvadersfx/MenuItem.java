package spaceinvadersfx;

import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Entidade que representa os itens dentro do menu inicial do jogo.
 * 
 * @author Caio E. Oliveira
 */
public class MenuItem extends StackPane {
    /**texto que aparecera*/
    private Text text;
    /**espaco no qual o texto aparecera*/
    private Rectangle selection;
    /**sombra do texto*/
    private DropShadow shadow;
    /**cor do texto*/
    private Color color = Color.rgb(21, 255, 42);

    /**
	 * Cria uma nova entidade do tipo MenuItem
	 * 
	 * @param name nome do jogo
	 * @param width largura do texto do menu
	 */
    public MenuItem(String name, int width) {
        setAlignment(Pos.CENTER_LEFT);

        text = new Text(name);
        text.setTranslateX(5);
        text.setFont(Font.font("Monaco", FontWeight.EXTRA_BOLD, 20));
        text.setFill(color);
        text.setStroke(Color.BLACK);

        shadow = new DropShadow(5, Color.BLACK);
        text.setEffect(shadow);

        selection = new Rectangle(width - 45, 30);
        selection.setFill(color);
        selection.setStroke(Color.BLACK);
        selection.setVisible(false);

        GaussianBlur blur = new GaussianBlur(4);
        selection.setEffect(blur);

        getChildren().addAll(selection, text);

        setOnMouseEntered(e -> {
            onSelect();
        });

        setOnMouseExited(e -> {
            onDeselect();
        });

        setOnMousePressed(e -> {
            text.setFill(Color.YELLOW);
        });
    }
    /**Quando passar em cima*/
    private void onSelect() {
        text.setFill(Color.BLACK);
        selection.setVisible(true);

        shadow.setRadius(1);
    }
    /**quando sair de cima*/
    private void onDeselect() {
        text.setFill(color);
        selection.setVisible(false);

        shadow.setRadius(5);
    }
    /**quando a acao for tomada*/
    public void setOnAction(Runnable action) {
        setOnMouseClicked(e -> {
            FillTransition ft = new FillTransition(Duration.seconds(0.45), selection,
                    Color.YELLOW, color);
            ft.setOnFinished(e2 -> action.run());
            ft.play();
        });
    }
}
