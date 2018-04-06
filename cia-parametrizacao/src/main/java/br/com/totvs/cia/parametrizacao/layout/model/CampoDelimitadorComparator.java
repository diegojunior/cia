
package br.com.totvs.cia.parametrizacao.layout.model;

import java.util.Comparator;

public class CampoDelimitadorComparator implements Comparator<Campo>{

	@Override
	public int compare(Campo c1, Campo c2) {
		return c1.getOrdem().compareTo(c2.getOrdem());
	}
}
