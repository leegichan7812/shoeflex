package web.com.springweb.vo;

import java.util.List;
public class PageResult<T> {
    private List<T> content;
    private int page;       // 1-based
    private int size;
    private int totalCount;
    private int totalPages;
    private boolean hasPrev;
    private boolean hasNext;

    // ===== Getter/Setter =====
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public boolean isHasPrev() { return hasPrev; }
    public void setHasPrev(boolean hasPrev) { this.hasPrev = hasPrev; }

    public boolean isHasNext() { return hasNext; }
    public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }
}