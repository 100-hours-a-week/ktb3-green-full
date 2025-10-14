package com.example.spring_community.Post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PagePostDto<T> {
    private final List<T> pageList;
    private final int page;
    private final int size;
    private final int totalPages;
    private final boolean hasPrev;
    private final boolean hasNext;

    @Builder
    public PagePostDto (List<T> pageList, int page, int size, int totalPages, boolean hasPrev, boolean hasNext) {
        this.pageList = pageList;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.hasPrev = hasPrev;
        this.hasNext = hasNext;
    }

    public static <T> PagePostDto<T> of (List<T> pageList, int page, int size, int totalPosts) {
        int totalPages = (int) Math.ceil((double) totalPosts / size);
        return PagePostDto.<T>builder()
                .pageList(pageList).page(page).size(size).totalPages(totalPages)
                .hasPrev(page > 0).hasNext(page < totalPages)
                .build();
    }

}
