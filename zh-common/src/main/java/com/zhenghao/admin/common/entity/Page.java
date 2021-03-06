package com.zhenghao.admin.common.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis分页参数及查询结果封装,注意所有序号从1开始
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date :2017年11月20日 下午3:39:18
 * Page.java
 */
public class Page<T> {

    /**
     * 页编号:第几页
     */
    private int pageNum = 1;

    /**
     * 页大小:每页的数量
     */
    private int pageSize = 10;

    /**
     * 验证 page size 过大导致内存溢出
     */
    private static final int MAX_PAGE_SIZE = 100;

    // --结果-- //
    /**
     * 查询结果
     */
    private List<T> data = new ArrayList<>();

    /**
     * 总条数
     */
    private int totalCount;

    /**
     * 总页数
     */
    private int totalPages;

    public Page(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        if (this.pageSize > MAX_PAGE_SIZE) {
            this.pageSize = MAX_PAGE_SIZE;
        }
    }

    public Page(Query search) {
        this.pageNum = search.getAsInt("pageNum") == null ? this.pageNum : search.getAsInt("pageNum");
        this.pageSize = search.getAsInt("pageSize") == null ? this.pageSize : search.getAsInt("pageSize");
        if (this.pageSize > MAX_PAGE_SIZE) {
            this.pageSize = MAX_PAGE_SIZE;
        }
    }

    /**
     * 获得当前页面的页号,序号从1开始,默认为1
     *
     * @return
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * 获得每页的记录数量,默认为1.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 根据pageNum和pageSize计算当前页第一条记录在总结果集中的位置
     *
     * @return
     */
    public int getFirst() {
        return ((pageNum - 1) * pageSize) + 1;
    }

    /**
     * 取得页内的记录列表
     *
     * @return
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 设置页内的记录列表.
     */
    public void setData(final List<T> data) {
        this.data = data;
        this.setTotal(data);
    }

    /**
     * 取得总记录数, 默认值为-1.
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总记录数.
     */
    public void setTotal(List<T> data) {
        this.data = data;
        if (data instanceof com.github.pagehelper.Page) {
            this.totalCount = (int) ((com.github.pagehelper.Page) data).getTotal();
            this.totalPages = ((com.github.pagehelper.Page) data).getPages();
        } else {
            this.totalCount = data.size();
            this.totalPages = 1;
        }
    }

    /**
     * 根据pageSize与total计算总页数, 默认值为-1.
     */
    public int getTotalPages() {
        if (totalCount < 0) {
            return -1;
        }
        int pages = totalCount / pageSize;
        return totalCount % pageSize > 0 ? ++pages : pages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
