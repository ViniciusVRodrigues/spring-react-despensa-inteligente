package com.viniciusvr.edespensa.application.controller;

import com.viniciusvr.edespensa.application.dto.*;
import com.viniciusvr.edespensa.application.usecase.*;
import com.viniciusvr.edespensa.domain.entity.CategoriaProduto;
import com.viniciusvr.edespensa.domain.entity.Produto;
import com.viniciusvr.edespensa.infrastructure.mapper.EntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller REST para operações com produtos da despensa.
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Produtos", description = "Operações de gerenciamento de produtos da despensa")
public class ProdutoController {

    private final CriarProdutoUseCase criarProdutoUseCase;
    private final BuscarProdutoUseCase buscarProdutoUseCase;
    private final AtualizarProdutoUseCase atualizarProdutoUseCase;
    private final DescartarProdutoUseCase descartarProdutoUseCase;
    private final ConsumirProdutoUseCase consumirProdutoUseCase;
    private final CriarProdutosEmLoteUseCase criarProdutosEmLoteUseCase;
    private final EntityMapper entityMapper;

    public ProdutoController(
            CriarProdutoUseCase criarProdutoUseCase,
            BuscarProdutoUseCase buscarProdutoUseCase,
            AtualizarProdutoUseCase atualizarProdutoUseCase,
            DescartarProdutoUseCase descartarProdutoUseCase,
            ConsumirProdutoUseCase consumirProdutoUseCase,
            CriarProdutosEmLoteUseCase criarProdutosEmLoteUseCase,
            EntityMapper entityMapper) {
        this.criarProdutoUseCase = criarProdutoUseCase;
        this.buscarProdutoUseCase = buscarProdutoUseCase;
        this.atualizarProdutoUseCase = atualizarProdutoUseCase;
        this.descartarProdutoUseCase = descartarProdutoUseCase;
        this.consumirProdutoUseCase = consumirProdutoUseCase;
        this.criarProdutosEmLoteUseCase = criarProdutosEmLoteUseCase;
        this.entityMapper = entityMapper;
    }

    @PostMapping
    @Operation(summary = "Criar produto", description = "Cria um novo produto na despensa (ação de compra)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProdutoResponse> criarProduto(@Valid @RequestBody ProdutoRequest request) {
        Produto produto = entityMapper.toEntity(request);
        Produto produtoCriado = criarProdutoUseCase.executar(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoResponse.fromEntity(produtoCriado));
    }

    @GetMapping
    @Operation(summary = "Listar produtos", description = "Lista todos os produtos ativos da despensa")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    public ResponseEntity<List<ProdutoResponse>> listarProdutos(
            @Parameter(description = "Filtrar por categoria")
            @RequestParam(required = false) CategoriaProduto categoria) {
        List<Produto> produtos;
        if (categoria != null) {
            produtos = buscarProdutoUseCase.listarPorCategoria(categoria);
        } else {
            produtos = buscarProdutoUseCase.listarTodosAtivos();
        }
        List<ProdutoResponse> response = produtos.stream()
                .map(ProdutoResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto", description = "Busca um produto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponse> buscarProduto(
            @Parameter(description = "ID do produto") @PathVariable String id) {
        Produto produto = buscarProdutoUseCase.buscarPorId(id);
        return ResponseEntity.ok(ProdutoResponse.fromEntity(produto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProdutoResponse> atualizarProduto(
            @Parameter(description = "ID do produto") @PathVariable String id,
            @Valid @RequestBody ProdutoRequest request) {
        Produto produtoAtualizado = entityMapper.toEntity(request);
        Produto produto = atualizarProdutoUseCase.executar(id, produtoAtualizado);
        return ResponseEntity.ok(ProdutoResponse.fromEntity(produto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Descartar produto", description = "Descarta um produto (soft delete - mantém histórico)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto descartado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> descartarProduto(
            @Parameter(description = "ID do produto") @PathVariable String id) {
        descartarProdutoUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/consume")
    @Operation(summary = "Consumir produtos", description = "Registra o consumo de produtos (batch)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consumo registrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "422", description = "Quantidade insuficiente")
    })
    public ResponseEntity<List<ProdutoResponse>> consumirProdutos(
            @Valid @RequestBody ConsumoLoteRequest request) {
        Map<String, Integer> consumos = request.consumos().stream()
                .collect(Collectors.toMap(
                        ConsumoRequest::produtoId,
                        ConsumoRequest::quantidade
                ));
        List<Produto> produtos = consumirProdutoUseCase.executarEmLote(consumos);
        List<ProdutoResponse> response = produtos.stream()
                .map(ProdutoResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    @Operation(summary = "Cadastrar produtos em lote", description = "Cria múltiplos produtos de uma vez")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produtos criados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<List<ProdutoResponse>> criarProdutosEmLote(
            @Valid @RequestBody ProdutoLoteRequest request) {
        List<Produto> produtos = request.produtos().stream()
                .map(entityMapper::toEntity)
                .toList();
        List<Produto> produtosCriados = criarProdutosEmLoteUseCase.executar(produtos);
        List<ProdutoResponse> response = produtosCriados.stream()
                .map(ProdutoResponse::fromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/expired")
    @Operation(summary = "Listar produtos expirados", description = "Lista todos os produtos com validade vencida")
    @ApiResponse(responseCode = "200", description = "Lista de produtos expirados retornada com sucesso")
    public ResponseEntity<List<ProdutoResponse>> listarProdutosExpirados() {
        List<Produto> produtos = buscarProdutoUseCase.listarExpirados();
        List<ProdutoResponse> response = produtos.stream()
                .map(ProdutoResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Listar produtos com estoque baixo", description = "Lista produtos com quantidade abaixo do limiar")
    @ApiResponse(responseCode = "200", description = "Lista de produtos com estoque baixo retornada com sucesso")
    public ResponseEntity<List<ProdutoResponse>> listarProdutosEstoqueBaixo(
            @Parameter(description = "Limiar de quantidade para considerar estoque baixo")
            @RequestParam(defaultValue = "5") Integer limiar) {
        List<Produto> produtos = buscarProdutoUseCase.listarComEstoqueBaixo(limiar);
        List<ProdutoResponse> response = produtos.stream()
                .map(ProdutoResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }
}
