package com.lotu_us.usedbook.repository;

import com.lotu_us.usedbook.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(nativeQuery = true, value =
            "SELECT * " +
            "FROM (SELECT * FROM chat where receiver_member_id = :memberId ORDER BY create_time DESC LIMIT 100) temp " +
            "GROUP BY chatroom_id")
    List<Chat> findByReceiverMemberId(@Param("memberId") Long memberId);

    @Query("select c from Chat c join fetch c.receiver rm join fetch c.sender sm join fetch c.chatRoom cr " +
            "where (rm.id = :receiverId and sm.id = :senderId) or " +
            "(sm.id = :receiverId and rm.id = :senderId) " +
            "group by cr.id")
    Optional<Chat> findByReceiverAndSender(@Param("receiverId") Long receiverId, @Param("senderId") Long senderId);
}
