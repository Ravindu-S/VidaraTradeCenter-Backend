package com.vidara.tradecenter.membership.repository;

import com.vidara.tradecenter.membership.model.UserMembership;
import com.vidara.tradecenter.membership.model.enums.MembershipRecordStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {

    Optional<UserMembership> findByUserIdAndStatus(Long userId, MembershipRecordStatus status);

    List<UserMembership> findByStatusOrderByCreatedAtDesc(MembershipRecordStatus status);

    @Query("SELECT m FROM UserMembership m ORDER BY m.createdAt DESC")
    Page<UserMembership> findAllOrderByCreatedAtDesc(Pageable pageable);

    Page<UserMembership> findByStatusOrderByCreatedAtDesc(MembershipRecordStatus status, Pageable pageable);
}
