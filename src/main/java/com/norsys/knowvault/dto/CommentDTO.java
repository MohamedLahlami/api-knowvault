package com.norsys.knowvault.dto;

import com.norsys.knowvault.model.Comment;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class CommentDTO {
    private Long id;
    private String text;
    private UUID userId;
    private Long pageId;

    public static CommentDTO toDto(Comment comment) {
        if (comment == null) return null;

        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        if (comment.getUser() != null) {
            dto.setUserId(comment.getUser().getId());
        }
        if (comment.getPage() != null) {
            dto.setPageId(comment.getPage().getId());
        }

        return dto;
    }

    public static List<CommentDTO> toDtoList(List<Comment> list) {
        return list.stream().map(CommentDTO::toDto).collect(Collectors.toList());
    }

}
