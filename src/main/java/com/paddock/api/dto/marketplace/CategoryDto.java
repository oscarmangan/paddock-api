package com.paddock.api.dto.marketplace;

import java.util.List;

public record CategoryDto(
   Long id,
   String discipline,
   String name,
   String label,
   String slug,
   Long parentId,
   int displayOrder,
   List<CategoryDto> children
) {}
