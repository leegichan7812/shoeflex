package web.com.springweb.vo;

public class ExceptionLogSearchDto {
	private String keyword = ""; // 검색어
    private int start = 1;       // 페이징 시작 번호
    private int end = 10;        // 페이징 끝 번호
    private int curPage = 1;     // 현재 페이지
    private int pageSize = 10;   // 페이지당 게시물 수
    private int blockSize = 10;
    private int startPage;
    private int endPage;

    // 👉 블록 계산 메서드
    public void setPageBlock(int totalCount) {
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
        this.startPage = ((curPage - 1) / blockSize) * blockSize + 1;
        this.endPage = startPage + blockSize - 1;
        if (endPage > totalPage) endPage = totalPage;
    }

    public void setPaging(int curPage, int pageSize) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.start = (curPage - 1) * pageSize + 1;
        this.end = curPage * pageSize;
    }

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

}
