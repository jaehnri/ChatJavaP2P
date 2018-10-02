import desordenada.ListaDesordenada;
import ordenada.ListaOrdenada;
import pacote.Pacote;

public class Sala implements Cloneable 
{
    protected String nome;
    protected int qtdUsuarios;
    protected ListaDesordenada<Usuario> usuarios;

    public Sala(String nome) {
    	this.nome = nome;
    	this.usuarios = new ListaDesordenada<Usuario>();
    	this.qtdUsuarios = 0;
    }

    public void entra(Usuario usr) throws Exception {
    	if (usr == null)
    		throw new Exception("Usuario inexistente");

        usr.setNomeSala(this.nome);
        this.usuarios.inserirNoFim(usr);
        this.qtdUsuarios++;
    }

    public void remover(Usuario usr) throws Exception {
        if (usr == null)
            throw new Exception("Usuario inexistente");

        this.usuarios.excluir(usr);
        this.qtdUsuarios--;
    }
    
    public boolean possuiUsuario(String nick) throws Exception {
    	if (nick == null || nick.trim() == "")
    		throw new Exception("Nick vazio");
    	
    	boolean achou = false;
    	
    	ListaDesordenada<Usuario> users = (ListaDesordenada<Usuario>)this.usuarios.clone();
    	Usuario user = null;
    	try {
    		for (;;) {
    			user = users.jogueForaPrimeiro();
    			if (user.getNick() == nick) {
    				achou = true;
    				break;
    			}
    		}
    	} 
    	catch (Exception e) {}
    	
    	return achou;
    }

    public ListaOrdenada<String> getNicks () {
    	ListaOrdenada<String> nicks = new ListaOrdenada<String>();
    	ListaDesordenada<Usuario> users = (ListaDesordenada<Usuario>)this.usuarios.clone();

    	try {
    		for (;;) {
    			Usuario user = users.jogueForaPrimeiro();
    			nicks.inserir(user.getNick());
    		}
    	} 
    	catch (Exception e) {}
    	
    	return nicks;
    }

    public Usuario getUsuario(String nick) throws Exception {
    	if (nick == null)
    		throw new Exception("Nick nulo");
    	
    	ListaDesordenada<Usuario> users = (ListaDesordenada<Usuario>)this.usuarios.clone();
    	
    	Usuario user = null;
    	
    	try {
    		for (;;) {
    			user = users.jogueForaPrimeiro();
    			
    			if (user.getNick() == nick)
    				break;
    		}
    	} 
    	catch (Exception e) {
    		throw new Exception("Usuario inexistente");
    	}
    	
    	return user;
    }

    public String getNome() {
        return this.nome;
    }

    public void enviarMensagemAberta(Pacote mensagem) throws Exception {
        if (mensagem == null || mensagem.getCmd().equals("MENSAGEM_ABERTA"))
            throw new Exception("Mensagem nula");

        ListaDesordenada<Usuario> users = (ListaDesordenada<Usuario>)this.usuarios.clone();
        try {
            for (;;) {
                Usuario user = users.jogueForaPrimeiro();
                user.envia(mensagem);
            }
        } 
        catch (Exception e) {}
    }

    public void enviarMensagemFechada(Pacote mensagem) throws Exception {
        if (mensagem == null || mensagem.getCmd().equals("MENSAGEM_FECHADA"))
            throw new Exception("Mensagem nula");

        ListaDesordenada<Usuario> users = (ListaDesordenada<Usuario>)this.usuarios.clone();
        try {
            for (;;) {
                Usuario user = users.jogueForaPrimeiro();
                if (user.getNick().toLowerCase().equals(mensagem.getComplementos()[0]))
                    user.envia(mensagem);
            }
        } 
        catch (Exception e) {}
    }

    // metodos para enviar mensagem para um usuario
    // especifico e metodos para enviar mensagens
    // abertas

    // metodos obrigatorios
    public boolean equals(Object obj) {
    	if (obj == null)
    		return false;

    	if (obj == this)
    		return true;

    	if (obj.getClass() != this.getClass())
    		return false;

    	Sala s = (Sala)obj;

    	if (this.nome != s.nome)
    		return false;

    	if (this.qtdUsuarios != s.qtdUsuarios)
    		return false;

    	if (!this.usuarios.equals(s.usuarios))
    		return false;

    	return true;
    }

    public int hashCode() {
		int ret = 7;

		ret = ret*7 + this.nome.hashCode();
    	ret = ret*7 + new Integer(this.qtdUsuarios).hashCode();
    	ret = ret*7 + this.usuarios.hashCode();

    	return ret;
    }

    public String toString() {
    	String ret = "[";
    	ret += this.nome + ",";
    	ret += this.qtdUsuarios + ",";
    	ret += this.usuarios;
    	ret += "]";

    	return ret;
    }

    public Sala(Sala m) throws Exception {
    	if (m == null)
    		throw new Exception("Modelo inexistente");

    	this.nome = m.nome;
    	this.qtdUsuarios = m.qtdUsuarios;
    	this.usuarios = (ListaDesordenada<Usuario>)m.usuarios.clone();
    }

    public Object clone() {
    	Sala ret = null;

    	try {
    		ret = new Sala(this);
    	}
    	catch (Exception e) {}

    	return ret;
    }
}