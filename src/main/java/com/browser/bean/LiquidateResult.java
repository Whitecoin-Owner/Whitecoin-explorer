package com.browser.bean;


import java.util.List;

/**
 * Created by 吴晓冬 on 2017/9/9.
 */
public class LiquidateResult {
    private long totalPages;
    private List<?> rows;
    private long currentPage;

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }
}
