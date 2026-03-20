package com.intsig.docflow.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 分页迭代器
 * <p>
 * 自动处理分页查询，透明地遍历所有数据
 * </p>
 *
 * @param <T> 数据项类型
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class PageIterator<T> implements Iterator<T> {

    private final PageFetcher<T> pageFetcher;
    private final Integer maxPages;

    private int currentPage;
    private List<T> currentPageData;
    private int currentIndex;
    private int totalCount;
    private boolean hasMorePages;

    /**
     * 创建分页迭代器
     *
     * @param pageFetcher 分页数据获取器
     * @param maxPages    最大页数限制（null表示不限制）
     */
    public PageIterator(PageFetcher<T> pageFetcher, Integer maxPages) {
        this.pageFetcher = pageFetcher;
        this.maxPages = maxPages;
        this.currentPage = 1;
        this.currentIndex = 0;
        this.hasMorePages = true;
        fetchNextPage();
    }

    /**
     * 创建分页迭代器（不限制页数）
     *
     * @param pageFetcher 分页数据获取器
     */
    public PageIterator(PageFetcher<T> pageFetcher) {
        this(pageFetcher, null);
    }

    @Override
    public boolean hasNext() {
        // 当前页还有数据
        if (currentPageData != null && currentIndex < currentPageData.size()) {
            return true;
        }

        // 当前页已遍历完，尝试获取下一页
        if (hasMorePages && (maxPages == null || currentPage < maxPages)) {
            currentPage++;
            fetchNextPage();
            return currentPageData != null && !currentPageData.isEmpty();
        }

        return false;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("没有更多数据");
        }

        T item = currentPageData.get(currentIndex);
        currentIndex++;
        return item;
    }

    /**
     * 获取下一页数据
     */
    private void fetchNextPage() {
        PageResult<T> result = pageFetcher.fetch(currentPage);

        this.currentPageData = result.getData();
        this.currentIndex = 0;
        this.totalCount = result.getTotal();

        // 判断是否还有下一页
        int pageSize = result.getPageSize();
        this.hasMorePages = (currentPage * pageSize < totalCount);
    }

    /**
     * 分页数据获取器接口
     *
     * @param <T> 数据项类型
     */
    @FunctionalInterface
    public interface PageFetcher<T> {
        /**
         * 获取指定页的数据
         *
         * @param page 页码（从1开始）
         * @return 分页结果
         */
        PageResult<T> fetch(int page);
    }

    /**
     * 分页结果
     *
     * @param <T> 数据项类型
     */
    public static class PageResult<T> {
        private final List<T> data;
        private final int total;
        private final int pageSize;

        public PageResult(List<T> data, int total, int pageSize) {
            this.data = data;
            this.total = total;
            this.pageSize = pageSize;
        }

        public List<T> getData() {
            return data;
        }

        public int getTotal() {
            return total;
        }

        public int getPageSize() {
            return pageSize;
        }
    }
}
