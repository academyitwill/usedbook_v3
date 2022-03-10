package com.lotu_us.usedbook.repository;

import com.lotu_us.usedbook.domain.entity.Comment;
import com.lotu_us.usedbook.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c join c.writer m where m.id = :memberId")
    Page<Comment> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
