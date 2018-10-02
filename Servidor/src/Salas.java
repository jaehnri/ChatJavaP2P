import desordenada.ListaDesordenada;

public class Salas implements Cloneable 
{
    protected int qtd;
    protected ListaDesordenada<Sala> salas;

    public Salas() {
    	this.salas = new ListaDesordenada<Sala>();
    	this.qtd = 0;
    }

    public void novaSala(String nome) throws Exception {
        if (nome == null || nome.trim() == "")
            throw new Exception("Nome vazio");

		Sala nova = new Sala(nome);
		try {
			salas.inserirNoFim(nova);
	    	this.qtd++;
		}
    	catch (Exception e) {}
    }

    public Sala getSala(String nome) throws Exception {
        if (nome == null || nome.trim() == "")
            throw new Exception("Nome vazio");

        ListaDesordenada<Sala> listaSalas = (ListaDesordenada<Sala>)this.salas.clone();
        Sala sala = null;
    	try {
            for (;;) {
                Sala aux = listaSalas.jogueForaPrimeiro();
                if (aux.getNome() == nome) {
                    sala = aux;
                    break;
                }
            }
        } 
        catch (Exception e) {}

        if (sala == null)
            throw new Exception("Sala inexistente");

        return sala;
    }

    public String[] getNomesSalas() {
    	String[] nomes = new String[this.qtd];
        ListaDesordenada<Sala> listaSalas = (ListaDesordenada<Sala>)this.salas.clone();

        int i = 0;
        try {
            for (;;) {
                Sala sala = listaSalas.jogueForaPrimeiro();
                nomes[i] = sala.getNome();
                i++;
            }
        } 
        catch (Exception e) {}

        return nomes;
    }
    
    public boolean possuiUsuario(String nick) throws Exception {
    	if (nick == null || nick.trim() == "")
    		throw new Exception("Nick vazio");
    	
    	boolean achou = false;
    	
    	ListaDesordenada<Sala> listaSalas = (ListaDesordenada<Sala>)this.salas.clone();
        Sala sala = null;
    	try {
            for (;;) {
                Sala aux = listaSalas.jogueForaPrimeiro();
                if (aux.possuiUsuario(nick)) {
                	achou = true;
                	break;
                }
            }
        } 
        catch (Exception e) {}
    	
    	return achou;
    }

    // metodos obrigatorios, claro
    public boolean equals(Object obj) {
    	if (obj == null)
    		return false;

    	if (obj == this)
    		return true;

    	if (obj.getClass() != this.getClass())
    		return false;

    	Salas s = (Salas)obj;

    	if (this.qtd != s.qtd)
    		return false;

    	if (!this.salas.equals(s.salas))
    		return false;

    	return true;
    }

    public int hashCode() {
    	int ret = 7;

    	ret = ret*7 + new Integer(this.qtd).hashCode();
    	ret = ret*7 + this.salas.hashCode();

    	return ret;
    }

    public String toString() {
    	String ret = "[";
    	ret += this.qtd + ",";
    	ret += this.salas;
    	ret += "]";

    	return ret;
    }

    public Salas(Salas m) throws Exception {
    	if (m == null)
    		throw new Exception("Modelo inexistente");

    	this.qtd = m.qtd;
    	this.salas = (ListaDesordenada<Sala>)m.salas.clone();
    }

    public Object clone() {
    	Salas ret = null;

    	try {
    		ret = new Salas(this);
    	}
    	catch (Exception e) {}

    	return ret;
    }
}