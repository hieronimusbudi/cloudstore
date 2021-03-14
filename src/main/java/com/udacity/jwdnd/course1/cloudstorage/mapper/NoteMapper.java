package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE notetitle = #{noteTitle}")
    List<Notes> getNotesByTitle(String noteTitle);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid ) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    void insert(String noteTitle, String noteDescription, int userId);

    @Update("UPDATE NOTES SET noteTitle = #{noteTitle}, noteDescription = #{noteDescription} WHERE noteId = #{noteId}")
    void update(String noteTitle, String noteDescription, int noteId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Notes> getNotes(int userId);

    @Delete("DELETE FROM NOTES WHERE noteId = #{noteId}")
    void delete(Integer noteId);
}

