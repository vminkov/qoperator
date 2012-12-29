package net.virtualqueues.qoperator.view;

import javax.swing.JFrame;
import javax.swing.UIManager;



public class QOpGUI implements Runnable {
	private JFrame mainWindow;
	
	@Override
	public void run() {
		mainWindow = new JFrame();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//dispose_on_close to leave the rest stuff running

		mainWindow.setSize(911, 480);  // 10/16 of screen size 
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}

		mainWindow.setLocation((1366-911)/2, (768-480)/2);
		mainWindow.add(new MainPanel());

		mainWindow.setVisible(true);
	}

	/*@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
	            @Override
	            public void handle(ActionEvent event) {
	                System.out.println("Hello World!");
            }
        });
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        
        //StackPane root = new StackPane();
        grid.getChildren().add(btn);
        
        GridPane innerPane = new GridPane();
        grid.add(innerPane, 0, 0);
        //primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
*/
}
