import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pacote.Pacote;

public class Usuario implements Cloneable 
{
    protected String nomeSala;
    protected String nick;
    protected Socket conexao;
    protected ObjectOutputStream transmissor;
    protected ObjectInputStream receptor;

    // construtor de usuario instancia BufferedReader e PrintWriter, 
    // interage atraves deles com o usuario para enviar a lista de
    // salas e obter a sala onde o usuario quer entrar, bem como o
    // seu nick, inicializando this.sala e this.nick
    public Usuario (Salas salas, Socket conexao) throws Exception {
    	if (conexao == null || salas == null)
    		throw new Exception("Parametro nulo");

    	this.conexao = conexao;
    	this.transmissor = new ObjectOutputStream(this.conexao.getOutputStream());
    	this.receptor = new ObjectInputStream(this.conexao.getInputStream());
    	
    	String[] nomesSalas = salas.getNomesSalas();

        Pacote lista = new Pacote("LISTA_SALAS", nomesSalas);
        this.envia(lista);

        boolean foiEscolhido = false;
        while (!foiEscolhido) {
            Pacote escolha = null;
            while (escolha == null)
                escolha = this.recebe();

            if (escolha.getCmd().equals("ESCOLHER_SALA")) {
            	String [] comp = escolha.getComplementos();
            	
            	if (!salas.possuiUsuario(comp[0])) {
            		for (int i = 0; i < nomesSalas.length; i++) {
                        if (nomesSalas[i].equals(comp[1])) {
                            this.envia(new Pacote("CONFIRMACAO_ENTROU", new String[0]));
                            this.nick = comp[0];
                            this.nomeSala = comp[1];
                            foiEscolhido = true;
                            break;
                        } else
                            if (i >= nomeSala.length()-1)
                                this.envia(new Pacote("SALA_INVALIDA", new String[0]));
                    }
            	} else
                    this.envia(new Pacote("NOME_JA_USADO", new String[0]));

            }
        }
    }

    // getters para nomeSala e nick
    public String getNomeSala() {
    	return this.nomeSala;
    }

    public void setNomeSala(String novoNome) throws Exception {
    	if (novoNome == null || novoNome.trim() == "")
    		throw new Exception("Nome nulo");

    	this.nomeSala = novoNome;
    }

    public String getNick() {
    	return this.nick;
    }

    public void setNick(String novoNick) throws Exception {
    	if (novoNick == null || novoNick.trim() == "")
    		throw new Exception("Nick nulo");

        String[] comp = new String[2];
        comp[0] = this.nick;
        comp[1] = novoNick;

        Pacote dados = new Pacote("MUDANCA_NICK", comp);
        this.envia(dados);

        this.nick = novoNick;
    }
    
    // metodos para desconectar
    public void desconectar() throws Exception {
        if (!this.conexao.isConnected())
            throw new Exception("Usuario ja desconectado");

    	this.transmissor.close();
    	this.receptor.close();
    	this.conexao.close();
    }

    // recebe do usuario, usando this.receptor
    public Pacote recebe() throws Exception {
        if (!this.conexao.isConnected())
            throw new Exception("Usuario desconectado");

        Pacote p = (Pacote)this.receptor.readObject();
        return p;
    }

    // envia para o usuario, usando this.transmissor
    public void envia(Pacote p) throws Exception {
        if (p == null)
            throw new Exception("Pacote nulo");

        if (!this.conexao.isConnected())
            throw new Exception("Usuario desconectado");

    	this.transmissor.writeObject(p);
        this.transmissor.flush();
    }

    // metodos obrigatorios
    public boolean equals(Object obj) {
    	if (obj == null)
    		return false;

    	if (obj == this)
    		return true;

    	if (obj.getClass() != this.getClass())
    		return false;

    	Usuario usr = (Usuario)obj;

    	if (this.nomeSala != usr.nomeSala)
    		return false;

    	if (this.nick != usr.nick)
    		return false;

    	if (!this.conexao.equals(usr.conexao))
    		return false;

    	if (!this.receptor.equals(usr.receptor))
    		return false;

    	if (!this.transmissor.equals(usr.transmissor))
    		return false;

    	return true;
    }

    public int hashCode() {
		int ret = 7;

		ret = ret*7 + this.nomeSala.hashCode();
		ret = ret*7 + this.nick.hashCode();
		ret = ret*7 + this.conexao.hashCode();
		ret = ret*7 + this.receptor.hashCode();
		ret = ret*7 + this.transmissor.hashCode();

    	return ret;
    }

    public String toString() {
    	String ret = "{";
    	ret += this.nick + ",";
    	ret += this.nomeSala + ",";
		ret += this.conexao + ",";
		ret += this.receptor + ",";
		ret += this.transmissor;
    	ret += "}";

    	return ret;
    }

    public Usuario(Usuario m) throws Exception {
    	if (m == null)
    		throw new Exception("Modelo inexistente");

    	this.nomeSala = m.nomeSala;
    	this.nick = m.nick;
    	this.conexao = m.conexao;
    	this.transmissor = m.transmissor;
    	this.receptor = m.receptor;
    }

    public Object clone() {
    	Usuario ret = null;

    	try {
    		ret = new Usuario(this);
    	}
    	catch (Exception e) {}

    	return ret;
    }
}