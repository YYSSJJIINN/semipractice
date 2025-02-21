package com.ohgiraffers.specialreservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExhibitionService {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    private UserRepository userRepository;

    // 전시회 게시글 작성
    public void createExhibition(ExhibitionRequest request, String username) {
        // 관리자 권한을 가진 사용자만 게시글을 작성할 수 있도록
        User adminUser = userRepository.findByUsername(username); // 관리자 정보 조회

        // 게시글 객체 생성
        Exhibition exhibition = new Exhibition(request.getTitle(), request.getContent(), adminUser.getId());

        // DB에 게시글 저장
        exhibitionRepository.save(exhibition);
    }

    // 전시회 게시글 삭제
    public void deleteExhibition(int id) {
        exhibitionRepository.deleteById(id);
    }

    // 전시회 게시글 조회 (ID로 조회)
    public Exhibition getExhibitionById(int id) {
        return exhibitionRepository.findById(id).orElse(null); // 게시글이 없으면 null 반환
    }

    // 전시회 게시글 전체 조회 (옵션)
    public List<Exhibition> getAllExhibitions() {
        return exhibitionRepository.findAll();
    }
}
