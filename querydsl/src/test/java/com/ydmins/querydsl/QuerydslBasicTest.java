package com.ydmins.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ydmins.querydsl.entity.Member;
import com.ydmins.querydsl.entity.QMember;
import com.ydmins.querydsl.entity.Team;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    @BeforeEach
    public void before(){
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
    }

    @DisplayName("JPQL : 멤버를 조회한다.")
    @Test
    public void withJPQL1(){
        String query =
                "select m from Member m" +
                "where m.username =: username";
        Member findMember = em.createQuery(query, Member.class)
                        .setParameter("username","member1")
                        .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @DisplayName("QueryDSL : 멤버를 조회한다.")
    @Test
    public void withQueryDSL1(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember member = new QMember("member");
        Member findMember = queryFactory
                                .select(member)
                                .from(member)
                                .where(member.username.eq("member1"))
                                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

}