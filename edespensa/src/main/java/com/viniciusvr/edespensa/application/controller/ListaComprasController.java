package com.viniciusvr.edespensa.application.controller;

import com.viniciusvr.edespensa.application.dto.ItemListaComprasRequest;
import com.viniciusvr.edespensa.application.dto.ItemListaComprasResponse;
import com.viniciusvr.edespensa.application.usecase.*;
import com.viniciusvr.edespensa.domain.entity.ItemListaCompras;
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

/**
 * Controller REST para operações com a lista de compras.
 */
@RestController
@RequestMapping("/api/shopping-list")
@Tag(name = "Lista de Compras", description = "Operações de gerenciamento da lista de compras")
public class ListaComprasController {

    private final AdicionarItemListaComprasUseCase adicionarItemUseCase;
    private final ListarItensListaComprasUseCase listarItensUseCase;
    private final RemoverItemListaComprasUseCase removerItemUseCase;
    private final GerarListaComprasAutomaticaUseCase gerarListaAutomaticaUseCase;
    private final EntityMapper entityMapper;

    public ListaComprasController(
            AdicionarItemListaComprasUseCase adicionarItemUseCase,
            ListarItensListaComprasUseCase listarItensUseCase,
            RemoverItemListaComprasUseCase removerItemUseCase,
            GerarListaComprasAutomaticaUseCase gerarListaAutomaticaUseCase,
            EntityMapper entityMapper) {
        this.adicionarItemUseCase = adicionarItemUseCase;
        this.listarItensUseCase = listarItensUseCase;
        this.removerItemUseCase = removerItemUseCase;
        this.gerarListaAutomaticaUseCase = gerarListaAutomaticaUseCase;
        this.entityMapper = entityMapper;
    }

    @GetMapping
    @Operation(summary = "Listar itens", description = "Lista todos os itens da lista de compras")
    @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso")
    public ResponseEntity<List<ItemListaComprasResponse>> listarItens(
            @Parameter(description = "Filtrar apenas itens não comprados")
            @RequestParam(defaultValue = "false") Boolean apenasNaoComprados) {
        List<ItemListaCompras> itens;
        if (apenasNaoComprados) {
            itens = listarItensUseCase.listarNaoComprados();
        } else {
            itens = listarItensUseCase.executar();
        }
        List<ItemListaComprasResponse> response = itens.stream()
                .map(ItemListaComprasResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Adicionar item", description = "Adiciona um novo item à lista de compras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ItemListaComprasResponse> adicionarItem(
            @Valid @RequestBody ItemListaComprasRequest request) {
        ItemListaCompras item = entityMapper.toEntity(request);
        ItemListaCompras itemAdicionado = adicionarItemUseCase.executar(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(ItemListaComprasResponse.fromEntity(itemAdicionado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover item", description = "Remove um item da lista de compras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public ResponseEntity<Void> removerItem(
            @Parameter(description = "ID do item") @PathVariable String id) {
        removerItemUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auto-generate")
    @Operation(summary = "Gerar lista automática", 
               description = "Gera automaticamente a lista de compras baseada em produtos com estoque baixo (< 20% do estoque médio)")
    @ApiResponse(responseCode = "201", description = "Lista gerada com sucesso")
    public ResponseEntity<List<ItemListaComprasResponse>> gerarListaAutomatica() {
        List<ItemListaCompras> itens = gerarListaAutomaticaUseCase.executar();
        List<ItemListaComprasResponse> response = itens.stream()
                .map(ItemListaComprasResponse::fromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
