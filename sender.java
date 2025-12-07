package SecureFileTransfer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;


public class sender extends JFrame {

    private JTextField ipField, portField;
    private JComboBox<String> encryptionMethod;
    private JTextArea statusArea;
    private File selectedFile;

    public sender() {
        setTitle("Secure File Transfer - Sender");
        setSize(550, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(3, 2));
        topPanel.add(new JLabel("Receiver IP:"));
        ipField = new JTextField("127.0.0.1");
        topPanel.add(ipField);

        topPanel.add(new JLabel("Port:"));
        portField = new JTextField("4433");
        topPanel.add(portField);

        topPanel.add(new JLabel("Encryption:"));
        encryptionMethod = new JComboBox<>(new String[]{"AES", "RSA"});
        topPanel.add(encryptionMethod);

        add(topPanel, BorderLayout.NORTH);

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton selectButton = new JButton("Select File");
        JButton sendButton = new JButton("Send File");
        buttonPanel.add(selectButton);
        buttonPanel.add(sendButton);
        add(buttonPanel, BorderLayout.SOUTH);

        selectButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                statusArea.append(" File selected: " + selectedFile.getAbsolutePath() + "\n");
            }
        });

        sendButton.addActionListener(e -> {
            if (selectedFile == null) {
                statusArea.append(" Select a file first.\n");
                return;
            }

            String ip = ipField.getText();
            int port = Integer.parseInt(portField.getText());
            String method = (String) encryptionMethod.getSelectedItem();

            File encryptedFile = null;

            if ("AES".equals(method)) {
                String password = JOptionPane.showInputDialog(this, "Enter AES password:");
                if (password == null || password.isEmpty()) {
                    statusArea.append(" AES password required.\n");
                    return;
                }
                encryptedFile = new File(selectedFile.getParent(), "encrypted_AES.enc");
                if (!encryptFileWithAES(selectedFile, encryptedFile, password)) return;
            } else {
                encryptedFile = new File(selectedFile.getParent(), "encrypted_RSA.enc");
                String pubKey = JOptionPane.showInputDialog(this, "Enter RSA public key file path:");
                if (pubKey == null || pubKey.isEmpty()) {
                    statusArea.append(" RSA public key path required.\n");
                    return;
                }
                if (!encryptFileWithRSA(selectedFile, encryptedFile, pubKey)) return;
            }

            sendEncryptedFile(ip, port, encryptedFile);
        });

        setVisible(true);
    }

    private boolean encryptFileWithAES(File inputFile, File outputFile, String password) {
        try {
            String cmd = String.format("openssl enc -aes-256-cbc -salt -in \"%s\" -out \"%s\" -pass pass:%s",
                    inputFile.getAbsolutePath(), outputFile.getAbsolutePath(), password);
            Process p = Runtime.getRuntime().exec(cmd);
            int code = p.waitFor();
            if (code == 0) {
                statusArea.append(" AES encryption successful: " + outputFile.getName() + "\n");
                return true;
            } else {
                statusArea.append(" AES encryption failed.\n");
                return false;
            }
        } catch (Exception e) {
            statusArea.append(" AES error: " + e.getMessage() + "\n");
            return false;
        }
    }

    private boolean encryptFileWithRSA(File inputFile, File outputFile, String publicKeyPath) {
        try {
            String cmd = String.format("openssl rsautl -encrypt -inkey \"%s\" -pubin -in \"%s\" -out \"%s\"",
                    publicKeyPath, inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
            Process p = Runtime.getRuntime().exec(cmd);
            int code = p.waitFor();
            if (code == 0) {
                statusArea.append(" RSA encryption successful: " + outputFile.getName() + "\n");
                return true;
            } else {
                statusArea.append(" RSA encryption failed.\n");
                return false;
            }
        } catch (Exception e) {
            statusArea.append(" RSA error: " + e.getMessage() + "\n");
            return false;
        }
    }

    private void sendEncryptedFile(String ip, int port, File fileToSend) {
        try (Socket socket = new Socket(ip, port);
             FileInputStream fis = new FileInputStream(fileToSend);
             OutputStream os = socket.getOutputStream()) {

            statusArea.append(" Sending to " + ip + ":" + port + "\n");
            byte[] buffer = new byte[4096];
            int count;
            while ((count = fis.read(buffer)) > 0) {
                os.write(buffer, 0, count);
            }
            os.flush();
            statusArea.append(" File sent successfully.\n");

        } catch (Exception e) {
            statusArea.append(" Send error: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(sender::new);
    }
}


