package io.blockwavelabs.tree.service;

import io.blockwavelabs.tree.domain.link.Link;
import io.blockwavelabs.tree.domain.link.LinkRepository;
import io.blockwavelabs.tree.dto.link.legacy.LinkAddDto;
import io.blockwavelabs.tree.dto.link.legacy.LinkEditDto;
import io.blockwavelabs.tree.dto.link.legacy.LinkSaveRequestDto;
import io.blockwavelabs.tree.dto.link.response.LinkResponseDto;
import io.blockwavelabs.tree.exception.SamTreeException;
import io.blockwavelabs.tree.exception.type.UserExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;

    @Transactional
    public Long save(LinkSaveRequestDto linkSaveRequestDto){

        return linkRepository.save(linkSaveRequestDto.toEntity()).getId();
    }

    @Transactional
    public LinkResponseDto.LinkInfoDto editingLinkInfo(Long userIndex, LinkEditDto linkEditDto){
        Link link = linkRepository.findLinkById(linkEditDto.getId())
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_INFO));
        if (!userIndex.equals(link.getUser().getId())){
            throw new SamTreeException(UserExceptionType.ILLEGAL_REQUEST);
        }
        linkRepository.updateLinkInfo(
                linkEditDto.getTitle().isEmpty() ? link.getLinkTitle() : linkEditDto.getTitle(),
                linkEditDto.getUrl().isEmpty() ? link.getLinkUrl() : linkEditDto.getUrl(),
                linkEditDto.getId());
        return LinkResponseDto.LinkInfoDto.of(linkRepository.findLinkById(linkEditDto.getId()).get());
    }

    @Transactional
    public void removeLinkInfo(Long userIndex, Long linkId){
        Link link = linkRepository.findLinkById(linkId)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_INFO));
        if (!userIndex.equals(link.getUser().getId())){
            throw new SamTreeException(UserExceptionType.ILLEGAL_REQUEST);
        }
        linkRepository.deleteById(linkId);
    }

    @Transactional
    public void addLink(Long userIndex, LinkAddDto linkAddDto){
        linkRepository.addLink(userIndex, linkAddDto.getLinkTitle(), linkAddDto.getLinkUrl());
    }

    @Transactional(readOnly = true)
    public List<LinkResponseDto.LinkInfoDto> getAllLinkInfo(String userId){
        List<LinkResponseDto.LinkInfoDto> linkInfoDtos = new ArrayList<>();
        linkRepository.findAllByUser_UserId(userId)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_INFO))
                .stream()
                .forEach(link -> linkInfoDtos.add(LinkResponseDto.LinkInfoDto.of(link)));
        return linkInfoDtos;
    }
}
