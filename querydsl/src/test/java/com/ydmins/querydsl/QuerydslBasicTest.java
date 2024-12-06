package com.ydmins.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ydmins.querydsl.entity.Member;
import com.ydmins.querydsl.entity.Team;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ydmins.querydsl.entity.QMember.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
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
        Member findMember = queryFactory.select(member)
                    .from(member)
                    .where(member.username.eq("member1"))
                    .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @DisplayName("QueryDSL : 멤버를 검색한다.")
    @Test
    public void withQueryDSL2(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @DisplayName("QueryDSL : 집합 쿼리를 실행한다.")
    @Test
    public void withQueryDSL3(){
        List<Tuple> result = queryFactory
                .select(
                    member.count(),
                    member.age.sum(),
                    member.age.avg(),
                    member.age.max(),
                    member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }
}
