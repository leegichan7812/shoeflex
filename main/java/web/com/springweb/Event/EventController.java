package web.com.springweb.Event;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import web.com.springweb.Event.dto.Event;

@RestController
@RequestMapping("/admin/events")
public class EventController {

	
	@Autowired
    private EventService eventService;

	/** 미진행(오늘 이후) 전체 이벤트 목록 */
    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingAll() {
        return ResponseEntity.ok(eventService.getUpcomingAll());
    }

    /**
     * 브랜드/타이틀/이미지 동시 저장(업데이트)
     * POST /admin/events/{calendarId}/slide
     * form-data: brandId(옵션), brandName(옵션), slideTitle(옵션), slideImg(파일 옵션)
     */
    @PostMapping(
	    path = "/{calendarId}/slide",
	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
	    produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Map<String, Object>> saveBrandAndSlide(
	        @PathVariable int calendarId,
	        @RequestParam(required = false) Integer brandId,
	        @RequestParam(required = false) String brandName,
	        @RequestParam(required = false) String slideTitle,
	        @RequestParam(value = "slideImg", required = false) MultipartFile slideImg
	) {
	    System.out.println("slideTitle = " + slideTitle);
	    System.out.println("slideImg   = " + (slideImg != null ? slideImg.getOriginalFilename()+"("+slideImg.getSize()+"B)" : "null"));

	    String imagePath = eventService.upsertBrandAndSlide(calendarId, brandId, brandName, slideTitle, slideImg);
	    Map<String, Object> body = new HashMap<>();
	    body.put("result", "ok");
	    if (imagePath != null) body.put("imagePath", imagePath);
	    return ResponseEntity.ok(body);
	}

    /** 이미지 삭제 */
    @DeleteMapping("/{calendarId}/slide")
    public Map<String, Object> deleteSlide(@PathVariable int calendarId) {
        eventService.deleteSlide(calendarId);
        return Map.of("result","ok");
    }
}
