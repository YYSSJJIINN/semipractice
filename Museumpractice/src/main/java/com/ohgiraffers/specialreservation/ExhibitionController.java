package com.ohgiraffers.specialreservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exhibitions")
public class ExhibitionController {
    @Autowired
    private ExhibitionService exhibitionService;

    @PostMapping("/create")
    public ResponseEntity<String> createExhibition(@RequestBody ExhibitionRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("❌ 관리자만 이 기능을 사용할 수 있습니다.");
        }

        exhibitionService.createExhibition(request, userDetails.getUsername());
        return ResponseEntity.ok("✅ 새 전시회 게시물이 작성되었습니다!");
    }
}

