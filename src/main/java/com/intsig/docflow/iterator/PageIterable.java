package com.intsig.docflow.iterator;

import java.util.Iterator;

/**
 * 分页可迭代对象
 * <p>
 * 实现 Iterable 接口，支持 for-each 循环和 Stream API
 * </p>
 *
 * @param <T> 数据项类型
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class PageIterable<T> implements Iterable<T> {

    private final PageIterator.PageFetcher<T> pageFetcher;
    private final Integer maxPages;

    /**
     * 创建分页可迭代对象
     *
     * @param pageFetcher 分页数据获取器
     * @param maxPages    最大页数限制（null表示不限制）
     */
    public PageIterable(PageIterator.PageFetcher<T> pageFetcher, Integer maxPages) {
        this.pageFetcher = pageFetcher;
        this.maxPages = maxPages;
    }

    /**
     * 创建分页可迭代对象（不限制页数）
     *
     * @param pageFetcher 分页数据获取器
     */
    public PageIterable(PageIterator.PageFetcher<T> pageFetcher) {
        this(pageFetcher, null);
    }

    @Override
    public Iterator<T> iterator() {
        return new PageIterator<>(pageFetcher, maxPages);
    }
}
