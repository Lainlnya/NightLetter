package com.nightletter.domain.member.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nightletter.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/members")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/nickname")
	public ResponseEntity<?> getMemberNickname() {
		return ResponseEntity.ok(memberService.getMemberNickname());
	}

	@PatchMapping("/nickname")
	public ResponseEntity<?> addDiary(@RequestParam String nickname) {
		return ResponseEntity.ok(memberService.updateMemberNickname(nickname));
	}

	@DeleteMapping("")
	public ResponseEntity<?> removeMember() {
		memberService.deleteMember();
		return ResponseEntity.noContent().build();
	}

}
