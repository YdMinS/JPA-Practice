package com.ydmins.data_jpa.repository;

import com.ydmins.data_jpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();
}
