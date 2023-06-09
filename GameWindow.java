import javax.swing.JFrame;

class GameWindow extends JFrame implements Runnable{
	private Thread th = null;
    private final int FPS = 60;

	public GameWindow(String title, int width, int height) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(width,height);
		setLocationRelativeTo(null);
		setResizable(false);
	}
 
	//ゲームループの開始メソッド
	public synchronized void startGameLoop(){
		if ( th == null ) {
			th = new Thread(this);
			th.start();
		}
	}
    public void addComponent(DrawCanvas dc) {
        super.add(dc);
    }

	//ゲームループの終了メソッド
	public synchronized void stopGameLoop(){
		if ( th != null ) {
			th = null;
		}
	}
	public void run(){
		//ゲームループ（定期的に再描画を実行）
		while(th != null){
			try{
				Thread.sleep(1000/FPS);
				repaint();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}