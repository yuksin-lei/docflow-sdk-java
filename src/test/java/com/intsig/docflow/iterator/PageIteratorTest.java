package com.intsig.docflow.iterator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 分页迭代器测试
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
class PageIteratorTest {

    @Test
    void testBasicIteration() {
        // 模拟3页数据，每页2条
        PageIterator.PageFetcher<String> fetcher = page -> {
            List<String> data = new ArrayList<>();
            if (page == 1) {
                data.add("item1");
                data.add("item2");
            } else if (page == 2) {
                data.add("item3");
                data.add("item4");
            } else if (page == 3) {
                data.add("item5");
                data.add("item6");
            }
            return new PageIterator.PageResult<>(data, 6, 2);
        };

        PageIterable<String> iterable = new PageIterable<>(fetcher);
        List<String> result = new ArrayList<>();

        for (String item : iterable) {
            result.add(item);
        }

        assertEquals(6, result.size());
        assertEquals(Arrays.asList("item1", "item2", "item3", "item4", "item5", "item6"), result);
    }

    @Test
    void testMaxPagesLimit() {
        // 模拟10页数据，每页1条
        PageIterator.PageFetcher<String> fetcher = page -> {
            List<String> data = new ArrayList<>();
            if (page <= 10) {
                data.add("item" + page);
            }
            return new PageIterator.PageResult<>(data, 10, 1);
        };

        // 限制只获取前3页
        PageIterable<String> iterable = new PageIterable<>(fetcher, 3);
        List<String> result = new ArrayList<>();

        for (String item : iterable) {
            result.add(item);
        }

        assertEquals(3, result.size());
        assertEquals(Arrays.asList("item1", "item2", "item3"), result);
    }

    @Test
    void testEmptyData() {
        PageIterator.PageFetcher<String> fetcher = page ->
                new PageIterator.PageResult<>(new ArrayList<>(), 0, 10);

        PageIterable<String> iterable = new PageIterable<>(fetcher);
        List<String> result = new ArrayList<>();

        for (String item : iterable) {
            result.add(item);
        }

        assertEquals(0, result.size());
    }

    @Test
    void testStreamAPI() {
        // 模拟5页数据，每页2条
        PageIterator.PageFetcher<Integer> fetcher = page -> {
            List<Integer> data = new ArrayList<>();
            if (page <= 5) {
                data.add((page - 1) * 2 + 1);
                data.add((page - 1) * 2 + 2);
            }
            return new PageIterator.PageResult<>(data, 10, 2);
        };

        PageIterable<Integer> iterable = new PageIterable<>(fetcher);

        // 使用 Stream API 进行过滤和映射
        List<Integer> evenNumbers = StreamSupport.stream(iterable.spliterator(), false)
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList(2, 4, 6, 8, 10), evenNumbers);
    }

    @Test
    void testManualIteration() {
        PageIterator.PageFetcher<String> fetcher = page -> {
            List<String> data = new ArrayList<>();
            if (page == 1) {
                data.add("first");
                data.add("second");
            } else if (page == 2) {
                data.add("third");
            }
            return new PageIterator.PageResult<>(data, 3, 2);
        };

        PageIterator<String> iterator = new PageIterator<>(fetcher);

        assertTrue(iterator.hasNext());
        assertEquals("first", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("second", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("third", iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Test
    void testPartialLastPage() {
        // 模拟最后一页只有部分数据
        PageIterator.PageFetcher<String> fetcher = page -> {
            List<String> data = new ArrayList<>();
            if (page == 1) {
                data.add("item1");
                data.add("item2");
                data.add("item3");
            } else if (page == 2) {
                data.add("item4");  // 最后一页只有1条
            }
            return new PageIterator.PageResult<>(data, 4, 3);
        };

        PageIterable<String> iterable = new PageIterable<>(fetcher);
        List<String> result = new ArrayList<>();

        for (String item : iterable) {
            result.add(item);
        }

        assertEquals(4, result.size());
    }

    @Test
    void testEarlyBreak() {
        PageIterator.PageFetcher<String> fetcher = page -> {
            List<String> data = new ArrayList<>();
            if (page <= 10) {
                data.add("item" + page);
            }
            return new PageIterator.PageResult<>(data, 10, 1);
        };

        PageIterable<String> iterable = new PageIterable<>(fetcher);
        List<String> result = new ArrayList<>();

        // 提前中断
        for (String item : iterable) {
            result.add(item);
            if (result.size() >= 3) {
                break;
            }
        }

        assertEquals(3, result.size());
        assertEquals(Arrays.asList("item1", "item2", "item3"), result);
    }
}
