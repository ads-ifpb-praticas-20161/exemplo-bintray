package br.edu.ifpb.praticas.teste;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by diogomoreira on 31/07/16.
 */
public class GerenciadorNotaFiscalTest {

    private GerenciadorNotaFiscal gerador;

    @Mock
    private NFDao dao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        gerador = new GerenciadorNotaFiscal(dao);

        List<NotaFiscal> nfs = new LinkedList<NotaFiscal>();
        nfs.add(new NotaFiscal("Diego", 26d, new Date()));

        // when(dao.listar()).thenReturn(nfs);
//        doAnswer(new Answer<List>() {
//            public List answer(InvocationOnMock invocationOnMock) throws Throwable {
//                List<NotaFiscal> nfs = new LinkedList<NotaFiscal>();
//                nfs.add(new NotaFiscal("Diogo", 26d, Calendar.getInstance()));
//                return nfs;
//            }
//        }).when(dao).listar();


        doReturn(nfs).when(dao).listar();

    }

    @Test
    public void geraNotaFiscalComDesconto() throws Exception {
        Pedido pedido = new Pedido("Diogo", 26d, 3);
        NotaFiscal notaEmitida = gerador.gera(pedido);
        assertEquals(26d * 0.94, notaEmitida.getValor(), 1.0);
        verify(dao, atLeastOnce()).persiste(notaEmitida);
    }

    @Test(expected = NotaFiscalException.class)
    public void naoPermiteDuasNFsMesmoCliente() throws NotaFiscalException {
        Pedido pedido = new Pedido("Diego", 26d, 3);
        NotaFiscal notaEmitida = gerador.gera(pedido);
    }

}
