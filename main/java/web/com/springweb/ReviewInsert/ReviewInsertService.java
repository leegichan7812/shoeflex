package web.com.springweb.ReviewInsert;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import web.com.springweb.vo.Review;
import web.com.springweb.vo.ReviewFileDto;

@Service
public class ReviewInsertService {

    @Autowired
    private ReviewInsertDao reviewInsertDao;
    
    @Value("${review.upload-dir}")
    private String uploadDir;

    @Value("${review.public-prefix}")
    private String publicPrefix;

    public void insertReviewWithFiles(Review review, List<MultipartFile> reports) throws IOException {
        // 1. 리뷰 본문 저장
        reviewInsertDao.insertReview(review);

        // 2. 파일 저장 (선택)
        if (reports != null && !reports.isEmpty()) {
            for (MultipartFile mf : reports) {
                if (!mf.isEmpty()) {
                    String originalName = mf.getOriginalFilename();

                    // UUID + "_" + 원본명
                    String uuid = java.util.UUID.randomUUID().toString();
                    String saveName = uuid + "_" + originalName;

                    // 저장 경로 (실제 서버 디렉토리)
                    String savePath = uploadDir + "/" + saveName;
                    mf.transferTo(new File(savePath));

                    // DB 저장 (공개 URL /img/review/uuid_name.jpg 형태)
                    ReviewFileDto fileDto = new ReviewFileDto();
                    fileDto.setFname(publicPrefix + saveName); // 접근 가능한 URL
                    fileDto.setEtc(originalName);              // 원본 파일명
                    reviewInsertDao.insertFile(fileDto);
                }
            }
        }
    }
}

