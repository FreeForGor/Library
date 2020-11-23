package ru.umsch.less1.dao;

import ru.umsch.less1.model.Comment;

import java.util.List;

public interface CommentDao {

    Comment addComment(Comment comment);

    List<String> getCommentsByBookId(Long bookId);

    Comment getCommentById(Long id);

    boolean deleteCommentById(Long id);

    void deleteAll();
}
