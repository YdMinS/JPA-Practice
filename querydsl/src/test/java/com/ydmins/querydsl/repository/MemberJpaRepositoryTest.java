package com.ydmins.querydsl.repository;

import com.ydmins.querydsl.dto.MemberSearchCondition;
import com.ydmins.querydsl.dto.MemberTeamDto;
import com.ydmins.querydsl.entity.Member;
import com.ydmins.querydsl.entity.Team;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired MemberJpaRepository memberJpaRepository;

    @DisplayName("JPA로 Member 엔티티가 저장된다.")
    @Test
    public void test1(){
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result1 = memberJpaRepository.findAll();
        assertThat(result1).containsExactly(member);

        List<Member> result2 = memberJpaRepository.findByUsername("member1");
        assertThat(result2).containsExactly(member);
    }

    @DisplayName("QueryDSL로 Member 엔티티가 저장된다.")
    @Test
    public void test2(){
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result1 = memberJpaRepository.findAll_QueryDSL();
        assertThat(result1).containsExactly(member);

        List<Member> result2 = memberJpaRepository.findByUsername_QueryDSL("member1");
        assertThat(result2).containsExactly(member);
    }

    @DisplayName("Builder로 동적쿼리를 처리한다.")
    @Test
    public void test3(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition memberSearchCondition = new MemberSearchCondition();
        memberSearchCondition.setAgeGoe(35);
        memberSearchCondition.setAgeLoe(41);
        memberSearchCondition.setTeamName("teamB");

        List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(memberSearchCondition);
        assertThat(result).extracting("username").containsExactly("member4");
    }

    @DisplayName("Builder 없이 동적쿼리를 처리한다.")
    @Test
    public void test4(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition memberSearchCondition = new MemberSearchCondition();
        memberSearchCondition.setAgeGoe(35);
        memberSearchCondition.setAgeLoe(41);
        memberSearchCondition.setTeamName("teamB");

        List<MemberTeamDto> result = memberJpaRepository.search(memberSearchCondition);
        assertThat(result).extracting("username").containsExactly("member4");
    }
}