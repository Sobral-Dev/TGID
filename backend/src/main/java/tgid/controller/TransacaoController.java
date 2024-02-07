package tgid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tgid.dto.TransacaoDTO;
import tgid.entity.Cliente;
import tgid.entity.Transacao;
import tgid.exception.TransacaoInvalidaException;
import tgid.exception.TransacaoNaoEncontradaException;
import tgid.exception.TransacaoRemocaoException;
import tgid.service.TransacaoService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/deposito/{empresaId}/{clienteId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deposito(@PathVariable("empresaId") Long empresaId,
                         @PathVariable("clienteId") Long clienteId,
                         @RequestBody double valor) {
        try {
            return transacaoService.realizarDeposito(empresaId, clienteId, valor);
        } catch (TransacaoInvalidaException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Depósito não pôde ser realizado");
        }
    }

    @PostMapping("/saque/{empresaId}/{clienteId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> saque(@PathVariable("empresaId") Long empresaId,
                      @PathVariable("clienteId") Long clienteId,
                      @RequestBody double valor) {
        try {
            return transacaoService.realizarSaque(empresaId, clienteId, valor);
        } catch (TransacaoInvalidaException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saque não pôde ser realizado");
        }
    }

    @GetMapping("listar-transacoes")
    @ResponseStatus(HttpStatus.OK)
    public List<TransacaoDTO> listarTodasTransacoes() {

        try {

            return transacaoService.listarTodasTransacoes();

        } catch (TransacaoNaoEncontradaException e) {
            throw new TransacaoNaoEncontradaException("Não há nenhum registro de transações disponível");
        }
    }

    @DeleteMapping("delete-transacao/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteTransacao(@PathVariable("id") Long id) {

        try {
            transacaoService.deleteTransacao(id);
        } catch (TransacaoRemocaoException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível deletar a transação");
        }

        return ResponseEntity.ok("Transação deletada com sucesso!");
    }

}
