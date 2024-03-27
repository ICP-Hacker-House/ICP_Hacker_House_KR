package io.blockwavelabs.tree.controller;

import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.dto.link.legacy.LinkAddDto;
import io.blockwavelabs.tree.dto.link.legacy.LinkEditDto;
import io.blockwavelabs.tree.dto.link.legacy.LinkSaveRequestDto;
import io.blockwavelabs.tree.dto.link.response.LinkResponseDto;
import io.blockwavelabs.tree.dto.utils.ResultDto;
import io.blockwavelabs.tree.service.LinkService;
import io.blockwavelabs.tree.service.UserService;
import io.blockwavelabs.tree.service.userdetails.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class LinkController {
    private final LinkService linkService;
    private final UserService userService;

    @PutMapping(value = "/link/edit")
    public ResponseEntity<ResultDto<LinkResponseDto.LinkInfoDto>> editLinkInfo(@AuthenticationPrincipal UserAdapter userAdapter,
                                          @RequestBody LinkEditDto linkEditDto){
        Long userIndex = userAdapter.getUser().getId();
        LinkResponseDto.LinkInfoDto linkInfo = linkService.editingLinkInfo(userIndex, linkEditDto);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), linkInfo));
    }

    @DeleteMapping(value = "/link/remove")
    public ResponseEntity<ResultDto> removeLinkInfo(@AuthenticationPrincipal UserAdapter userAdapter,
                                          @RequestParam(value = "link_id") Long linkId){
        Long userIndex = userAdapter.getUser().getId();
        linkService.removeLinkInfo(userIndex, linkId);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString()));
    }

    @PostMapping(value = "/link/add")
    public ResponseEntity<ResultDto> addLink(@AuthenticationPrincipal UserAdapter userAdapter,
                                     @RequestBody LinkAddDto linkAddDto){
        Long userIndex = userAdapter.getUser().getId();
        linkService.addLink(userIndex, linkAddDto);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString()));
    }

    @GetMapping(value = "/public/link/all")
    public ResponseEntity<ResultDto<List<LinkResponseDto.LinkInfoDto>>> getAllLink(@RequestParam(value = "user_id") String userId){
        List<LinkResponseDto.LinkInfoDto> allLinkInfo = linkService.getAllLinkInfo(userId);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, HttpStatus.OK.toString(), allLinkInfo));
    }


    @PostMapping("/links/new")
    public Long addLink(@RequestParam("userId") String userId, @RequestBody LinkSaveRequestDto linkSaveDto){
        User user = userService.findUserByUserId(userId);
        linkSaveDto.setUser(user);
        return linkService.save(linkSaveDto);
    }
}
