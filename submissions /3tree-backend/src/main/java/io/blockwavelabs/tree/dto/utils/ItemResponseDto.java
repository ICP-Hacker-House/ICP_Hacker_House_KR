package io.blockwavelabs.tree.dto.utils;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemResponseDto<T> {
    private T items;

    @Builder
    public ItemResponseDto(T items) {
        this.items = items;
    }
}
