import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame{
    private JPanel panel1;
    private JLabel lblNickname;
    private JTextField nicknameVal;
    private JLabel lblAddress;
    private JTextField addressVal;
    private JLabel lblPort;
    private JTextField portVal;
    private JButton submitButton;

    public Login(){
        System.setProperty("javax.net.ssl.trustStore", "src\\keystore\\examplestore1");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentPane(panel1);
        setTitle("SuperChat - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                    login(nicknameVal.getText(), addressVal.getText(), Integer.parseInt(portVal.getText()));
            }
        });


        nicknameVal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    addressVal.requestFocusInWindow();
                }
            }
        });
        addressVal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    portVal.requestFocusInWindow();
                }
            }
        });
        portVal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    login(nicknameVal.getText(), addressVal.getText(), Integer.parseInt(portVal.getText()));
                }
            }
        });
    }

    private void login(String nickname, String address, int port){
        dispose();
        new Client(nickname, address, port);
    }

    public static void main(String[] args) {
        Login frame = new Login();
        frame.setVisible(true);
    }

}
