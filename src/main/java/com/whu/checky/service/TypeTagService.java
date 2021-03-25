package com.whu.checky.service;

import com.whu.checky.domain.TypeTag;
import com.whu.checky.domain.UserHobby;

import java.util.List;

public interface TypeTagService {
    List<TypeTag> getTypeTagsBytypeId(String typeId);
    List<TypeTag> getTypeTagsBytagId(String tagId);
    List<TypeTag> getAllTypeTags();
    int addTypeTag(TypeTag typeTag);
    int delTypeTag(TypeTag typeTag);
    void updateTypeTag(String typeId,String tagId);

    List<TypeTag> getTypeTagByTypeTag(String tagId, String typeId);
}
