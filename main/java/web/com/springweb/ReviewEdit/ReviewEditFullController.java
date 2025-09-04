package web.com.springweb.ReviewEdit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import web.com.springweb.vo.Users;

@Controller
@RequestMapping("/review")
public class ReviewEditFullController {
	
    @Autowired
    private ReviewEditFullService reviewEditFullService;

    @PostMapping(value = "/updateFull")
    @ResponseBody
    public String updateFull(HttpSession session,
                             @RequestParam int reviewId,
                             @RequestParam int rating,
                             @RequestParam String content,
                             @RequestParam(required = false) List<Integer> deleteFileIds,
                             @RequestParam(required = false, name="fileIdForReplace") List<Integer> fileIdForReplace,
                             @RequestParam(required = false, name="replaceFiles") List<MultipartFile> replaceFiles,
                             @RequestParam(required = false, name="newFiles") List<MultipartFile> newFiles) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return "로그인이 필요합니다.";

        try {
            reviewEditFullService.updateFull(loginUser.getUserId(), reviewId, rating, content,
                                             deleteFileIds, fileIdForReplace, replaceFiles, newFiles);
            return "리뷰가 수정되었습니다.";
        } catch (IllegalAccessException e) {
            return "권한이 없습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "수정 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
    
}