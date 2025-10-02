package com.ksanaDock.dao;

import org.apache.ibatis.annotations.Mapper;
import com.ksanaDock.entity.ChatContact;
import com.ksanaDock.model.vo.ChatContactVO;
import java.util.List;

@Mapper
public interface ChatContactDao {
    List<ChatContactVO> selectContactsByUserId(String userId);
    int insert(ChatContact contact);

    void delete(String userId, String contactId);
}