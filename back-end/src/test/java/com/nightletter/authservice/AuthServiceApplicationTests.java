package com.nightletter.authservice;

import com.nightletter.domain.diary.dto.DiaryCreateRequest;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryTarot;
import com.nightletter.domain.diary.entity.DiaryType;
import com.nightletter.domain.diary.repository.DiaryRepository;
import com.nightletter.domain.diary.service.DiaryService;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static com.nightletter.domain.diary.entity.DiaryType.PRIVATE;

@SpringBootTest
class AuthServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
