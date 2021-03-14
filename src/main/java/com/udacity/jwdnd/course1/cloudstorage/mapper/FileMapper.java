package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    Files getFileByFileName(String fileName, Integer userId);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    Files getFileByFileId(Integer fileId);

    @Insert("INSERT INTO FILES (fileid,filename, contenttype, filesize, userid, filedata) VALUES (#{fileId}, #{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int saveFile(Files file);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<Files> getFilesByUserId(Integer userId);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    void deleteFileByFileId(Integer fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    Files getFileByName(String fileName);
}
