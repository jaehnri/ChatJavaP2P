package ordenada;

import java.lang.reflect.*;

public class ListaOrdenada <X extends Comparable<X>>
{
	protected class No {
		protected X  info;
		protected No prox;
		protected No ant;

		public X getInfo() {
			return this.info;
		}

		public No getAnt() {
			return this.ant;
		}

		public No getProx() {
			return this.prox;
		}

		public void setInfo(X x) {
			this.info = x;
		}

		public void setAnt(No n) {
			this.ant = n;

			if (n != null)
				n.prox = this;
		}

		public void setProx(No n) {
			this.prox = n;

			if (n != null)
				n.ant = this;
		}

		public No (X x, No n) {
			this.info = x;
			this.prox = n;

			if (this.prox != null)
				n.ant = this;
		}

		public No (X x) {
			this(x,null);
		}

		public int hashCode() {
			int ret = 44;

			ret = ret*7 + info.hashCode();

			if (prox != null)
				ret = ret*7 + prox.hashCode();

			return ret;
		}

		public boolean equals (Object obj) {
			if (obj == this)
				return true;

			if (obj == null)
				return false;

			if (!this.getClass().equals(obj.getClass()))
				return false;

			No no = (No)obj;

			if (!(this.info).equals(no.info))
				return false;

			if (this.prox == null)
				if (no.prox == null)
					return true;
				else
					return false;

			if (no.prox == null)
				return false;

			if (!(this.prox).equals(no.prox))
				return false;

			return true;
		}
	}

	protected No prim;
	protected No ultimo;
	protected int qtd;

	protected X meuCloneDeX (X x) {
		X ret = null;

		try {
			Class<?> classe = x.getClass();
			Class<?>[] tipoDoParametroFormal = null;
			Method metodo = classe.getMethod ("clone", tipoDoParametroFormal);
			Object[] parametroReal = null;

			ret = ((X)metodo.invoke (x, parametroReal));
		}
		catch (NoSuchMethodException erro){}
		catch (InvocationTargetException erro){}
		catch (IllegalAccessException erro){}

		return ret;
    }

	public void inserir(X x) throws Exception {
		if (x == null)
			throw new Exception("Informacao ausente");

		X info;
		if (x instanceof Cloneable)
			info = meuCloneDeX(x);
		else
			info = x;

		if (prim == null)
			prim = new No(info);
		else
		{
			No atual = this.prim;
			while(atual.getInfo().compareTo(info) < 0 && atual != null)
				atual = atual.getProx();

			if (atual != this.prim)
			{
				atual = atual.getAnt();
				atual.setProx(new No (info, atual.getProx()));
			}
			else
				this.prim = new No(info, this.prim);
		}
		
		this.qtd++;
	}
	
	public String[] toStringArray() {
		String[] ret = new String[this.qtd];
		
		No aux = this.prim;
		for (int i = 0; i < ret.length; i++) {
			ret[i] = aux.getInfo().toString();
			aux = aux.getProx();
		}
		
		return ret;
	}

	public String toString() {
		String ret = "{";
		No atual = this.prim;

		while (atual != null) {
			ret += atual.getInfo();

			if (atual.getProx() != null)
				ret += ",";

			atual = atual.getProx();
		}

		ret += "}";

		return ret;
	}

	public int hashCode() {
		int ret = 44;

		ret = ret*7 + prim.hashCode();

		return ret;
	}

	public boolean equals (Object obj) {
		if (obj == this)
			return true;

		if (obj == null)
			return false;

		if (!this.getClass().equals(obj.getClass()))
			return false;

		ListaOrdenada<X> lis = (ListaOrdenada<X>)obj;

		if (this.qtd != lis.qtd)
			return false;
		
		if (!((this.prim).equals(lis.prim)))
			return false;

		return true;
	}

	public Object clone () {
		ListaOrdenada<X> ret = null;
		
		try {
			ret = new ListaOrdenada<X>(this);
		}
		catch(Exception erro) {}

		return ret;
	}

	public ListaOrdenada(ListaOrdenada<X> modelo) throws Exception {
		if (modelo == null)
			throw new Exception("Modelo ausente");

		this.qtd = modelo.qtd;
		if (modelo.prim != null) {
			this.prim = new No(modelo.prim.getInfo());

			No atual = modelo.prim.getProx();
			No aux   = this.prim;

			while (atual != null) {
				aux.setProx(new No(atual.getInfo()));

				atual = atual.getProx();
				aux   = aux.getProx();
			}
		}
	}
	
	public ListaOrdenada<X> menos(ListaOrdenada<X> modelo)
	{
		ListaOrdenada<X> ret = (ListaOrdenada<X>)this.clone();
		
		No atualA = ret.prim;
		No atualB = modelo.prim;

		while(atualA != null && atualB != null)
		{
			int comp = atualA.getInfo().compareTo(atualB.getInfo());
			
			if(comp == 0)
			{
				if (atualA != ret.prim) {
					atualA.getAnt().setProx(atualA.getProx());
					ret.qtd--;
				}
				else
				{
					atualA.getProx().setAnt(null);
					prim = atualA.getProx();
					ret.qtd--;
				}
					
				atualA = atualA.getProx();
				atualB = atualB.getProx();
			}
			else
				if(comp < 0)
					atualA = atualA.getProx();
				else 
					atualB = atualB.getProx();
		}
		
		return ret;
	}

	public ListaOrdenada() 
	{
		this.prim = null;
		this.ultimo = null;
		this.qtd = 0;
	}

	public void jogueForaUltimo() throws Exception {
		if(this.prim == null)
			throw new Exception("Nao ha nada para jogar fora");

		this.prim = this.prim.getProx();

		if (this.prim == null)
			this.ultimo = null;
		
		this.qtd--;
	}

	public void jogueForaPrimeiro() throws Exception {
		if(this.prim == null)
			throw new Exception("Nao ha nada para jogar fora");

		if (this.prim.getProx() != null)
		{
			this.ultimo = this.ultimo.getAnt();
			this.ultimo.setProx(null);
		}
		else
		{
			this.prim = null;
			this.ultimo = null;
		}
		
		this.qtd--;
	}
}