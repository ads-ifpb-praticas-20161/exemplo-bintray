package br.edu.ifpb.praticas.teste;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author diogomoreira
 */
public class GerenciadorNotaFiscal {

    private NFDao dao;

    public GerenciadorNotaFiscal() {
        dao = new NFDao();
    }

    public GerenciadorNotaFiscal(NFDao dao) {
        this.dao = dao;
    }

    public NotaFiscal gera(Pedido pedido) throws NotaFiscalException {
        NotaFiscal nf = new NotaFiscal(pedido.getCliente(), pedido.getValorTotal() * 0.94, new Date());
        try {
            List<NotaFiscal> notasExistentes = dao.listar();
            if(notasExistentes.contains(nf))
                throw new NotaFiscalException("Não foi possível registrar a nota fiscal.");
            dao.persiste(nf);
        } catch(Exception e) {
            throw new NotaFiscalException("Não foi possível registrar a nota fiscal. " + e.getMessage());
        }
        return nf;
    }

    public NotaFiscal recupera(String cliente) throws NotaFiscalException {
        try {
            return dao.consultar(cliente);
        } catch (SQLException e) {
            throw new NotaFiscalException("Nota fiscal inexistente");
        }
    }

}
