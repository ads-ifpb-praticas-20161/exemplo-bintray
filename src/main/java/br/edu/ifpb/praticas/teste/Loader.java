package br.edu.ifpb.praticas.teste;

/**
 * @author diogomoreira.
 */
public class Loader {

    public static void main(String[] args) throws NotaFiscalException {
        GerenciadorNotaFiscal gerenciadorNotaFiscal = new GerenciadorNotaFiscal();
        gerenciadorNotaFiscal.gera(new Pedido("Diogo", 26d, 5));
    }
}
