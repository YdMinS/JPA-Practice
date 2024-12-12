package com.ydmins.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ydmins.querydsl.dto.MemberDto;
import com.ydmins.querydsl.dto.QMemberDto;
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

    @DisplayName("프로젝션 대상이 하나이다.")
    @Test
    public void withQueryDSL4(){
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();
        for (String s : result) {
            System.out.println(s);
        }
    }

    @DisplayName("튜플 프로젝션한다.")
    @Test
    public void withQueryDSL5(){
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();
        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            int age= tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

    @DisplayName("DTO 프로젝션한다.")
    @Test
    public void withJPA6(){
        List<MemberDto> result = em.createQuery("select new com.ydmins.querydsl.dto.MemberDto(m.username, m.age) from" +
                                " Member" +
                        " m",
                        MemberDto.class)
                .getResultList();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

    }

    @DisplayName("Projections.bean으로 DTO 프로젝션한다.")
    @Test
    public void withQueryDSL6(){
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @DisplayName("Projections.fields로 DTO 프로젝션한다.")
    @Test
    public void withQueryDSL7(){
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class, member.username, member.age))
                .from(member)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @DisplayName("QueryProejctdion으로 DTO 프로젝션한다.")
    @Test
    public void withQueryDSL8(){
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @DisplayName("BooleanBuilder로 동적쿼리를 처리한다.")
    @Test
    public void withQueryDSL9(){
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {
        BooleanBuilder builder = new BooleanBuilder();
        if(usernameCond != null){
            builder.and(member.username.eq(usernameCond));
        }
        if(ageCond != null){
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    @DisplayName("Where로 동적쿼리를 처리한다.")
    @Test
    public void withQueryDSL10(){
        String usernameParam = "member1";
        Integer ageParam = 10;
        
        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
                .where(allEq(usernameCond, ageCond))
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond == null ? null : member.username.eq(usernameCond);
    }

    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond == null ? null : member.age.eq(ageCond);
    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond){
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    @DisplayName("bulk update를 수행한다.")
    @Test
    public void withQueryDSL11(){
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();

        em.flush();
        em.clear();

        long count2 = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .set(member.age, member.age.multiply(1))
                .execute();

        em.flush();
        em.clear();

        long count3 = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();

        em.flush();
        em.clear();
    }
}
