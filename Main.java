import java.awt.Color;

class Main {
    public static void main(String[] args) {
		GameWindow gw = new GameWindow("虫だ！",800,600);
        DrawCanvas dw = new DrawCanvas();
		dw.setBackground(new Color(0x7DA3A1));
        gw.addKeyListener(dw);
		gw.addComponent(dw);
		gw.setVisible(true);
		gw.startGameLoop();
	}
}

