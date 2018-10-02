package cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class FrmSettings extends JFrame {

	/**
	 * 
	 */
	private FrmChat frmChat;
	private JPanel contentPane;
	private JTextField txtIP;
	private JTextField txtNick;

	private ClientHandler clHandler;

	/**
	 * Create the frame.
	 */
	public FrmSettings() {
		initialize();
	}
	
	public void initialize() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 465, 293);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel plConectar = new JPanel();
		plConectar.setBounds(10, 10, 437, 240);
		contentPane.add(plConectar);
		plConectar.setLayout(null);
		plConectar.setVisible(true);
		
		JPanel plEntrarSala = new JPanel();
		plEntrarSala.setBounds(10, 10, 437, 240);
		contentPane.add(plEntrarSala);
		plEntrarSala.setLayout(null);
		plEntrarSala.setVisible(false);
		
		txtIP = new JTextField();
		txtIP.setBounds(72, 83, 279, 31);
		plConectar.add(txtIP);
		txtIP.setColumns(10);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.setBounds(72, 125, 279, 31);
		plConectar.add(btnConectar);
		
		JLabel lblIP = new JLabel("Digite o IP do servidor:");
		lblIP.setBounds(72, 47, 151, 31);
		plConectar.add(lblIP);
		
		txtNick = new JTextField();
		txtNick.setBounds(151, 44, 199, 29);
		plEntrarSala.add(txtNick);
		txtNick.setColumns(10);
		
		JButton btnEntrar = new JButton("Entrar na Sala");
		btnEntrar.setBounds(54, 154, 296, 41);
		plEntrarSala.add(btnEntrar);
		
		JLabel lblNick = new JLabel("Escolha seu nick:");
		lblNick.setBounds(54, 48, 87, 20);
		plEntrarSala.add(lblNick);
		
		JComboBox<String> cbxSalas = new JComboBox<String>();
		cbxSalas.setBounds(151, 97, 199, 29);
		plEntrarSala.add(cbxSalas);
		
		JLabel lblSalas = new JLabel("Escolha a sala:");
		lblSalas.setBounds(65, 101, 76, 20);
		plEntrarSala.add(lblSalas);
		
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!txtIP.getText().trim().equals("") && txtIP.getText() != null) {
					try {
						Socket conn = new Socket(txtIP.getText().trim(), 12345);
						
						clHandler = new ClientHandler(conn);
						String [] salas = clHandler.getSalas();
						for (int i = 0; i < salas.length; i++)
						{
							cbxSalas.addItem(salas[i]);
						}
						
						plConectar.setVisible(false);
						plEntrarSala.setVisible(true);
					}  catch (Exception e) {
						txtIP.setText("");
						txtIP.grabFocus();
						JOptionPane.showMessageDialog(null,"Servidor indisponível!");
					}
				} else
					txtIP.grabFocus();
			}
		});
		
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nickEscolhido = txtNick.getText().trim();
				String salaEscolhida = cbxSalas.getSelectedItem().toString().trim();
				
				if (!nickEscolhido.equals("") && !salaEscolhida.equals("")) {
					try {
						clHandler.entrar(nickEscolhido, salaEscolhida);
						
						frmChat = new FrmChat(clHandler);
						frmChat.setVisible(true);
						setVisible(false);
					} catch (Exception ex) {
						txtNick.setText("");
						txtNick.grabFocus();
						JOptionPane.showMessageDialog(null,ex.getMessage());
					}
				} else
					txtNick.grabFocus();
			}
		});
	}
}
