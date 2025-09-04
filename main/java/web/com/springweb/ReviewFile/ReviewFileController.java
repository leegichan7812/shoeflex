package web.com.springweb.ReviewFile;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import web.com.springweb.Review.ReviewService;
import web.com.springweb.ReviewInsert.ReviewInsertDao;

//예: web.com.springweb.review.ReviewFileController
@RestController // 또는 @Controller + @ResponseBody
@RequestMapping("/review")
public class ReviewFileController {

 private final ReviewInsertDao reviewInsertDao; // 기존 DAO 사용 이름 맞추세요
 private final ReviewService reviewService;     // (선택) 소유권/권한 검증용

 public ReviewFileController(ReviewInsertDao reviewInsertDao,
                             ReviewService reviewService) {
     this.reviewInsertDao = reviewInsertDao;
     this.reviewService = reviewService;
 }

 @PostMapping("/file/delete")
 public ResponseEntity<String> deleteReviewFile(@RequestParam("fileId") long fileId,
                                                @RequestParam(value="reviewId", required=false) Long reviewId,
                                                Principal principal) {
     // (권장) 소유권/권한 체크
     // reviewService.assertFileOwnedByUser(fileId, principal.getName());

     int deleted = reviewInsertDao.deleteFileById(fileId);
     if (deleted > 0) {
         // (선택) 물리 파일도 지우려면 파일명 조회 후 삭제 로직 수행
         return ResponseEntity.ok("이미지를 삭제했습니다.");
     }
     return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 파일이 없습니다.");
 }
}
