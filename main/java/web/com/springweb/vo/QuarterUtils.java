package web.com.springweb.vo;

import java.time.LocalDate;
import java.time.YearMonth;

public class QuarterUtils {
	public static class QuarterInfo {
        public int year, quarter;
        public LocalDate startDate, endDate;
        public QuarterInfo(int year, int quarter, LocalDate startDate, LocalDate endDate) {
            this.year = year; this.quarter = quarter; this.startDate = startDate; this.endDate = endDate;
        }
    }
    public static QuarterInfo getQuarterInfo(int quarter) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int currQuarter = (now.getMonthValue()-1)/3+1;
        // 끝난 분기만 올해, 아닌 건 작년
        int baseYear = (quarter < currQuarter) ? year : year-1;
        int[][] q = {{1,3},{4,6},{7,9},{10,12}};
        LocalDate start = LocalDate.of(baseYear, q[quarter-1][0], 1);
        LocalDate end = LocalDate.of(baseYear, q[quarter-1][1], YearMonth.of(baseYear, q[quarter-1][1]).lengthOfMonth());
        return new QuarterInfo(baseYear, quarter, start, end);
    }
}
