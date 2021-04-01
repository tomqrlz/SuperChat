import javax.net.ssl.SSLSocketFactory;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Client extends JFrame{

    private JPanel panel1;
    private JTextArea messageList;
    private JTextField messageInput;
    private JButton sendButton;
    private JScrollPane scroll;
    private JTextArea activeClientArea;

    private String nickname;
    private String address;
    private int port;

    private Socket clientSocket;
    private InetAddress ipAddress;
    private InetSocketAddress socketAddress;

    private Thread receive;
    private boolean running = false;
    private String messageReceived;
    private int ID = 0;

    private String time;


    public Client(String nickname, String address, int port) {
        this.nickname = nickname;
        this.address = address;
        this.port = port;

        boolean connected = openConnection(address, port);
        clock();

        if(connected) {
            CreateWindow();
            writeOut("Connecting: " + nickname + " to Chat with address: " + address + ":" + port);
            String connection = "/c/" + nickname;
            send(connection);
            receive();
        }
        else{
            System.err.println("Connection failed");
            writeOut("Connection failed!");
        }
    }

    private void CreateWindow(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentPane(panel1);
        setTitle("SuperChat - "  + nickname);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);

        messageInput.requestFocusInWindow();

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                submit(messageInput.getText());
            }
        });

        messageInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submit(messageInput.getText());
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String disconnect = "/d/" + ID;
                send(disconnect);
                closeConnection();
                running = false;
            }
        });
    }

    private boolean openConnection(String address, int port){
        try {
            ipAddress = InetAddress.getByName(address);
            socketAddress = new InetSocketAddress(ipAddress, port);
            clientSocket = ((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket();
            clientSocket.connect(socketAddress);
            running = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void writeOut(String message){
        messageList.append(message + "\n");
    }


    private void submit(String message){
        if(message.equals("")) return;
        send("/m/" + nickname + ": " + message);
        messageInput.setText("");
    }

    private void send(String message){
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            out.println(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive() {
        receive = new Thread("Receive") {
            public void run() {
                while (running) {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        messageReceived = in.readLine();
                        if(messageReceived.startsWith("/c/")) {
                            ID = Integer.parseInt(messageReceived.substring(3));
                            System.out.println("Your id = " + ID);
                        } else {
                            writeOut(time + " " + messageReceived);
                        }
                    } catch (IOException e) {
                        return;
                    } catch (NullPointerException npe){
                        return;
                    }
                }
            }
        };
        receive.start();
    }

    public void closeConnection(){
        synchronized (clientSocket){
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clock() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            }
        }, 0, 1000);
    }

}


