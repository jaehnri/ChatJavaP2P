import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Programa {
	
    public static void main (String[] args)
    {     
    	class Servidor extends Thread {
        	private ServerSocket pedido;
        	private Salas salas;
        	private boolean fim;
        	
        	public Servidor() {
        		try {
        			this.pedido = new ServerSocket(12345);
            		this.salas = new Salas();
            		this.fim = false;
            		
            		this.salas.novaSala("Dinalva");
            		this.salas.novaSala("Claudio");
            		this.salas.novaSala("Lapa");
            		this.salas.novaSala("Disney");
            		this.salas.novaSala("Direção");
            		this.salas.novaSala("Sala da Teresa Helena");
        		}
        		catch (Exception e) {}
        	}
        	
            public void run() {
            	while (!this.fim) {
            		try {
        				Socket conexao = this.pedido.accept();
        				new TratadorDeConexao(salas, conexao).start();
        			} catch (Exception e) {}
            	}
            }
            
            public void pare() {
            	this.fim = true;
            }
        }
    	
        Servidor server = new Servidor();
        server.start();
        
        System.out.println ("Servidor operante...");
        System.out.println ("De o comando 'shutdown' para derrubar o servidor");
        
        String input = null;
        while (input == null || !input.equals("SHUTDOWN")) {
            Scanner teclado = new Scanner(System.in);
            input = teclado.nextLine().trim().toUpperCase();
        }
        
        server.pare();
        
        System.out.println("Servidor encerrado");
    }
    
}