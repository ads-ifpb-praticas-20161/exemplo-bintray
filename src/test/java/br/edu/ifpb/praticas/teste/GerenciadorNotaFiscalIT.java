package br.edu.ifpb.praticas.teste;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Created by diogomoreira on 17/07/16.
 */
public class GerenciadorNotaFiscalIT extends GenericDatabaseTestCase {

    private GerenciadorNotaFiscal gerenciadorNotaFiscal;

    @Before
    public void setUp() {
        this.gerenciadorNotaFiscal = new GerenciadorNotaFiscal();
    }

    @Test(expected = NotaFiscalException.class)
    public void naoDevePermitirDuasNotasParaOMesmoCliente() throws NotaFiscalException {
        Pedido pedido = new Pedido("Diogo Moreira", 26d, 7);
        gerenciadorNotaFiscal.gera(pedido);
    }

    @Test
    public void geraNotaFiscalComDesconto() throws Exception {
        Pedido pedido = new Pedido("Diogo M.", 26d, 3);
        NotaFiscal notaEmitida = gerenciadorNotaFiscal.gera(pedido);
        assertEquals(26d * 0.94, notaEmitida.getValor(), 1.0);
    }

    public String getDataSetFile() {
        return "src/test/resources/notas_fiscais.xml";
    }
}
