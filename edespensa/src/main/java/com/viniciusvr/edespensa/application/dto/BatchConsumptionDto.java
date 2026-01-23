package com.viniciusvr.edespensa.application.dto;

import java.util.List;

/**
 * DTO for batch consumption request.
 */
public record BatchConsumptionDto(
    List<ConsumptionItemDto> items
) {}
