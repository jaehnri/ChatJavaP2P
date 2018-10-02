package cliente;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import pacote.Pacote;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FrmChat extends JFrame {
	
	private JPanel contentPane;
	private JTextField txtMensagem;
	private JButton btnEnviar;
	private JComboBox<String> cbxDest;
	private JTextPane txtPConversa;
	private JLabel lblSala;
	private JLabel lblNick;
	private JLabel lblDest;
	private Loader load;
	
	private Pacote pac;
	private ClientHandler clHandler;

	private class Loader extends Thread {
		public void run() {
			for (;;) {
				String[] atualizacoes = clHandler.getAtualizacoes();
				if (atualizacoes != null && atualizacoes.length > 0)
				{
					if (atualizacoes[0].equals("NICKS_NA_SALA")) {
						cbxDest.removeAllItems();
						cbxDest.addItem("TODOS");
						for (int i = 1; i < atualizacoes.length; i++)
							cbxDest.addItem(atualizacoes[i]);
					} else {
						for (int i = 0; i < atualizacoes.length; i++)
							txtPConversa.setText(txtPConversa.getText() + "\n" + atualizacoes[i]);
					}
				}
				pac = clHandler.receber();
				String texto = pac.getCmd();
				txtPConversa.setText(texto);
			}
		}
	}
	
	/**
	 * Create the frame.
	 */
	public FrmChat(ClientHandler ch) throws Exception {
		if (ch == null)
			throw new Exception("Cliente nulo");
		
		this.clHandler = ch;
		this.load = new Loader();
		initialize();
		this.load.start();
	}
	
	private void initialize() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				clHandler.desconectar();
			}
		});
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 554, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(445, 334, 71, 33);
		contentPane.add(btnEnviar);
		
		txtMensagem = new JTextField();
		txtMensagem.setBounds(154, 335, 281, 32);
		contentPane.add(txtMensagem);
		txtMensagem.setColumns(10);
		
		cbxDest = new JComboBox<String>();
		cbxDest.setBounds(32, 334, 112, 33);
		contentPane.add(cbxDest);
		cbxDest.addItem("TODOS");
		
		txtPConversa = new JTextPane();
		txtPConversa.setEditable(false);
		txtPConversa.setBounds(32, 46, 484, 265);
		contentPane.add(txtPConversa);
		txtPConversa.setText("eae");

		
		lblSala = new JLabel("Sala: " + clHandler.getNomeSala());
		lblSala.setBounds(32, 12, 169, 33);
		contentPane.add(lblSala);
		
		lblNick = new JLabel("Nick: " + clHandler.getNick());
		lblNick.setBounds(341, 12, 175, 33);
		contentPane.add(lblNick);
		
		lblDest = new JLabel("Destinat\u00E1rio:");
		lblDest.setBounds(32, 312, 112, 23);
		contentPane.add(lblDest);
		
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String mensagem = txtMensagem.getText().trim();
				String destinatario = null;
				if (cbxDest.getSelectedIndex() == -1)
					destinatario = "TODOS";
				else 
					destinatario = cbxDest.getSelectedItem().toString().toLowerCase();
				
				if (mensagem != "") {
					try {
						clHandler.enviarMensagem(mensagem, destinatario);
					} catch (Exception e) {}
				}
			}
		});
	}
}
